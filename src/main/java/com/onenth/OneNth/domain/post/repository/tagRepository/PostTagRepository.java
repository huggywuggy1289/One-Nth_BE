package com.onenth.OneNth.domain.post.repository.tagRepository;

import com.onenth.OneNth.domain.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostTagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

}
