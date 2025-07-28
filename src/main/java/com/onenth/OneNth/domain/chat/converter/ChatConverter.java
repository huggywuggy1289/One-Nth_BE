package com.onenth.OneNth.domain.chat.converter;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.ChatRoom;

public class ChatConverter {

    public static ChatResponseDTO.ChatRoomResponseDTO toChatRoomResponseDTO (ChatRoom chatRoom) {
        return ChatResponseDTO.ChatRoomResponseDTO.builder()
                .ChatRoomId(chatRoom.getId())
                .ChatRoomName(chatRoom.getName())
                .build();
    }

    public static ChatResponseDTO.ChatRoomPreviewDTO toChatRoomPreviewDTO (
            ChatRoom chatRoom, Long targetMemberId) {
        return ChatResponseDTO.ChatRoomPreviewDTO.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomType(chatRoom.getChatRoomType())
                .chatRoomName(chatRoom.getName())
                .opponentId(targetMemberId)
                .build();
    }
}