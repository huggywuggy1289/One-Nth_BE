package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.region.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountMapService implements MapService {

    private final PostRepository postRepository;
    private final RegionResolver regionResolver;

    @Override
    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId, Long regionId) {

        Region region = regionResolver.resolveRegion(userId, regionId);

        List<Post> discountPosts = postRepository.findAllByRegionAndPostTypeWithLocation(region, MarkerType.DISCOUNT);

        List<MapResponseDTO.MarkerSummary> summaries = discountPosts.stream().map(
                discountPost -> MapConverter.toMarkerSummary(discountPost, MarkerType.DISCOUNT)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
    }
}
