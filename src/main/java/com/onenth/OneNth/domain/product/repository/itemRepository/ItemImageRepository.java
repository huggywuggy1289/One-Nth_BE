package com.onenth.OneNth.domain.product.repository.itemRepository;

import com.onenth.OneNth.domain.product.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByPurchaseItemId(Long purchaseItemId);

    List<ItemImage> findBySharingItemId(Long sharingItemId);
}
