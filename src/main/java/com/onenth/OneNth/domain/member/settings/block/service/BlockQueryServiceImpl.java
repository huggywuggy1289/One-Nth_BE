package com.onenth.OneNth.domain.member.settings.block.service;

import com.onenth.OneNth.domain.member.entity.Block;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.domain.member.settings.block.converter.BlockConverter;
import com.onenth.OneNth.domain.member.settings.block.dto.BlockResponseDTO;
import com.onenth.OneNth.domain.member.settings.block.repository.BlockRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class BlockQueryServiceImpl implements BlockQueryService {

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;

    @Override
    public BlockResponseDTO.GetBlockedUsersResponseDTO getBlockedUsers(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Block> blockList = blockRepository.findAllByMember(member);

        List<BlockResponseDTO.BlockedUserSummary> blockedUserSummaryList = blockList.stream().map(
                block -> BlockConverter.toBlockedUserSummary(block)
        ).collect(Collectors.toList());

        return BlockConverter.toGetBlockedUsersResponseDTO(blockedUserSummaryList);
    }
}
