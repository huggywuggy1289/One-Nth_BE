package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.RegionKeywordAlert;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionKeywordAlertRepository extends JpaRepository<RegionKeywordAlert, Long> {

    int countByMember(Member member);

    boolean existsByMemberAndRegionKeyword(Member member, Region region);

    Optional<RegionKeywordAlert> findByIdAndMember(Long id, Member member);
}
