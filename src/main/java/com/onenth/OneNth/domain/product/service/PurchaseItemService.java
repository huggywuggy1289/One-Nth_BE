package com.onenth.OneNth.domain.product.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.ItemImageRepository;
import com.onenth.OneNth.domain.product.repository.PurchaseItemRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemImageRepository itemImageRepository;

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
        System.out.println(itemCategory);
        // 식품 카테고리라면 소비기한이 있어야 함
        if (ItemCategory.valueOf(itemCategory) == ItemCategory.FOOD && expirationDate == null) {
            throw new IllegalArgumentException("식품 카테고리 상품은 소비기한을 입력해야 합니다.");
        }

        // 임시 회원 객체
        Member dummy = Member.builder().id(userId).build();
        // 임시 지역 객체
        Region dummyRegion = Region.builder().id(1).build();

        PurchaseItem purchaseItem = PurchaseItem.builder()
                .name(title)
                .purchaseMethod(PurchaseMethod.valueOf(purchaseMethod))
                .itemCategory(ItemCategory.valueOf(itemCategory.trim().replace(",","")))
                .purchaseLocation(purchaseUrl)
                .expirationDate(expirationDate != null ? LocalDate.parse(expirationDate) : null)
                .price(originPrice)
                .status(Status.DEFAULT)
                .member(dummy)                // 임시로 아이디 강제 주입
                .region(dummyRegion)         // 임시로 지역 강제 주입
                //.member(member)             // 회원 연동 시 주석 해제
                //.region(region)             // 지역 연동 시 주석 해제
                .build();

        purchaseItemRepository.save(purchaseItem);

        // 이미지 업로드 처리
        if (imageFiles != null && !imageFiles.isEmpty()) {
            if (imageFiles.size() > 3) {
                throw new IllegalArgumentException("이미지는 최대 3장까지 업로드할 수 있습니다.");
            }

            imageFiles.forEach(file -> {
                try {
                    String uploadDir = "/tmp/uploads"; // 임시 저장 경로
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
        }

        return purchaseItem.getId();
    }

}