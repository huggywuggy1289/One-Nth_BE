package com.onenth.OneNth.domain.post.repository.likeRepository;

import com.onenth.OneNth.domain.post.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query("SELECT l FROM Like l " +
            "JOIN FETCH l.post p " +
            "JOIN FETCH p.region r " +
            "WHERE l.member.id = :memberId " +
            "ORDER BY l.createdAt DESC ")
    Page<Like> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
