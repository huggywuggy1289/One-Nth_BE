package com.onenth.OneNth.domain.member.repository.memberRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
}
