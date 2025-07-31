package com.onenth.OneNth.domain.product.dto;

import com.onenth.OneNth.domain.product.entity.enums.ItemCategory;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SharingItemRequestDTO {

    @NotBlank
    private String title;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Min(0)
    private Integer price;

    @NotNull
    private ItemCategory itemCategory;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;

    @NotNull
    private Boolean isAvailable;

    @NotNull
    private PurchaseMethod purchaseMethod;

    @NotNull
    private Long regionId;

    @Size(min = 1, max = 3)
    private List<@NotBlank String> imageUrls;

    @NotEmpty
    private List<String> tags;

    @NotBlank
    private String sharingLocation; // 지도마커때문에 추가
}

