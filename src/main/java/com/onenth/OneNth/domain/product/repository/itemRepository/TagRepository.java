package com.onenth.OneNth.domain.product.repository.itemRepository;

import com.onenth.OneNth.domain.product.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
