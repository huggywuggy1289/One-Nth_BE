package com.onenth.OneNth.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomResponseDTO{
        Long ChatRoomId;
        String ChatRoomName;
    }
}
