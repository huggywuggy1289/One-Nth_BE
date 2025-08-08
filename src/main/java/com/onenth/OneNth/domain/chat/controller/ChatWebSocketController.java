package com.onenth.OneNth.domain.chat.controller;

import com.onenth.OneNth.domain.chat.service.ChatCommandService;
import com.onenth.OneNth.domain.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatCommandService chatCommandService;

    @MessageMapping("/chat-rooms/{roomName}")
    public ChatMessageDTO.MessageDTO chat(
            @DestinationVariable("roomName") String roomName,
            ChatMessageDTO.MessageDTO chatMessageDTO) {
        chatCommandService.createMessage(roomName, chatMessageDTO);
        return(chatMessageDTO);
    }
}