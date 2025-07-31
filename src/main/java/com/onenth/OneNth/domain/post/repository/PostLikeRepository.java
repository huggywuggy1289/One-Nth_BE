package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.post.entity.Like;
import com.onenth.OneNth.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<Like, Long> {

    long countByPost(Post post);
}
