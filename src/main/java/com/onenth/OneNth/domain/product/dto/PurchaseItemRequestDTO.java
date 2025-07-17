package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class PurchaseItemRequestDTO {
    @NotBlank
    private String title;
    @NotNull
    private PurchaseMethod purchaseMethod;
    @NotNull
    private ItemCategory itemCategory;
    @NotBlank
    private String purchaseUrl;
    @NotBlank
    private String purchaseLocation;
    private LocalDate expirationDate;
    @NotNull
    private Integer originPrice;
    @Size(max = 3)
    private List<@NotBlank String> imageUrls;
    @NotEmpty
    private List<String> tags;
}