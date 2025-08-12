package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.product.entity.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealConfirmationRepository extends JpaRepository<DealConfirmation, Long> {

    boolean existsByProductIdAndItemType(Long productId, ItemType itemType);
    List<DealConfirmation> findByBuyerAndSeller(Member buyer, Member seller);
}
