package com.onenth.OneNth.domain.member.settings.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.domain.Page;

import java.util.List;

public class UserSettingsConverter {

    public static MemberRegion toMemberRegion(Member member, Region region) {
        MemberRegion memberRegion = MemberRegion.builder()
                .member(member)
                .region(region)
                .build();

        member.getMemberRegions().add(memberRegion);

        return memberRegion;
    }

    public static UserSettingsResponseDTO.AddMyRegionResponseDTO toAddMyRegionResponseDTO(MemberRegion memberRegion) {
        return UserSettingsResponseDTO.AddMyRegionResponseDTO.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static UserSettingsResponseDTO.MyRegionListResponseDTO toGetMyRegionsResponseDTO(List<UserSettingsResponseDTO.RegionSummary> regionSummaryList) {
        return UserSettingsResponseDTO.MyRegionListResponseDTO.builder()
                .myRegions(regionSummaryList)
                .build();
    }

    public static UserSettingsResponseDTO.RegionSummary toRegionSummary(MemberRegion memberRegion) {
        return UserSettingsResponseDTO.RegionSummary.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static UserSettingsResponseDTO.UpdateMainRegionResponseDTO toUpdateMainRegionResponseDTO(MemberRegion memberRegion) {
        return UserSettingsResponseDTO.UpdateMainRegionResponseDTO.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }

    public static UserSettingsResponseDTO.SearchedRegionSummary toSearchedRegionSummary(Region region) {
        return UserSettingsResponseDTO.SearchedRegionSummary.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public static UserSettingsResponseDTO.Pagination toPagination(Page<Region> regionPage) {
        return UserSettingsResponseDTO.Pagination.builder()
                .page(regionPage.getNumber())
                .size(regionPage.getSize())
                .totalPages(regionPage.getTotalPages())
                .totalElements(regionPage.getTotalElements())
                .isLast(regionPage.isLast())
                .build();
    }

    public static UserSettingsResponseDTO.GetRegionsByKeywordResponseDTO toGetRegionsByKeywordResponseDTO(
            List<UserSettingsResponseDTO.SearchedRegionSummary> regionSummaryList,
            UserSettingsResponseDTO.Pagination pagination
    ) {
        return UserSettingsResponseDTO.GetRegionsByKeywordResponseDTO.builder()
                .regions(regionSummaryList)
                .pagination(pagination)
                .build();
    }
}
