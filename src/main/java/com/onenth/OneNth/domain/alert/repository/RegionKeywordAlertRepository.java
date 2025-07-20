package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionKeywordAlertRepository extends JpaRepository<RegionKeywordAlert, Long> {

    int countByMember(Member member);

    boolean existsByMemberAndRegionKeyword(Member member, Region region);

}
