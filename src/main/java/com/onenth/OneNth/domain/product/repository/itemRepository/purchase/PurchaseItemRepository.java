package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemRepository  extends JpaRepository<PurchaseItem, Long>, PurchaseItemRepositoryCustom {
}
