package com.onenth.OneNth.domain.member.settings.memberRegion.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.settings.memberRegion.dto.MemberRegionResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.domain.Page;

import java.util.List;

public class MemberRegionConverter {

    public static MemberRegion toMemberRegion(Member member, Region region) {
        MemberRegion memberRegion = MemberRegion.builder()
                .member(member)
                .region(region)
                .build();

        member.getMemberRegions().add(memberRegion);

        return memberRegion;
    }

    public static MemberRegionResponseDTO.AddMyRegionResponseDTO toAddMyRegionResponseDTO(MemberRegion memberRegion) {
        return MemberRegionResponseDTO.AddMyRegionResponseDTO.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static MemberRegionResponseDTO.MyRegionListResponseDTO toGetMyRegionsResponseDTO(List<MemberRegionResponseDTO.RegionSummary> regionSummaryList) {
        return MemberRegionResponseDTO.MyRegionListResponseDTO.builder()
                .myRegions(regionSummaryList)
                .build();
    }

    public static MemberRegionResponseDTO.RegionSummary toRegionSummary(MemberRegion memberRegion) {
        return MemberRegionResponseDTO.RegionSummary.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static MemberRegionResponseDTO.UpdateMainRegionResponseDTO toUpdateMainRegionResponseDTO(MemberRegion memberRegion) {
        return MemberRegionResponseDTO.UpdateMainRegionResponseDTO.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static MemberRegionResponseDTO.SearchedRegionSummary toSearchedRegionSummary(Region region) {
        return MemberRegionResponseDTO.SearchedRegionSummary.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public static MemberRegionResponseDTO.Pagination toPagination(Page<Region> regionPage) {
        return MemberRegionResponseDTO.Pagination.builder()
                .page(regionPage.getNumber())
                .size(regionPage.getSize())
                .totalPages(regionPage.getTotalPages())
                .totalElements(regionPage.getTotalElements())
                .isLast(regionPage.isLast())
                .build();
    }

    public static MemberRegionResponseDTO.GetRegionsByKeywordResponseDTO toGetRegionsByKeywordResponseDTO(
            List<MemberRegionResponseDTO.SearchedRegionSummary> regionSummaryList,
            MemberRegionResponseDTO.Pagination pagination
    ) {
        return MemberRegionResponseDTO.GetRegionsByKeywordResponseDTO.builder()
                .regions(regionSummaryList)
                .pagination(pagination)
                .build();
    }
}
