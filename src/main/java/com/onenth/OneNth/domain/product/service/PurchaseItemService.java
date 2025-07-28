package com.onenth.OneNth.domain.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRegionRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.product.dto.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.converter.PurchaseItemConverter;
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
    public long registerItem(
            String title,
            String purchaseMethod,
            String itemCategory,
            String purchaseUrl,
            String expirationDate,
            Integer originPrice,
            List<MultipartFile> imageFiles,
            List<String> tags, // +
            Long userId
    ) {

        // 카테고리 파싱
        ItemCategory category = ItemCategory.valueOf(itemCategory.trim());

        // 소비기한 유효성 체크
        if (category == ItemCategory.FOOD) {
            if (expirationDate == null || expirationDate.trim().isEmpty()) {
                throw new IllegalArgumentException("식품 카테고리 상품은 소비기한을 입력해야 합니다.");
            }
        } else {
            if (expirationDate != null && !expirationDate.trim().isEmpty()) {
                throw new IllegalArgumentException("식품 이외의 상품은 소비기한을 입력할 수 없습니다.");
            }
        }

        // feature/#1병합 후 회원연동
        Member member = Member.builder().id(userId).build();
        // 이어서 회원가입시 등록된 대표지역도 주입
        Region region = memberRegionRepository.findByMemberId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("회원의 대표지역이 설정되지 않았습니다."))
                .getRegion();

        // 태그 필드추가 + #조건 추가
        List<Tag> tagEntities = tags.stream()
                .peek(tagName -> {
                    if (!tagName.startsWith("#")) {
                        throw new IllegalArgumentException("태그는 반드시 #으로 시작해야 합니다: " + tagName);
                    }
                })
                .map(tagName -> tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build())))
                .toList();


        PurchaseItem purchaseItem = PurchaseItem.builder()
                .name(title)
                .purchaseMethod(PurchaseMethod.valueOf(purchaseMethod))
                .itemCategory(ItemCategory.valueOf(itemCategory.trim().replace(",", "")))
                .purchaseLocation(purchaseUrl)
                .expirationDate(expirationDate != null && !expirationDate.trim().isEmpty()
                        ? LocalDate.parse(expirationDate)
                        : null)
                .price(originPrice)
                .status(Status.DEFAULT)
                .member(member)             // 회원 연동 시 주석 해제
                .region(region)             // 지역 연동 시 주석 해제
                .tags(new ArrayList<>()) // +
                .build();
        purchaseItem.getTags().addAll(tagEntities); // +
        purchaseItemRepository.save(purchaseItem);

        // 이미지 유효성 검사
        if (imageFiles == null || imageFiles.isEmpty()) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        // 유효한 파일만 개수 체크
        long validFileCount = imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .count();

        if (validFileCount == 0) {
            throw new IllegalArgumentException("상품 이미지는 최소 1장 이상 첨부해야 합니다.");
        }

        if (validFileCount > 3) {
            throw new IllegalArgumentException("이미지는 최대 3장까지 업로드할 수 있습니다.");
        }

        // nullable true로 하는 대신에 폼에 맞는 쪽 값 검증
        if (purchaseItem == null){
            throw new IllegalArgumentException("같이사요는 PurchaseItem 반드시 지정하세요.");
        }

        // 이미지 업로드 처리 (S3 업로드로 변경)
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

                        // DB저장
                        ItemImage image = ItemImage.builder()
                                .purchaseItem(purchaseItem)
                                .url(s3Url) // S3 URL을 저장
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


    // 단일 상품 리스트 조회
    @Transactional(readOnly = true)
    public PurchaseItemResponseDTO.GetPurchaseItemResponseDTO getItemDetail(Long groupPurchaseId, Long userId) {

        PurchaseItem item = purchaseItemRepository.findById(groupPurchaseId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        Member member = item.getMember();  // 이미 포함돼 있음

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