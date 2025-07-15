package com.onenth.OneNth.domain.product.DTO;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.enums.TradeMethod;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class PurchaseItemRequestDTO {
    @NotBlank
    private String title; // 상품명
    @NotNull
    private PurchaseMethod purchaseMethod; // 온라인구매 & 오프라인 구매
    @NotNull
    private ItemCategory itemCategory; // 카테고리
    @NotBlank
    private String purchaseUrl; // 구매링크
    @NotBlank
    private String purchaseLocation; // 구매처
    private LocalDate expirationDate; // 소비기한
    @NotNull
    private Integer originPrice; // 상품 원가
    @Size(max = 3)
    private List<@NotBlank String> imageUrls; // 이미지 등록
    @NotEmpty
    private List<String> tags; // 태그입력란 (예: "#핸드폰 #배터리")
}