package com.onenth.OneNth.domain.product.repository.itemRepository;

import com.onenth.OneNth.domain.product.entity.ItemImage;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByPurchaseItemId(Long purchaseItemId);

    List<ItemImage> findBySharingItemId(Long sharingItemId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ItemImage i where i.purchaseItem.id = :itemId")
    int deleteByPurchaseItemId(@Param("itemId") Long itemId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from SharingItemScrap s where s.sharingItem.id = :itemId")
    int deleteBySharingItemId(@Param("itemId") Long itemId);

    // 이미지 여러장 한꺼번에 get
    List<ItemImage> findByPurchaseItemIdInAndItemType(
            Collection<Long> purchaseItemIds,
            ItemType itemType
    );

    List<ItemImage> findBySharingItemIdInAndItemType(Collection<Long> ids, ItemType itemType);
}
