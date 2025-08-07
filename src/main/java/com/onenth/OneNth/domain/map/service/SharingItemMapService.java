package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharingItemMapService implements MapService {

    private final SharingItemRepository sharingItemRepository;
    private final RegionResolver regionResolver;
    private final MemberRepository memberRepository;
    private final SharingItemScrapRepository sharingItemScrapRepository;

    @Override
    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<SharingItem> sharingItems = sharingItemRepository.findAllByRegionAndPurchaseMethod(region, PurchaseMethod.OFFLINE);

        List<MapResponseDTO.MarkerSummary> summaries = sharingItems.stream().map(
                sharingItem -> MapConverter.toMarkerSummary(sharingItem)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
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
}
