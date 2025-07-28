package com.onenth.OneNth.domain.member.settings.block.repository;

import com.onenth.OneNth.domain.member.entity.Block;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByMemberAndBlockedMember(Member member, Member blockedMember);
}
