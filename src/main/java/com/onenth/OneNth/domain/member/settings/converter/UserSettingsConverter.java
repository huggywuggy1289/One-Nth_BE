package com.onenth.OneNth.domain.member.settings.converter;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.member.settings.dto.UserSettingsResponseDTO;
import com.onenth.OneNth.domain.region.entity.Region;

public class UserSettingsConverter {

    public static MemberRegion toMemberRegion(Member member, Region region) {
        MemberRegion memberRegion = MemberRegion.builder()
                .member(member)
                .region(region)
                .build();

        System.out.println("memberRegions is null? " + (member.getMemberRegions() == null));
        System.out.println("memberRegions instanceof HibernateProxy? " + (member.getMemberRegions() instanceof org.hibernate.collection.spi.PersistentCollection));

        member.getMemberRegions().add(memberRegion);

        return memberRegion;
    }

    public static UserSettingsResponseDTO.AddMyRegionResponseDTO toAddMyRegionResponseDTO(Region region) {
        return UserSettingsResponseDTO.AddMyRegionResponseDTO.builder()
                .regionId(region.getId())
                .regionName(region.getRegionName())
                .build();
    }
}
