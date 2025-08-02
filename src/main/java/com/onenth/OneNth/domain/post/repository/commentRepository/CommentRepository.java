package com.onenth.OneNth.domain.post.repository.commentRepository;

import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<PostComment, Long> {

    long countByPost(Post post);

    List<PostComment> findByPostIdOrderByCreatedAtDesc(Long postId);

}
