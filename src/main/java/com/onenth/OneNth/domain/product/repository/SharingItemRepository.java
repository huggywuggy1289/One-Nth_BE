package com.onenth.OneNth.domain.product.repository;

import com.onenth.OneNth.domain.product.entity.SharingItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SharingItemRepository extends JpaRepository<SharingItem, Long> {
    Optional<SharingItem> findById(Long id);
}