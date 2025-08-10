package com.onenth.OneNth.domain.deal.converter;

import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.dto.DealResponseDTO;
import com.onenth.OneNth.domain.deal.entity.CancelledDeal;
import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.deal.entity.enums.CancelReason;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.Item;
import com.onenth.OneNth.domain.product.entity.ItemImage;

public class DealConverter {

    public static DealConfirmation toDealConfirmation(
            DealRequestDTO.DealConfirmationRequestDTO request,
            Member seller, Member buyer) {
        return DealConfirmation.builder()
                .tradeDate(request.getDealDate())
                .tradePrice(request.getPurchasePrice())
                .originalPrice(request.getOriginalPrice())
                .tradeType(request.getTradeType())
                .itemType(request.getItemType())
                .productId(request.getItemId())
                .seller(seller)
                .buyer(buyer)
                .build();
    }

    public static DealCompletion toDealCompletion(
            DealRequestDTO.DealCompletionRequestDTO request,
            Member seller, Member buyer, DealConfirmation dealConfirmation
    ){
        return DealCompletion.builder()
                .dealConfirmation(dealConfirmation)
                .seller(seller)
                .buyer(buyer)
                .tradeDate(request.getDealDate())
                .tradePrice(request.getTradePrice())
                .tradeCount(request.getTradeCount())
                .tradeType(request.getTradeType())
                .build();
    }

    public static CancelledDeal toCancelledDeal(
            CancelReason cancelReason, DealConfirmation dealConfirmation){
        return CancelledDeal.builder()
                .cancelReason(cancelReason)
                .productId(dealConfirmation.getProductId())
                .itemType(dealConfirmation.getItemType())
                .build();
    }

    public static DealResponseDTO.getAvailableProductDTO toGetAvailableProductDTO(
            Item item, ItemImage itemImage){
        return DealResponseDTO.getAvailableProductDTO.builder()
                .itemId(item.getId())
                .itemType(item.getItemType())
                .itemName(item.getProductName())
                .itemImageUrl(itemImage != null ? itemImage.getUrl() : null)
                .build();
    }
}
