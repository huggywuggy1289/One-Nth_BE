package com.onenth.OneNth.domain.member.settings.block.converter;

import com.onenth.OneNth.domain.member.entity.Block;
import com.onenth.OneNth.domain.member.settings.block.dto.BlockResponseDTO;

import java.util.List;

public class BlockConverter {

    public static BlockResponseDTO.GetBlockedUsersResponseDTO toGetBlockedUsersResponseDTO(List<BlockResponseDTO.BlockedUserSummary> blockedUserSummaryList) {
        return BlockResponseDTO.GetBlockedUsersResponseDTO.builder()
                .blockedUserSummaryList(blockedUserSummaryList)
                .build();
    }

    public static BlockResponseDTO.BlockedUserSummary toBlockedUserSummary(Block block) {
        return BlockResponseDTO.BlockedUserSummary.builder()
                .userId(block.getBlockedMember().getId())
                .profileImageUrl(block.getBlockedMember().getProfileImageUrl())
                .nickname(block.getBlockedMember().getNickname())
                .build();
    }
}
