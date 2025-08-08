package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.CancelledDeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelledDealRepository extends JpaRepository<CancelledDeal, Long> {
}