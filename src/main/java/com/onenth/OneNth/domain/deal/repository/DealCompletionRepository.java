package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.DealCompletion;
import com.onenth.OneNth.domain.deal.entity.DealConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealCompletionRepository extends JpaRepository<DealCompletion, Long> {
    boolean existsByDealConfirmation (DealConfirmation dealConfirmation);
}
