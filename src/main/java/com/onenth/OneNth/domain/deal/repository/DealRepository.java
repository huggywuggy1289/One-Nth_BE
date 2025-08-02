package com.onenth.OneNth.domain.deal.repository;

import com.onenth.OneNth.domain.deal.entity.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}