package com.onenth.OneNth.domain.deal.service;

import com.onenth.OneNth.domain.chat.dto.ChatMessageDTO;
import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import com.onenth.OneNth.domain.chat.repository.ChatRoomRepository;
import com.onenth.OneNth.domain.deal.converter.DealConverter;
import com.onenth.OneNth.domain.deal.dto.DealRequestDTO;
import com.onenth.OneNth.domain.deal.entity.CancelledDeal;
import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.deal.entity.enums.CancelReason;
import com.onenth.OneNth.domain.deal.repository.CancelledDealRepository;
import com.onenth.OneNth.domain.deal.repository.DealCompletionRepository;
import com.onenth.OneNth.domain.deal.repository.DealConfirmationRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.Item;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.ChatHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.DealHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DealCommandService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final SharingItemRepository sharingItemRepository;

    private final PurchaseItemRepository purchaseItemRepository;
    private final DealConfirmationRepository dealConfirmationRepository;
    private final DealCompletionRepository dealCompletionRepository;
    private final CancelledDealRepository cancelledDealRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void createDealConfirmation(
            Long memberId, DealRequestDTO.DealConfirmationRequestDTO request, String roomName) {
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

        ChatMessageDTO.DealConfirmationResponseDTO messageDTO =
                ChatMessageDTO.DealConfirmationResponseDTO.builder()
                        .dealConfirmationFormId(result.getId())
                        .sendMemberId(member.getId())
                        .itemName(item.getProductName())
                        .build();

        messagingTemplate.convertAndSend(
                "/sub/chat-rooms/" + roomName,messageDTO);
    }

    @Transactional
    public void createDealCompletion(Long memberId, DealRequestDTO.DealCompletionRequestDTO request, String roomName) {
        Member member = findMemberById(memberId);

        DealConfirmation dealConfirmation = dealConfirmationRepository.findById(request.getDealConfirmationId())
                .orElseThrow(() -> new DealHandler(ErrorStatus.DEAL_CONFIRMATION_NOT_FOUND));

        Item item = getProduct(dealConfirmation.getItemType(), dealConfirmation.getProductId());

        if(item.getStatus()==Status.COMPLETED){
            throw new DealHandler(ErrorStatus.DEAL_COMPLETION_ALREADY);
        }
        boolean exists = dealCompletionRepository.existsByDealConfirmation(dealConfirmation);
        if (exists) {
            throw new DealHandler(ErrorStatus.DEAL_COMPLETION_ALREADY_EXISTS);
        }
        DealCompletion result = DealConverter.toDealCompletion(request, member, dealConfirmation);
        dealCompletionRepository.save(result);

        item.setStatus(Status.COMPLETED);

        ChatRoom chatRoom = chatRoomRepository.findByName(roomName)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));

        ChatMessageDTO.DealCompletionResponseDTO messageDTO =
                ChatMessageDTO.DealCompletionResponseDTO.builder()
                        .dealCompletionFormId(result.getId())
                        .sendMemberId(member.getId())
                        .itemName(item.getProductName())
                        .build();

        messagingTemplate.convertAndSend(
                "/sub/chat-rooms/" + roomName,messageDTO);
    }

    @Transactional
    public void cancelDeal(DealRequestDTO.DealCancelRequestDTO request) {

        DealConfirmation dealConfirmation = dealConfirmationRepository.findById(request.getDealConfirmationId())
                .orElseThrow(() -> new DealHandler(ErrorStatus.DEAL_CONFIRMATION_NOT_FOUND));

        Item item = getProduct(dealConfirmation.getItemType(), dealConfirmation.getProductId());

        if(item.getStatus()!=Status.IN_PROGRESS){
            throw new DealHandler(ErrorStatus.NOT_DEAL_COMPLETION);
        }

        CancelReason cancelReason = request.getCancelReason();
        CancelledDeal cancelledDeal = DealConverter.toCancelledDeal(cancelReason, dealConfirmation);
        cancelledDealRepository.save(cancelledDeal);

        dealConfirmationRepository.delete(dealConfirmation);
        item.setStatus(Status.DEFAULT);
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
