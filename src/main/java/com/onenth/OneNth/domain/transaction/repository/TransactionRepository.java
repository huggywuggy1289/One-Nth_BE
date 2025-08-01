package com.onenth.OneNth.domain.transaction.repository;

import com.onenth.OneNth.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}