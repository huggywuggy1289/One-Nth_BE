package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.product.entity.enums.PurchaseMethod;
import com.onenth.OneNth.domain.product.repository.itemRepository.sharing.SharingItemRepository;
import com.onenth.OneNth.domain.product.repository.scrapRepository.SharingItemScrapRepository;
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
public class SharingItemMapService {

    private final SharingItemRepository sharingItemRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;
    private final SharingItemScrapRepository sharingItemScrapRepository;

    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<SharingItem> sharingItems = sharingItemRepository.findAllByRegionAndPurchaseMethod(region, PurchaseMethod.OFFLINE);

        Map<String, List<SharingItem>> grouped = sharingItems.stream()
                .collect(Collectors.groupingBy(item -> getLocationKey(item.getLatitude(), item.getLongitude())));

        List<MapResponseDTO.GroupedMarkerSummary> groupedMarkerSummaries = grouped.entrySet().stream()
                .map(entry -> {
                    List<SharingItem> groupedSharingItems = entry.getValue();

                    String[] latLng = entry.getKey().split("-");

                    List<MapResponseDTO.MarkerSummary> markers = groupedSharingItems.stream().map(sharingItem ->
                            MapConverter.toMarkerSummary(sharingItem)).toList();

                    return MapConverter.toGroupedMarkerSummary(latLng, markers);
                }).toList();

        return MapConverter.toGetMarkersResponseDTO(groupedMarkerSummaries);
    }

    public MapResponseDTO.GetItemMarkerDetailsResponseDTO getMarkerDetails(Long userId, List<Long> itemIds) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<SharingItem> sharingItems = itemIds.stream().map(
                itemId -> sharingItemRepository.findById(itemId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.SHARING_ITEM_NOT_FOUND))
        ).collect(Collectors.toList());

        List<MapResponseDTO.ItemMarkerDetail> itemMarkerDetails = sharingItems.stream().map(
                sharingItem -> MapConverter.toItemMarkerDetail(
                        sharingItem,
                        sharingItemScrapRepository.existsByMemberAndSharingItem(member, sharingItem)
                )
        ).collect(Collectors.toList());

        return MapConverter.toGetItemMarkerDetailsResponseDTO(itemMarkerDetails);

    }

    private String getLocationKey(Double latitude, Double longitude) {
        return String.format("%.4f-%.4f", latitude, longitude);
    }
}
