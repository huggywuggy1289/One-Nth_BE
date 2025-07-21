package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.KeywordAlert;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordAlertRepository extends JpaRepository<KeywordAlert, Long> {

    int countByMember(Member member);

    boolean existsByMemberAndKeyword(Member member, String keyword);

    Optional<KeywordAlert> findByIdAndMember(Long id, Member member);

    List<KeywordAlert> findAllByMember(Member member);
}
