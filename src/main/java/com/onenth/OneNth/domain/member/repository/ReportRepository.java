package com.onenth.OneNth.domain.member.repository;

import com.onenth.OneNth.domain.member.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}