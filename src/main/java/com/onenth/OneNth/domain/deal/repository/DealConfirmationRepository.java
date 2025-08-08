package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealConfirmationRepository extends JpaRepository<DealConfirmation, Long> {
    boolean existsByProductIdAndItemType(Long productId, ItemType itemType);
}
