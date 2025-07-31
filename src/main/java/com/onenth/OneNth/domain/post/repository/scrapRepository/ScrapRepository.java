package com.onenth.OneNth.domain.post.repository.scrapRepository;

import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    @Query("SELECT s FROM Scrap s " +
            "JOIN FETCH s.post p " +
            "JOIN FETCH p.region r " +
            "WHERE s.member.id = :memberId " +
            "ORDER BY s.createdAt DESC ")
    Page<Scrap> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    Optional<Scrap> findByMemberIdAndPostId(Long memberId, Long postId);
}
