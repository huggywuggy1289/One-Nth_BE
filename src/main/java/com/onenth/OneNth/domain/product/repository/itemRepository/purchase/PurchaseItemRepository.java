package com.onenth.OneNth.domain.product.repository.itemRepository.purchase;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseItemRepository  extends JpaRepository<PurchaseItem, Long>, PurchaseItemRepositoryCustom {
    List<PurchaseItem> findByMember(Member member);
}
