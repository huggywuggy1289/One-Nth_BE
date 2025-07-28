package com.onenth.OneNth.domain.chat.service;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;

import java.util.List;

public interface ChatQueryService {
    List<ChatResponseDTO.ChatRoomPreviewDTO> getMyChatRoomList (Long userId, ChatRoomType chatRoomType);
}
