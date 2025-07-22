package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.ProductKeywordAlert;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordAlertRepository extends JpaRepository<ProductKeywordAlert, Long> {

    int countByMember(Member member);

    boolean existsByMemberAndKeyword(Member member, String keyword);

    Optional<ProductKeywordAlert> findByIdAndMember(Long id, Member member);

    List<ProductKeywordAlert> findAllByMember(Member member);
}
