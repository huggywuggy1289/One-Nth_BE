package com.onenth.OneNth.domain.member.settings.alert.generalAlert.repository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberAlertSettingRepository extends JpaRepository<MemberAlertSetting, Long> {
    Optional<MemberAlertSetting> findByMember(Member member);
}
