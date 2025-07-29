package com.onenth.OneNth.domain.chat.service;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;

public interface ChatCommandService {

    ChatResponseDTO.ChatRoomResponseDTO getChatRoomName(Long userId, Long targetUserId, ChatRoomType chatRoomType);
    void leaveChatRoom(Long memberId, Long chatRoomId);
}