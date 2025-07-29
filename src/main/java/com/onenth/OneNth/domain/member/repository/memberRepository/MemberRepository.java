package com.onenth.OneNth.domain.member.repository.memberRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.enums.LoginType;
import com.onenth.OneNth.domain.member.entity.enums.MemberStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndStatus(String email, MemberStatus status);

    Optional<Member> findBySocialIdAndLoginType(String socialId, LoginType loginType);

    boolean existsBySocialIdAndLoginType(String socialId, LoginType loginType);

    boolean existsByEmailAndLoginType(String email, LoginType loginType);

    Optional<Member> findByEmailAndName(String email, String name);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberRegions WHERE m.id = :id")
    Optional<Member> findByIdWithRegions(@Param("id") Long id);

    @EntityGraph(attributePaths = {"purchaseReviews"})
    Optional<Member> findWithPurchaseReviewsById(Long id);

    @EntityGraph(attributePaths = {"sharingReviews"})
    Optional<Member> findWithSharingReviewsById(Long id);
}