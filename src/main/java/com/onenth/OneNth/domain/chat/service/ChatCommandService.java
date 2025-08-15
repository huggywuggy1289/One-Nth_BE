package com.onenth.OneNth.domain.chat.service;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;
import com.onenth.OneNth.domain.chat.dto.ChatMessageDTO;
import com.onenth.OneNth.domain.member.entity.enums.ReportType;

public interface ChatCommandService {

    ChatResponseDTO.ChatRoomResponseDTO getChatRoomName(Long userId, Long targetUserId, ChatRoomType chatRoomType);
    void leaveChatRoom(Long memberId, Long chatRoomId);
    void createMessage(String roomName, ChatMessageDTO.MessageDTO chatMessageDTO);
    void reportMember(Long memberId, Long chatRoomId, ReportType reportType);
}