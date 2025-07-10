package com.onenth.OneNth.domain.product.service;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.DTO.PurchaseItemRequestDTO;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.repository.PurchaseItemRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.ManagedEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.onenth.OneNth.domain.product.entity.enums.Status;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {
    private final PurchaseItemRepository purchaseItemRepository;

    // 상품 등록 조건
    @Transactional
    public long registerItem(PurchaseItemRequestDTO dto, Long userId) {
//        Member member = memberRepository.findById(userId);

        // 식품 카테고리라면 소비기한이 있어야 함
        if (dto.getItemCategory() == ItemCategory.FOOD &&
                dto.getExpirationDate() == null) {
            throw new IllegalArgumentException("식품 카테고리 상품은 소비기한을 입력해야 합니다.");
        }

        // 임시 회원 객체
        Member dummy = Member.builder().id(1L).build();
        // 임시 지역 객체
        Region dummy1 = Region.builder().id(1).build();

        PurchaseItem purchaseItem = PurchaseItem.builder()
                .name(dto.getTitle())
                .purchaseMethod(dto.getPurchaseMethod())
                .itemCategory(dto.getItemCategory())
                .purchaseLocation(dto.getPurchaseUrl())
                .expirationDate(dto.getExpirationDate())
                .price(dto.getOriginPrice())
                .status(Status.DEFAULT)
                .member(dummy)                // 임시로 아이디 강제 주입
                .region(dummy1)               // 임시로 지역 강제 주입
                //.member(member)             // 회원 연동 시 주석 해제
                //.region(region)             // 지역 연동 시 주석 해제
                .build();

        purchaseItemRepository.save(purchaseItem);

        return purchaseItem.getId();
    }

}