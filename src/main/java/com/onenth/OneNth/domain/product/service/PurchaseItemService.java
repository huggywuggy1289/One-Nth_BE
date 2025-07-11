package com.onenth.OneNth.domain.product.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.product.DTO.PurchaseItemListDTO;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.PurchaseItemRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.domain.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.onenth.OneNth.domain.product.entity.QSharingItem.sharingItem;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final RegionRepository regionRepository;
    private final MemberRegionRepository memberRegionRepository; // 검색 필터링시

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
                .build();

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

        // 이미지 업로드 처리
        imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .forEach(file -> {
                    try {
                        String uploadDir = "C:\\Users\\손재윤\\OneDrive\\바탕 화면\\uploads"; // 임시 저장 경로이니 수정요함.
                        File dir = new File(uploadDir);
                        if (!dir.exists()) dir.mkdirs();

                        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                        String filePath = uploadDir + "/" + filename;

                        // 실제 파일 저장
                        file.transferTo(new File(filePath));

                        ItemImage image = ItemImage.builder()
                                .purchaseItem(purchaseItem)
                                .url(filePath) // 임시로 파일 경로를 저장
                                .itemType(ItemType.PURCHASE)
                                .build();

                        itemImageRepository.save(image);
                    } catch (IOException e) {
                        throw new RuntimeException("파일 저장 실패: " + e.getMessage());
                    }
                });

        return purchaseItem.getId();
    }

    // 전체 상품 리스트 조회 - 지역명, 카테고리명 , 태그명(보류)
    @Transactional(readOnly = true)
    public List<PurchaseItemListDTO> searchItems(String keyword, Long userId) {
        // 대표 지역 1개 가져오기
        Region region = memberRegionRepository.findByMemberId(userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("회원의 지역이 설정되지 않았습니다."))
                .getRegion();

        // QueryDSL 조회 조건
        List<PurchaseItem> items;

        if (keyword.startsWith("#")) {
            // 태그 검색은 미구현
            throw new UnsupportedOperationException("태그 검색은 현재 지원되지 않습니다.");
        } else if (isCategory(keyword)) {
            // 카테고리 검색 (대표 지역 내)
            items = purchaseItemRepository.findByRegionAndCategory(region.getId(), keyword);
        } else {
            // 지역명 검색 (모든 지역 대상)
            items = purchaseItemRepository.findByRegionName(keyword);
        }

        // DTO 변환
        return items.stream()
                .map(PurchaseItemListDTO::fromEntity)
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

    // 단일 상품 리스트 조회
    }