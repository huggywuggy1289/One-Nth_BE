package com.onenth.OneNth.domain.member.settings.block.service;

import com.onenth.OneNth.domain.member.settings.block.dto.BlockResponseDTO;

public interface BlockQueryService {

    BlockResponseDTO.GetBlockedUsersResponseDTO getBlockedUsers(Long userId);
}
