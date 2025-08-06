package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.SharingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemRepository  extends JpaRepository<PurchaseItem, Long>, PurchaseItemRepositoryCustom {
    List<PurchaseItem> findByMember(Member member);

    // 상품단건조회
    @Query("""
    SELECT p
    FROM PurchaseItem p
    JOIN FETCH p.region
    WHERE p.id = :id
    """)
    Optional<PurchaseItem> findWithRegionById(@Param("id") Long id);
}
