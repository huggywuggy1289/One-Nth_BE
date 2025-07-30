package com.onenth.OneNth.domain.member.settings.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsRequestDTO;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;

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

    public static UserSettingsResponseDTO.AddMyRegionResponseDTO toAddMyRegionResponseDTO(Region region) {
        return UserSettingsResponseDTO.AddMyRegionResponseDTO.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public static UserSettingsResponseDTO.MyRegionListResponseDTO toGetMyRegionsResponseDTO(List<UserSettingsResponseDTO.RegionSummary> regionSummaryList) {
        return UserSettingsResponseDTO.MyRegionListResponseDTO.builder()
                .myRegions(regionSummaryList)
                .build();
    }

    public static UserSettingsResponseDTO.RegionSummary toRegionSummary(Region region) {
        return UserSettingsResponseDTO.RegionSummary.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }

    public static UserSettingsResponseDTO.UpdateMainRegionResponseDTO toUpdateMainRegionResponseDTO(MemberRegion memberRegion) {
        return UserSettingsResponseDTO.UpdateMainRegionResponseDTO.builder()
                .regionId(memberRegion.getRegion().getId())
                .regionName(memberRegion.getRegion().getRegionName())
                .isMain(memberRegion.isMain())
                .build();
    }
}
