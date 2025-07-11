package com.onenth.OneNth.domain.product.service;

import com.onenth.OneNth.domain.member.entity.Member;
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

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemImageRepository itemImageRepository;
    private final RegionRepository regionRepository;

    // 상품 등록 조건
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

        // 임시 회원 객체
        Member dummy = Member.builder().id(userId).build();
        // 임시 지역 객체
        Region dummyRegion = Region.builder().id(1).build();

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
                .member(dummy)                // 임시로 아이디 강제 주입
                .region(dummyRegion)         // 임시로 지역 강제 주입
                //.member(member)             // 회원 연동 시 주석 해제
                //.region(region)             // 지역 연동 시 주석 해제
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

        // 이미지 업로드 처리
        imageFiles.stream()
                .filter(f -> f != null && !f.isEmpty())
                .forEach(file -> {
                    try {
                        String uploadDir = "/tmp/uploads"; // 임시 저장 경로이니 수정요함.
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
    public List<PurchaseItemListDTO> searchItems(String keyword, List<Long> myRegionIds) {
        // TODO : 계정설정의 회원의 우리 동네 지역목록(최대3개) 기능 완성되면 지역&카테고리&태그별 필터링 로직 구현 예정
        throw new UnsupportedOperationException("조회 로직은 회원/지역 설정 로직이 완료된 뒤 구현됩니다.");
    }
    // 단일 상품 리스트 조회

}