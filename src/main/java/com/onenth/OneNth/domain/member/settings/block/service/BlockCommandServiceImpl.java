package com.onenth.OneNth.domain.member.settings.block.service;

import com.onenth.OneNth.domain.member.entity.Block;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.block.repository.BlockRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BlockCommandServiceImpl implements BlockCommandService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    @Override
    public Void unblockUser(Long userId, Long blockedUserId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Member blockedMember = memberRepository.findById(blockedUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BLOCK_TARGET_NOT_FOUND));

        Block block = blockRepository.findByMemberAndBlockedMember(member, blockedMember)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BLOCK_NOT_FOUND));

        blockRepository.delete(block);

        return null;
    }
}
