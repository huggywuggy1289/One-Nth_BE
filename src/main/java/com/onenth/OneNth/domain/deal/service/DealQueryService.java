package com.onenth.OneNth.domain.deal.service;

import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import com.onenth.OneNth.domain.chat.entity.ChatRoomMember;
import com.onenth.OneNth.domain.chat.repository.ChatRoomRepository;
import com.onenth.OneNth.domain.deal.dto.DealResponseDTO;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.deal.repository.DealConfirmationRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.Item;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.ChatHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.onenth.OneNth.domain.deal.converter.DealConverter.toGetAvailableProductDTO;
import static com.onenth.OneNth.domain.deal.converter.DealConverter.toGetDealConfirmationDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealQueryService {

    private final MemberRepository memberRepository;

    private final SharingItemRepository sharingItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final DealConfirmationRepository dealConfirmationRepository;

    public List<DealResponseDTO.getAvailableProductDTO> getAvailableProducts(Long memberId) {
        Member member = findMemberById(memberId);

        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByMemberAndStatus(member, Status.DEFAULT);
        List<SharingItem> sharingItems = sharingItemRepository.findByMemberAndStatus(member, Status.DEFAULT);

        List<DealResponseDTO.getAvailableProductDTO> availableProductDTOs = new ArrayList<>();

        purchaseItems.stream()
                .filter(item -> item.getItemImages() != null && !item.getItemImages().isEmpty())
                .forEach(item -> availableProductDTOs.add(toGetAvailableProductDTO(item, item.getItemImages().get(0))));

        sharingItems.stream()
                .filter(item -> item.getItemImages() != null && !item.getItemImages().isEmpty())
                .forEach(item -> availableProductDTOs.add(toGetAvailableProductDTO(item, item.getItemImages().get(0))));

        return availableProductDTOs;
    }

    public List<DealResponseDTO.GetDealConfirmationDTO> getDealConfirmations(Long memberId, String roomName) {
        Member buyer = findMemberById(memberId);
        ChatRoom chatRoom = findChatRoomByName(roomName);

        Member seller = chatRoom.getChatRoomMembers().stream()
                .map(ChatRoomMember::getMember)
                .filter(m -> !m.equals(buyer))
                .findFirst()
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return dealConfirmationRepository.findByBuyerAndSeller(buyer, seller).stream()
                .map(confirmation -> {
                    Item item = getProductWithImages(confirmation.getItemType(), confirmation.getProductId());
                    return new AbstractMap.SimpleEntry<>(confirmation, item);
                })
                .filter(entry -> entry.getValue().getStatus() != Status.COMPLETED)
                .map(entry -> {
                    Item item = entry.getValue();
                    ItemImage mainImage = item.getItemImages().isEmpty() ? null : item.getItemImages().get(0);
                    return toGetDealConfirmationDTO(item, mainImage, entry.getKey().getId());
                })
                .collect(Collectors.toList());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private ChatRoom findChatRoomByName(String roomName) {
        return chatRoomRepository.findWithChatRoomMembersByName(roomName)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_ROOM_NOT_FOUND));
    }

    private Item getProductWithImages(ItemType itemType, Long productId) {
        return switch(itemType) {
            case PURCHASE -> purchaseItemRepository.findWithItemImagesById(productId)
                    .orElseThrow(() -> new SharingItemHandler(ErrorStatus.PURCHASE_ITEM_NOT_FOUND));
            case SHARE -> sharingItemRepository.findWithItemImagesById(productId)
                    .orElseThrow(() -> new SharingItemHandler(ErrorStatus.SHARING_ITEM_NOT_FOUND));
        };
    }
}