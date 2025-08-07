package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.repository.itemRepository.purchase.PurchaseItemRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.PurchaseItemScrapRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseItemMapService {

    private final PurchaseItemRepository purchaseItemRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;
    private final PurchaseItemScrapRepository purchaseItemScrapRepository;

    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<PurchaseItem> purchaseItems = purchaseItemRepository.findAllByRegionAndPurchaseMethod(region, PurchaseMethod.OFFLINE);

        Map<String, List<PurchaseItem>> grouped = purchaseItems.stream()
                .collect(Collectors.groupingBy(item -> getLocationKey(item.getLatitude(), item.getLongitude())));

        List<MapResponseDTO.GroupedMarkerSummary> groupedMarkerSummaries = grouped.entrySet().stream()
                .map(entry -> {
                    List<PurchaseItem> groupedPurchaseItems = entry.getValue();

                    String[] latLng = entry.getKey().split("-");

                    List<MapResponseDTO.MarkerSummary> markers = groupedPurchaseItems.stream().map(purchaseItem ->
                            MapConverter.toMarkerSummary(purchaseItem)).toList();

                    return MapConverter.toGroupedMarkerSummary(latLng, markers);
                }).toList();


        return MapConverter.toGetMarkersResponseDTO(groupedMarkerSummaries);

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

    private String getLocationKey(Double latitude, Double longitude) {
        return String.format("%.4f-%.4f", latitude, longitude);
    }
}
