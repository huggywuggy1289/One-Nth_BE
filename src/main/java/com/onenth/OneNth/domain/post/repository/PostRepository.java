package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    //마이페이지 - 내가 쓴 글 조회
    Page<Post> findByMemberId(Long memberId, Pageable pageable);

}
