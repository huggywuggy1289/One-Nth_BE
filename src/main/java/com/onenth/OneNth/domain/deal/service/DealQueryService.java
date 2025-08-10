package com.onenth.OneNth.domain.deal.service;

import com.onenth.OneNth.domain.deal.dto.DealResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.Status;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.onenth.OneNth.domain.deal.converter.DealConverter.toGetAvailableProductDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DealQueryService {

    private final MemberRepository memberRepository;

    private final SharingItemRepository sharingItemRepository;
    private final PurchaseItemRepository purchaseItemRepository;

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

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
