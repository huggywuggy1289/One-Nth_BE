package com.onenth.OneNth.domain.deal.converter;

import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.member.entity.Member;

public class DealConverter {

    public static DealConfirmation toDealConfirmation(
            DealRequestDTO.DealConfirmationRequestDTO request,
            Member member) {
        return DealConfirmation.builder()
                .tradeDate(request.getDealDate())
                .tradePrice(request.getPurchasePrice())
                .originalPrice(request.getOriginalPrice())
                .tradeType(request.getTradeType())
                .itemType(request.getItemType())
                .productId(request.getItemId())
                .member(member)
                .build();
    }

    public static DealCompletion toDealCompletion(
            DealRequestDTO.DealCompletionRequestDTO request,
            Member member, DealConfirmation dealConfirmation
    ){
        return DealCompletion.builder()
                .dealConfirmation(dealConfirmation)
                .member(member)
                .tradeDate(request.getDealDate())
                .tradePrice(request.getTradePrice())
                .tradeCount(request.getTradeCount())
                .tradeType(request.getTradeType())
                .build();
    }
}
