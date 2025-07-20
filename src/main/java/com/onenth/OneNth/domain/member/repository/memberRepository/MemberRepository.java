package com.onenth.OneNth.domain.member.repository.memberRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Optional<Member> findBySocialIdAndLoginType(String socialId, LoginType loginType);

    boolean existsBySocialIdAndLoginType(String socialId, LoginType loginType);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);
}
