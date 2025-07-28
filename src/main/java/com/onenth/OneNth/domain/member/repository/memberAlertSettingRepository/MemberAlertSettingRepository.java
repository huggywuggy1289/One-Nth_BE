package com.onenth.OneNth.domain.member.repository.memberAlertSettingRepository;

import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.entity.MemberAlertSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAlertSettingRepository extends JpaRepository<MemberAlertSetting, Long> {
}
