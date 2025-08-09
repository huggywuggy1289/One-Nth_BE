package com.onenth.OneNth.domain.post.repository.tagRepository;

import com.onenth.OneNth.domain.post.entity.Post;
import com.onenth.OneNth.domain.post.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepositoryCustom extends JpaRepository<PostTag, Long> {
    void deleteAllByPost(Post post);
}
