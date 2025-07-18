package com.onenth.OneNth.domain.member.repository.memberRepository;

import com.onenth.OneNth.domain.member.entity.MemberRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRegionRepository extends JpaRepository<MemberRegion, Long> {
    List<MemberRegion> findByMemberId(Long memberId);
}
