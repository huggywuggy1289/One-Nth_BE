package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealCompletionRepository extends JpaRepository<DealCompletion, Long> {
    boolean existsByDealConfirmation (DealConfirmation dealConfirmation);

    @EntityGraph(attributePaths = {"dealConfirmation"})
    List<DealCompletion> findBySellerOrBuyer (Member seller, Member buyer);
}
