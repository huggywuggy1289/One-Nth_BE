package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.FcmToken;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMember(Member member);
}
