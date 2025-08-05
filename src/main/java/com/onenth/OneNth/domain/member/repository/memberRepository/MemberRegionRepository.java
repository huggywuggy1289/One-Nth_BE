package com.onenth.OneNth.domain.member.repository.memberRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberRegion;
import com.onenth.OneNth.domain.region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRegionRepository extends JpaRepository<MemberRegion, Long> {
    List<MemberRegion> findByMemberId(Long memberId);

    int countByMember(Member member);

    boolean existsByMemberAndRegion(Member member, Region region);

    Optional<MemberRegion> findByMemberAndRegion(Member member, Region region);

    List<MemberRegion> findAllByMember(Member member);

    Optional<MemberRegion> findByMemberAndIsMain(Member member, boolean isMain);
}
