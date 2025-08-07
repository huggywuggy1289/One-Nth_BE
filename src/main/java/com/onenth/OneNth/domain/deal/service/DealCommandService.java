package com.onenth.OneNth.domain.deal.service;

import com.onenth.OneNth.domain.deal.converter.DealConverter;
import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.deal.repository.DealConfirmationRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.Item;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.DealHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealCommandService {

    private final MemberRepository memberRepository;

    private final SharingItemRepository sharingItemRepository;

    private final PurchaseItemRepository purchaseItemRepository;
    private final DealConfirmationRepository dealConfirmationRepository;

    @Transactional
    public void createDealConfirmation(Long memberId, DealRequestDTO.DealConfirmationRequestDTO request) {
        Member member = findMemberById(memberId);
        Item item = getProduct(request.getItemType(), request.getItemId());

        boolean exists = dealConfirmationRepository.existsByProductIdAndItemType(
                request.getItemId(), request.getItemType());
        if (exists) {
            throw new DealHandler(ErrorStatus.DEAL_CONFIRMATION_ALREADY_EXISTS);
        }
        DealConfirmation result = DealConverter.toDealConfirmation(request, member);
        dealConfirmationRepository.save(result);

        item.setStatus(Status.IN_PROGRESS);
    }

    private Item getProduct(ItemType itemType, Long productId) {
        return switch(itemType) {
            case PURCHASE -> purchaseItemRepository.findById(productId)
                    .orElseThrow(() -> new SharingItemHandler(ErrorStatus.PURCHASE_ITEM_NOT_FOUND));
            case SHARE -> sharingItemRepository.findById(productId)
                    .orElseThrow(() -> new SharingItemHandler(ErrorStatus.SHARING_ITEM_NOT_FOUND));
        };
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
