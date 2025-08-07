package com.onenth.OneNth.domain.product.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
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
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import com.onenth.OneNth.domain.product.repository.itemRepository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.TagRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.PurchaseItemScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.PurchasingItemHandler;
import com.onenth.OneNth.global.external.kakao.dto.GeoCodingResult;
import com.onenth.OneNth.global.external.kakao.service.GeoCodingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final MemberRegionRepository memberRegionRepository;
    private  final TagRepository tagRepository;
    private final RegionRepository regionRepository;
    private final GeoCodingService geoCodingService;
    private final MemberRepository memberRepository;
    private final PurchaseItemScrapRepository scrapRepository;

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

        // 등록한 주소명 문자열 기반으로 region 찾아 매핑
        GeoCodingResult geo = geoCodingService.getCoordinatesFromAddress(dto.getPurchaseLocation());
        if (geo == null) {
            throw new IllegalArgumentException("유효한 주소를 입력해주세요.");
        }

        Region region = regionRepository.findByRegionNameContaining(dto.getPurchaseLocation())
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("입력한 주소에 해당하는 지역 정보를 찾을 수 없습니다."));


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

        // 장소입력 유효성
        if (dto.getPurchaseMethod() == PurchaseMethod.OFFLINE) {
            if (dto.getPurchaseLocation() == null || dto.getPurchaseLocation().isBlank()) {
                throw new IllegalArgumentException("오프라인 구매는 거래 장소를 반드시 입력해야 합니다.");
            }

            geo = geoCodingService.getCoordinatesFromAddress(dto.getPurchaseLocation());
            if (geo == null) {
                throw new IllegalArgumentException("유효한 주소를 입력해주세요.");
            }
        } else {
            // 온라인일 경우엔 주소 없어야 함
            if (dto.getPurchaseLocation() != null && !dto.getPurchaseLocation().isBlank()) {
                throw new IllegalArgumentException("온라인 구매는 거래 장소를 입력할 수 없습니다.");
            }
        }

        // PurchaseItem 생성
        PurchaseItem purchaseItem = PurchaseItem.builder()
                .name(dto.getName())
                .purchaseMethod(dto.getPurchaseMethod())
                .itemCategory(dto.getItemCategory())
                .purchaseLocation(dto.getPurchaseUrl())
                .expirationDate(dto.getExpirationDate())
                .price(dto.getPrice())
                .status(Status.DEFAULT)
                .member(member)
                .region(region)
                .tags(new ArrayList<>())
                .build();
        purchaseItem.getTags().addAll(tagEntities);

        // ONLINE이면 대표지역 위도경도 설정
        if (dto.getPurchaseMethod().equals(PurchaseMethod.ONLINE)) {
            Region mainRegion = memberRegionRepository.findByMemberId(userId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("대표 지역이 없습니다."))
                    .getRegion();

            if (mainRegion.getLatitude() == null || mainRegion.getLongitude() == null) {
                GeoCodingResult regionGeo = geoCodingService.getCoordinatesFromAddress(mainRegion.getRegionName());
                if (regionGeo == null) {
                    throw new IllegalStateException("대표 지역의 위도/경도 정보를 찾을 수 없습니다.");
                }

                mainRegion.setLatitude(regionGeo.getLatitude());
                mainRegion.setLongitude(regionGeo.getLongitude());
                regionRepository.save(mainRegion);
            }

            purchaseItem.setLatitude(mainRegion.getLatitude());
            purchaseItem.setLongitude(mainRegion.getLongitude());
        } else {
        if (geo == null) {
            throw new IllegalArgumentException("OFFLINE 주소에서 위도/경도를 가져올 수 없습니다.");
        }

        purchaseItem.setLatitude(geo.getLatitude());
        purchaseItem.setLongitude(geo.getLongitude());
    }

    // 폼 최종저장
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

        List<PurchaseItemScrap> scraps = scrapRepository.findByUserId(userId);

        // 디버깅
        System.out.println("스크랩 수: " + scraps.size());
        for (PurchaseItemScrap scrap : scraps) {
            System.out.println("스크랩된 아이템 ID: " + scrap.getPurchaseItem().getId());
        }

        Set<Long> bookmarkedIds = scraps.stream()
                .map(scrap -> scrap.getPurchaseItem().getId())
                .collect(Collectors.toSet());

        log.info("유저 {}의 북마크 목록: {}", userId, bookmarkedIds);

        return PurchaseItemConverter.toPurchaseItemListDTOs(items, bookmarkedIds);
    }

    // 상품명 검색++++
    @Transactional(readOnly = true)
    public List<PurchaseItemListDTO> searchByTitleInUserRegions(String keyword, Long userId) {

        log.info("검색 요청 - keyword: {}, userId: {}", keyword, userId);

        List<Integer> regionIds = memberRegionRepository.findByMemberId(userId)
                .stream()
                .map(r -> r.getRegion().getId())
                .toList();

        log.info("사용자 설정 지역 ID 목록: {}", regionIds);

        List<PurchaseItem> items = purchaseItemRepository.searchByTitleAndRegion(keyword, regionIds);

        List<PurchaseItemScrap> scraps = scrapRepository.findByUserId(userId);
        Set<Long> bookmarkedIds = scraps.stream()
                .map(s -> s.getPurchaseItem().getId())
                .collect(Collectors.toSet());

        return items.stream()
                .filter(i -> i.getStatus() != Status.COMPLETED)
                .map(item -> PurchaseItemListDTO.fromEntity(item, bookmarkedIds.contains(item.getId())))
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
                .price(item.getPrice())
                .latitude(item.getLatitude())
                .longitude(item.getLongitude())
                .build();
    }

    @Transactional
    public void changeItemStatus(Long groupPurchaseId, Long memberId, Status status) {

        PurchaseItem item = purchaseItemRepository.findById(groupPurchaseId)
                .orElseThrow(() -> new PurchasingItemHandler(ErrorStatus.PURCHASE_ITEM_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!item.getMember().equals(member)) {
            throw new MemberHandler(ErrorStatus._FORBIDDEN);
        }
        item.setStatus(status);
    }

    // 북마크 추가
    @Transactional
    public void addScrap(Long purchaseItemId, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        PurchaseItem item = purchaseItemRepository.findById(purchaseItemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        if (scrapRepository.existsByMemberAndPurchaseItem(member, item)) {
            throw new IllegalStateException("이미 스크랩한 상품입니다.");
        }

        PurchaseItemScrap scrap = PurchaseItemScrap.builder()
                .member(member)
                .purchaseItem(item)
                .build();

        log.info("스크랩 저장 완료: userId={}, itemId={}", userId, purchaseItemId);

        scrapRepository.save(scrap);
    }

    // 북마크 삭제
    @Transactional
    public void removeScrap(Long purchaseItemId, Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다."));

        PurchaseItem item = purchaseItemRepository.findById(purchaseItemId)
                .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));

        PurchaseItemScrap scrap = scrapRepository.findByMemberIdAndPurchaseItemId(userId, purchaseItemId)
                .orElseThrow(() -> new IllegalStateException("스크랩 정보가 존재하지 않습니다."));

        scrapRepository.delete(scrap);
        log.info("스크랩 삭제 완료: userId={}, itemId={}", userId, purchaseItemId);
    }
}