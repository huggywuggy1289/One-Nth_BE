package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
