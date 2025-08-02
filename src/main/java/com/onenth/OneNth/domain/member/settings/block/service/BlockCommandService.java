package com.onenth.OneNth.domain.member.settings.block.service;

public interface BlockCommandService {

    Void unblockUser(Long userId, Long blockedUserId);
}
