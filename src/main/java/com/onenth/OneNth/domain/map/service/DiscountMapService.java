package com.onenth.OneNth.domain.map.service;

import com.onenth.OneNth.domain.map.converter.MapConverter;
import com.onenth.OneNth.domain.map.dto.MapResponseDTO;
import com.onenth.OneNth.domain.map.enums.MarkerType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRegionRepository;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.repository.PostRepository;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import com.onenth.OneNth.domain.region.entity.Region;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountMapService implements MapService {

    private final MemberRepository memberRepository;
    private final MemberRegionRepository memberRegionRepository;
    private final PostRepository postRepository;

    @Override
    public MapResponseDTO.GetMarkersResponseDTO getMarkers(Long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        MemberRegion mainMemberRegion = memberRegionRepository.findByMemberAndIsMainTrue(member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MAIN_REGION_NOT_FOUND));

        Region region = mainMemberRegion.getRegion();

        List<Post> discountPosts = postRepository.findAllByRegionAndPostTypeWithLocation(region, MarkerType.DISCOUNT);

        List<MapResponseDTO.MarkerSummary> summaries = discountPosts.stream().map(
                discountPost -> MapConverter.toMarkerSummary(discountPost, MarkerType.DISCOUNT)
        ).collect(Collectors.toList());

        return MapConverter.toGetMarkersResponseDTO(summaries);
    }
}
