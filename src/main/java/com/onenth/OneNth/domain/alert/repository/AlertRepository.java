package com.onenth.OneNth.domain.alert.repository;

import com.onenth.OneNth.domain.alert.entity.Alert;
import com.onenth.OneNth.domain.alert.entity.AlertType;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findByMemberAndAlertTypeInAndCreatedAtAfter(Member member, List<AlertType> alertTypes, LocalDateTime since);
}
