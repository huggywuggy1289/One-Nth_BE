package com.onenth.OneNth.domain.product.repository.scrapRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import com.onenth.OneNth.domain.product.entity.scrap.PurchaseItemScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseItemScrapRepository extends JpaRepository<PurchaseItemScrap, Long> {

    boolean existsByMemberAndPurchaseItem(Member member, PurchaseItem item);

    @Query("SELECT s FROM PurchaseItemScrap s WHERE s.member.id = :userId")
    List<PurchaseItemScrap> findByUserId(@Param("userId") Long userId);
}