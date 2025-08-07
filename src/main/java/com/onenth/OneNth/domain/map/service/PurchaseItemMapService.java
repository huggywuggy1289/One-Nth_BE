package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.repository.scrapRepository.ScrapRepository;
import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.PurchaseItemScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseItemMapService implements MapService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;
    private final PurchaseItemScrapRepository purchaseItemScrapRepository;

    @Override
    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<PurchaseItem> purchaseItems = purchaseItemRepository.findAllByRegionAndPurchaseMethod(region, PurchaseMethod.OFFLINE);

        List<MapResponseDTO.MarkerSummary> summaries = purchaseItems.stream().map(
                purchaseItem -> MapConverter.toMarkerSummary(purchaseItem)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
    }

    public MapResponseDTO.GetItemMarkerDetailsResponseDTO getMarkerDetails(Long userId, List<Long> itemIds) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<PurchaseItem> purchaseItems = itemIds.stream().map(
                itemId -> purchaseItemRepository.findById(itemId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.PURCHASE_ITEM_NOT_FOUND))
        ).collect(Collectors.toList());

        List<MapResponseDTO.ItemMarkerDetail> itemMarkerDetails = purchaseItems.stream().map(
                purchaseItem -> MapConverter.toItemMarkerDetail(
                        purchaseItem,
                        purchaseItemScrapRepository.existsByMemberAndPurchaseItem(member, purchaseItem)
                )
        ).collect(Collectors.toList());

        return MapConverter.toGetItemMarkerDetailsResponseDTO(itemMarkerDetails);

    }
}
