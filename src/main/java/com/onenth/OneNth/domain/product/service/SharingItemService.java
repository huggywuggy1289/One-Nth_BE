package com.onenth.OneNth.domain.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.converter.SharingItemConverter;
import com.onenth.OneNth.domain.product.dto.SharingItemListDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemRequestDTO;
import com.onenth.OneNth.domain.product.dto.SharingItemResponseDTO;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import com.onenth.OneNth.domain.product.entity.scrap.SharingItemScrap;
import com.onenth.OneNth.domain.product.repository.itemRepository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.TagRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.SharingItemScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import com.onenth.OneNth.domain.product.entity.Tag;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SharingItemService {
    private final SharingItemRepository sharingItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final TagRepository tagRepository;
    private final RegionRepository regionRepository;
    private final GeoCodingService geoCodingService;
    //+
    private final SharingItemScrapRepository scrapRepository;
    private final MemberRepository memberRepository;

    private final AmazonS3 amazonS3;

    @Value("${AWS_S3_BUCKET}")
    private String bucketName;

    @Transactional
    public Long registerItem(SharingItemRequestDTO dto, List<MultipartFile> imageFiles, Long userId) {

        if (dto.getItemCategory() == ItemCategory.FOOD) {
            if (dto.getExpirationDate() == null) {
                throw new IllegalArgumentException("식품 카테고리는 소비기한을 입력해야 합니다.");
            }
        } else {
            if (dto.getExpirationDate() != null) {
                throw new IllegalArgumentException("식품 외 카테고리는 소비기한을 입력할 수 없습니다.");
            }
        }

        Member member = Member.builder().id(userId).build();

        GeoCodingResult geo = null;
        Region region;

        if (dto.getPurchaseMethod() == PurchaseMethod.OFFLINE) {
            if (dto.getSharingLocation() == null || dto.getSharingLocation().isBlank()) {
                throw new IllegalArgumentException("오프라인 구매는 거래 장소를 반드시 입력해야 합니다.");
            }

            geo = geoCodingService.getCoordinatesFromAddress(dto.getSharingLocation());
            if (geo == null) {
                throw new IllegalArgumentException("유효한 주소를 입력해주세요.");
            }

            region = regionRepository.findByRegionNameContaining(dto.getSharingLocation())
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("입력한 주소에 해당하는 지역 정보를 찾을 수 없습니다."));
        } else {
            if (dto.getSharingLocation() != null && !dto.getSharingLocation().isBlank()) {
                throw new IllegalArgumentException("온라인 구매는 거래 장소를 입력할 수 없습니다.");
            }

            region = memberRegionRepository.findByMemberId(userId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("회원의 대표지역이 설정되지 않았습니다."))
                    .getRegion();
        }

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

        SharingItem sharingItem = SharingItem.builder()
                .title(dto.getTitle())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .itemCategory(dto.getItemCategory())
                .expirationDate(dto.getExpirationDate())
                .isAvailable(dto.getIsAvailable())
                .purchaseMethod(dto.getPurchaseMethod())
                .member(member)
                .region(region)
                .status(Status.DEFAULT)
                .tags(new ArrayList<>())
                .sharingLocation(dto.getSharingLocation())
                .build();
        sharingItem.getTags().addAll(tagEntities);

        if (dto.getPurchaseMethod() == PurchaseMethod.ONLINE) {
            if (region.getLatitude() == null || region.getLongitude() == null) {
                GeoCodingResult regionGeo = geoCodingService.getCoordinatesFromAddress(region.getRegionName());
                if (regionGeo == null) {
                    throw new IllegalStateException("대표 지역의 위도/경도 정보를 찾을 수 없습니다.");
                }

                region.setLatitude(regionGeo.getLatitude());
                region.setLongitude(regionGeo.getLongitude());
                regionRepository.save(region);
            }

            sharingItem.setLatitude(region.getLatitude());
            sharingItem.setLongitude(region.getLongitude());

        } else {
            sharingItem.setLatitude(geo.getLatitude());
            sharingItem.setLongitude(geo.getLongitude());
        }

        sharingItemRepository.save(sharingItem);

        imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .forEach(file -> {
                    try {
                        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        String s3Key = "sharing/" + filename;

                        ObjectMetadata metadata = new ObjectMetadata();
                        metadata.setContentLength(file.getSize());
                        metadata.setContentType(file.getContentType());

                        amazonS3.putObject(bucketName, s3Key, file.getInputStream(), metadata);
                        String s3Url = amazonS3.getUrl(bucketName, s3Key).toString();

                        boolean exists = amazonS3.doesObjectExist(bucketName, s3Key);
                        if (!exists) {
                            throw new RuntimeException("S3에 파일이 존재하지 않습니다: " + s3Key);
                        }

                        ItemImage image = ItemImage.builder()
                                .sharingItem(sharingItem)
                                .url(s3Url)
                                .itemType(ItemType.SHARE)
                                .build();

                        itemImageRepository.save(image);

                    } catch (IOException e) {
                        throw new RuntimeException("S3 파일 업로드 실패: " + e.getMessage());
                    }
                });

        return sharingItem.getId();
    }

    // 전체 상품 리스트 조회
    @Transactional(readOnly = true)
    public List<SharingItemListDTO> searchItems(String keyword, Long userId) {
        List<Integer> regionIds = memberRegionRepository.findByMemberId(userId)
                .stream()
                .map(r -> r.getRegion().getId())
                .toList();

        List<SharingItem> items = new ArrayList<>();

        if (keyword.startsWith("#")) {
            // 태그 검색 (설정 지역 내)
            String tag = keyword;
            items = sharingItemRepository.findByRegionAndTag(regionIds, tag);
        } else if (isCategory(keyword)) {
            // 카테고리 검색 (설정 지역 내)
            items = sharingItemRepository.findByRegionAndCategory(regionIds, keyword);
        } else if (isRegion(keyword)) {
            // 지역명 검색 (전국)
            items = sharingItemRepository.findByRegionName(keyword);
        }

        List<SharingItemScrap> scraps = scrapRepository.findByUserId(userId);

        // 디버깅
        System.out.println("스크랩 수: " + scraps.size());
        for (SharingItemScrap scrap : scraps) {
            System.out.println("스크랩된 아이템 ID: " + scrap.getSharingItem().getId());
        }

        Set<Long> bookmarkedIds = scraps.stream()
                .map(s -> s.getSharingItem().getId())
                .collect(Collectors.toSet());

        log.info("유저 {}의 북마크 목록: {}", userId, bookmarkedIds);

        return SharingItemConverter.toSharingItemListDTOs(items, bookmarkedIds);
    }

    // 상품명 검색++++
    @Transactional(readOnly = true)
    public List<SharingItemListDTO> searchByTitleInUserRegions(String keyword, Long userId) {

        // 1. 유저 ID 기반 지역 목록 조회
        List<Integer> regionIds = memberRegionRepository.findByMemberId(userId)
                .stream()
                .map(r -> r.getRegion().getId())
                .toList();

        if (regionIds.isEmpty()) {
            throw new IllegalStateException("사용자의 지역 정보가 존재하지 않습니다.");
        }

        List<Status> statuses = List.of(Status.DEFAULT, Status.IN_PROGRESS);

        List<SharingItem> items = sharingItemRepository.searchByTitleAndRegion(
                keyword,
                regionIds,
                statuses
        );

        List<SharingItemScrap> scraps = scrapRepository.findByUserId(userId);
        Set<Long> bookmarkedIds = scraps.stream()
                .map(s -> s.getSharingItem().getId())
                .collect(Collectors.toSet());

        // 3. 거래 완료 제외 후 DTO 변환
        return items.stream()
                .filter(i -> i.getStatus() != Status.COMPLETED)
                .map(item -> SharingItemListDTO.fromEntity(item, bookmarkedIds.contains(item.getId())))
                .toList();
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
        return !regionRepository.findByRegionNameContaining(keyword).isEmpty();
    }

    // 단일 상품 리스트 조회
    @Transactional(readOnly = true)
    public SharingItemResponseDTO.GetResponse getItemDetail(Long sharingItemId, Long userId) {
        SharingItem item = sharingItemRepository.findById(sharingItemId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 함께 나눠요 상품입니다."));

        Member member = item.getMember();
        boolean isVerified = true;
        String regionName = item.getRegion().getRegionName();

        if (item.getStatus() == Status.COMPLETED) {
            throw new NotFoundException("이미 거래 완료된 상품입니다.");
        }

        // 이미지 목록 직접 조회
        List<String> imageUrls = itemImageRepository.findBySharingItemId(sharingItemId)
                .stream()
                .map(ItemImage::getUrl)
                .toList();

        // 태그 문자열 리스트 추출
        List<String> tags = item.getTags().stream()
                .map(Tag::getName)
                .toList();

        return SharingItemResponseDTO.GetResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .itemCategory(item.getItemCategory())
                .expirationDate(item.getExpirationDate())
                .isAvailable(item.getIsAvailable())
                .purchaseMethod(item.getPurchaseMethod())
                .regionName(regionName)
                .imageUrls(imageUrls)
                .tags(tags)
                .writerNickname(member.getNickname())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }
    // 스크랩 설정
    @Transactional
    public void addScrap(Long sharingItemId, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        SharingItem item = sharingItemRepository.findById(sharingItemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        if (scrapRepository.existsByMemberAndSharingItem(member, item)) {
            throw new IllegalStateException("이미 스크랩한 상품입니다.");
        }

        SharingItemScrap scrap = SharingItemScrap.builder()
                .member(member)
                .sharingItem(item)
                .build();

        log.info("스크랩 저장 완료: userId={}, sharingItemId={}", userId, sharingItemId);

        scrapRepository.save(scrap);
    }

    // 북마크 삭제
    @Transactional
    public void removeScrap(Long purchaseItemId, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        SharingItem item = sharingItemRepository.findById(purchaseItemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        SharingItemScrap scrap = scrapRepository.findByMemberIdAndSharingItemId(userId, purchaseItemId)
                .orElseThrow(() -> new IllegalStateException("스크랩 정보가 존재하지 않습니다."));

        scrapRepository.delete(scrap);
        log.info("스크랩 삭제 완료: userId={}, itemId={}", userId, purchaseItemId);
    }
}