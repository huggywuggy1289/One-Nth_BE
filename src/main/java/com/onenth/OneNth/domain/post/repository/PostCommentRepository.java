package com.onenth.OneNth.domain.post.repository;

import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository  extends JpaRepository<PostComment, Long> {

    long countByPost(Post post);

}
