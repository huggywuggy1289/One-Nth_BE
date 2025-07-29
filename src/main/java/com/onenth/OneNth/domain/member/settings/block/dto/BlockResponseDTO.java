package com.onenth.OneNth.domain.member.settings.block.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class BlockResponseDTO {

    @Getter
    @Builder
    public static class GetBlockedUsersResponseDTO {
        List<BlockedUserSummary> blockedUserSummaryList;
    }

    @Getter
    @Builder
    public static class BlockedUserSummary {
        private Long userId;
        private String profileImageUrl;
        private String nickname;
    }
}
