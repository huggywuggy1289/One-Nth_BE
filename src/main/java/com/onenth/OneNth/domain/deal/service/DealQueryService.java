package com.onenth.OneNth.domain.deal.service;

import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import com.onenth.OneNth.domain.chat.entity.ChatRoomMember;
import com.onenth.OneNth.domain.chat.repository.ChatRoomRepository;
import com.onenth.OneNth.domain.deal.dto.DealResponseDTO;
import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.repository.DealCompletionRepository;
import com.onenth.OneNth.domain.deal.repository.DealConfirmationRepository;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.Item;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.entity.review.PurchaseReview;
import com.onenth.OneNth.domain.product.entity.review.SharingReview;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.purchase.PurchaseReviewRepository;
import com.onenth.OneNth.domain.product.repository.reviewRepository.sharing.SharingReviewRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.ChatHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import com.onenth.OneNth.global.apiPayload.exception.handler.SharingItemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.onenth.OneNth.domain.deal.converter.DealConverter.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealQueryService {

    private final MemberRepository memberRepository;

    private final SharingItemRepository sharingItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final DealConfirmationRepository dealConfirmationRepository;
    private final DealCompletionRepository dealCompletionRepository;
    private final PurchaseReviewRepository purchaseReviewRepository;
    private final SharingReviewRepository sharingReviewRepository;

    public List<DealResponseDTO.getProducPreviewtDTO> getAvailableProducts(Long memberId) {
        Member member = findMemberById(memberId);

        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByMemberAndStatus(member, Status.DEFAULT);
        List<SharingItem> sharingItems = sharingItemRepository.findByMemberAndStatus(member, Status.DEFAULT);

        List<DealResponseDTO.getProducPreviewtDTO> availableProductDTOs = new ArrayList<>();

        purchaseItems.stream()
                .filter(item -> item.getItemImages() != null && !item.getItemImages().isEmpty())
                .forEach(item -> availableProductDTOs.add(toGetProductPreviewDTO(item, item.getItemImages().get(0))));

        sharingItems.stream()
                .filter(item -> item.getItemImages() != null && !item.getItemImages().isEmpty())
                .forEach(item -> availableProductDTOs.add(toGetProductPreviewDTO(item, item.getItemImages().get(0))));

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

    public DealResponseDTO.GetMyDealHistoryDTO getMyDealHistory(Long memberId) {
        Member targetMember = findMemberById(memberId);

        List<SharingItem> sharingItems = sharingItemRepository.findByMemberAndStatus(targetMember,Status.COMPLETED);
        List<PurchaseItem> purchaseItems = purchaseItemRepository.findByMemberAndStatus(targetMember, Status.COMPLETED);

        int totalReviewCount = calculateTotalReviewCount(sharingItems, purchaseItems);
        BigDecimal totalRatingSum = calculateTotalRatingSum(sharingItems, purchaseItems);
        double averageRating = calculateAverageRating(totalRatingSum, totalReviewCount);

        List<DealCompletion> dealCompletions = dealCompletionRepository.findBySellerOrBuyer(targetMember, targetMember);
        int totalDealsCount
                = dealCompletions.size();
        int totalDealsAmount = dealCompletions.stream()
                .mapToInt(DealCompletion::getTradePrice)
                .sum();

        List<DealCompletion> purchaseDealCompletions = dealCompletions.stream()
                .filter(dc -> dc.getDealConfirmation().getItemType() == ItemType.PURCHASE)
                .toList();
        int purchaseTotalDealCount = purchaseDealCompletions.size();
        int purchaseTotalDealAmount = purchaseDealCompletions.stream()
                .mapToInt(DealCompletion::getTradePrice)
                .sum();

        List<DealCompletion> shareDealCompletions = dealCompletions.stream()
                .filter(dc -> dc.getDealConfirmation().getItemType() == ItemType.SHARE)
                .toList();
        int shareTotalDealCount = shareDealCompletions.size();
        int shareTotalDealAmount = shareDealCompletions.stream()
                .mapToInt(DealCompletion::getTradePrice)
                .sum();

        List<DealCompletion> buyerDealCompletions = dealCompletions.stream()
                .filter(dc -> dc.getBuyer().equals(targetMember))
                .toList();
        List<DealCompletion> sellerDealCompletions = dealCompletions.stream()
                .filter(dc -> dc.getSeller().equals(targetMember))
                .toList();
        int savedAmount = Stream.concat(
                        sellerDealCompletions.stream(),
                        buyerDealCompletions.stream()
                )
                .mapToInt(DealCompletion::getTradePrice)
                .sum();

        return DealResponseDTO.GetMyDealHistoryDTO.builder()
                .totalReviewRating(averageRating)
                .totalReviewCount(totalReviewCount)
                .savedAmount(savedAmount)
                .totalDealHistory(toDealHistoryDetailDTO(totalDealsCount, totalDealsAmount))
                .purchaseDealHistory(toDealHistoryDetailDTO(purchaseTotalDealCount,purchaseTotalDealAmount))
                .shareDealHistory(toDealHistoryDetailDTO(shareTotalDealCount,shareTotalDealAmount))
                .build();
    }

    public List<DealResponseDTO.getProducPreviewtDTO> getMyDealItems(Long memberId, String reviewStatus) {
        Member targetMember = findMemberById(memberId);
        List<DealCompletion> dealCompletions = dealCompletionRepository.findBySellerOrBuyer(targetMember, targetMember);

        if(reviewStatus.equals("all")) {
            return dealCompletions.stream()
                    .map(DealCompletion::getDealConfirmation)
                    .map(dc -> {
                        Item item = getProductWithImages(dc.getItemType(), dc.getProductId());
                        ItemImage firstImage = item.getItemImages().isEmpty() ? null : item.getItemImages().get(0);
                        return toGetProductPreviewDTO(item, firstImage);
                    })
                    .collect(Collectors.toList());
        } else {
            List<PurchaseItem> purchaseReviews = purchaseReviewRepository.findByMember(targetMember).stream()
                    .map(PurchaseReview::getPurchaseItem).toList();
            List<SharingItem> sharingReviews = sharingReviewRepository.findByMember(targetMember).stream()
                    .map(SharingReview::getSharingItem).toList();

            Set<Long> reviewedPurchaseItemIds = purchaseReviews.stream()
                    .map(PurchaseItem::getId)
                    .collect(Collectors.toSet());
            Set<Long> reviewedSharingItemIds = sharingReviews.stream()
                    .map(SharingItem::getId)
                    .collect(Collectors.toSet());

            return dealCompletions.stream()
                    .map(DealCompletion::getDealConfirmation)
                    .filter(dc -> {
                        if ("PURCHASE".equals(dc.getItemType())) {
                            return !reviewedPurchaseItemIds.contains(dc.getProductId());
                        } else if ("SHARING".equals(dc.getItemType())) {
                            return !reviewedSharingItemIds.contains(dc.getProductId());
                        }
                        return true;
                    })
                    .map(dc -> {
                        Item item = getProductWithImages(dc.getItemType(), dc.getProductId());
                        ItemImage firstImage = item.getItemImages().isEmpty() ? null : item.getItemImages().get(0);
                        return toGetProductPreviewDTO(item, firstImage);
                    })
                    .collect(Collectors.toList());
        }
    }

    private int calculateTotalReviewCount(List<SharingItem> sharingItems, List<PurchaseItem> purchaseItems) {
        return sharingItems.stream()
                .mapToInt(item -> item.getSharingReviews().size())
                .sum() +
                purchaseItems.stream()
                        .mapToInt(item -> item.getPurchaseReviews().size())
                        .sum();
    }

    private BigDecimal calculateTotalRatingSum(List<SharingItem> sharingItems, List<PurchaseItem> purchaseItems) {
        BigDecimal sharingSum = sharingItems.stream()
                .flatMap(item -> item.getSharingReviews().stream())
                .map(SharingReview::getRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal purchaseSum = purchaseItems.stream()
                .flatMap(item -> item.getPurchaseReviews().stream())
                .map(PurchaseReview::getRate)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sharingSum.add(purchaseSum);
    }

    private double calculateAverageRating(BigDecimal totalRatingSum, int totalReviewCount) {
        if (totalReviewCount == 0) return 0.0;

        return totalRatingSum.divide(BigDecimal.valueOf(totalReviewCount), 1, RoundingMode.HALF_UP)
                .doubleValue();
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