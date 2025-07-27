package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapRepository extends JpaRepository<Scrap, Long> {

    boolean existsByPostIdAndMemberId(Long postId, Long memberId);
}
