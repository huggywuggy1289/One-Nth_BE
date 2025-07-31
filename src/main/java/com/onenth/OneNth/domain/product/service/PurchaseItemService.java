package com.onenth.OneNth.domain.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.converter.PurchaseItemConverter;
import com.onenth.OneNth.domain.product.dto.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.PurchaseItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.Tag;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.TagRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRegionRepository memberRegionRepository; // 검색 필터링시
    private  final TagRepository tagRepository; // +
    private final RegionRepository regionRepository;

    //s3 연동
    private final AmazonS3 amazonS3;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    // 같이사요 상품 등록
    @Transactional
    public long registerItem(PurchaseItemRequestDTO dto, List<MultipartFile> imageFiles, Long userId) {

        // 소비기한 유효성 체크
        if (dto.getItemCategory() == ItemCategory.FOOD) {
            if (dto.getExpirationDate() == null) {
                throw new IllegalArgumentException("식품 카테고리 상품은 소비기한을 입력해야 합니다.");
            }
        } else {
            if (dto.getExpirationDate() != null) {
                throw new IllegalArgumentException("식품 이외의 상품은 소비기한을 입력할 수 없습니다.");
            }
        }

        // 회원 및 지역 조회
        Member member = Member.builder().id(userId).build();
        Region region = memberRegionRepository.findByMemberId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("회원의 대표지역이 설정되지 않았습니다."))
                .getRegion();

        // 태그 유효성 검사 및 저장
        List<Tag> tagEntities = dto.getTags().stream()
                .peek(tag -> {
                    if (!tag.startsWith("#")) {
                        throw new IllegalArgumentException("태그는 반드시 #으로 시작해야 합니다: " + tag);
                    }
                })
                .map(tag -> tagRepository.findByName(tag)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tag).build())))
                .toList();

        if (tagEntities.size() > 5) {
            throw new IllegalArgumentException("태그는 최대 5개까지 입력 가능합니다.");
        }

        // PurchaseItem 생성
        PurchaseItem purchaseItem = PurchaseItem.builder()
                .name(dto.getName())
                .purchaseMethod(dto.getPurchaseMethod())
                .itemCategory(dto.getItemCategory())
                .purchaseLocation(dto.getPurchaseUrl())
                .expirationDate(dto.getExpirationDate())
                .price(dto.getOriginPrice())
                .status(Status.DEFAULT)
                .member(member)
                .region(region)
                .tags(new ArrayList<>())
                .build();
        purchaseItem.getTags().addAll(tagEntities);
        purchaseItemRepository.save(purchaseItem);

        // 이미지 유효성 검사
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        long validFileCount = imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .count();

        if (validFileCount < 1) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        if (validFileCount > 3) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드할 수 있습니다.");
        }

        // 이미지 업로드 (S3)
        imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .forEach(file -> {
                    try {
                        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        String s3Key = "purchase/" + filename;

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(file.getSize());
                        metadata.setContentType(file.getContentType());

                        amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);
                        String s3Url = amazonS3.getUrl(bucketName, s3Key).toString();

                        // 디버깅
                        boolean exists = amazonS3.doesObjectExist(bucketName, s3Key);
                        if (!exists) {
                            throw new RuntimeException("S3에 파일이 존재하지 않습니다: " + s3Key);
                        } else {
                            System.out.println(s3Key + " : 존재합니다");
                        }
                        ItemImage image = ItemImage.builder()
                                .purchaseItem(purchaseItem)
                                .url(s3Url)
                                .itemType(ItemType.PURCHASE)
                                .build();

                        itemImageRepository.save(image);
                    } catch (IOException e) {
                        throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage());
                    }
                });

        return purchaseItem.getId();
    }

    // 전체 상품 리스트 조회
    @Transactional(readOnly = true)
    public List<PurchaseItemListDTO> searchItems(String keyword, Long userId) {
        List<Integer> regionIds = memberRegionRepository.findByMemberId(userId)
                .stream()
                .map(r -> r.getRegion().getId())
                .toList();

        List<PurchaseItem> items = new ArrayList<>();
            // 태그 검색 (설정 지역 내)
        if (keyword.startsWith("#")) {
            String tag = keyword;
            items = purchaseItemRepository.findByRegionsAndTag(regionIds, tag);
        } else if (isCategory(keyword)) {
            // 카테고리 검색 (설정 지역 내)
            items = purchaseItemRepository.findByRegionsAndCategory(regionIds, keyword);
        } else if (isRegion(keyword)){
            // 지역명 검색 (모든 지역)
            items = purchaseItemRepository.findByRegionsName(keyword);
        }
        System.out.println("keyword: [" + keyword + "]");

        return PurchaseItemConverter.toPurchaseItemListDTOs(items);
    }

    private boolean isCategory(String keyword) {
        try {
            ItemCategory.valueOf(keyword.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
            }
        }

    private boolean isRegion(String keyword){
        return regionRepository.findByRegionNameContaining(keyword).isPresent();
    }

    // 상품명 검색++++
    @Transactional(readOnly = true)
    public List<PurchaseItemListDTO> searchByTitleWithRegions(String keyword, List<Integer> regionIds) {
        List<PurchaseItem> items;

        if (regionIds == null || regionIds.isEmpty()) {
            // 전국 검색
            items = purchaseItemRepository.findByNameContainingIgnoreCase(keyword)
                    .stream()
                    .filter(i -> i.getStatus() != Status.COMPLETED)
                    .toList(); // +
        } else {
            items = purchaseItemRepository.searchByTitleAndRegion(keyword, regionIds);
        }

        return items.stream().map(PurchaseItemListDTO::fromEntity).toList();
    }



    // 단일 상품 리스트 조회
    @Transactional(readOnly = true)
    public PurchaseItemResponseDTO.GetPurchaseItemResponseDTO getItemDetail(Long groupPurchaseId, Long userId) {

        PurchaseItem item = purchaseItemRepository.findById(groupPurchaseId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        Member member = item.getMember();

        if (item.getStatus() == Status.COMPLETED) {
            throw new NotFoundException("이미 거래 완료된 상품입니다.");
        }

        boolean isVerified = true;
        String profileImageUrl = null;

        List<String> imageUrls = itemImageRepository.findByPurchaseItemId(groupPurchaseId)
                .stream()
                .map(ItemImage::getUrl)
                .toList();

        return PurchaseItemResponseDTO.GetPurchaseItemResponseDTO.builder()
                .title(item.getName())
                .imageUrls(imageUrls)
                .purchaseUrl(item.getPurchaseLocation())
                .expirationDate(
                        item.getItemCategory() == ItemCategory.FOOD
                                ? item.getExpirationDate()
                                : null
                )
                .writerNickname(member.getNickname())
                .writerProfileImageUrl(profileImageUrl)
                .writerVerified(isVerified)
                .itemCategory(item.getItemCategory())
                .purchaseMethod(item.getPurchaseMethod())
                .originPrice(item.getPrice())
                .build();
    }

}