package com.onenth.OneNth.domain.chat.controller;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;
import com.onenth.OneNth.domain.chat.service.ChatCommandService;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "채팅 관련 API", description = "채팅 도메인 전반적인 API 지원")
public class ChatController {

    private final ChatCommandService chatCommandService;

    @Operation(
            summary = "채팅방 Name 조회 API",
            description = """
        WebSocket 채팅방 연결 시 사용하는 채팅방 이름(Name)을 반환합니다.
        - 해당 이름은 STOMP 구독 경로와 발행 경로에서 사용됩니다.
        - 동일한 사용자 간이라도 `chatRoomType`(DEAL, TIP_SHARE)이 다르면 서로 다른 채팅방으로 구분됩니다.
        - targetMemberId: 채팅 상대방의 사용자 ID
        - chatRoomType: 채팅방 타입
           - DEAL: N분의1 채팅
           - TIP_SHARE : 꿀팁 N분의1 채팅
        - 채팅방이 존재하지 않을 경우, 새로 생성 후 반환합니다.
        """
    )
    @PostMapping("/rooms")
    public ApiResponse<ChatResponseDTO.ChatRoomResponseDTO> getChatRoomName(
            @AuthUser Long memberId,
            @RequestParam("targetMemberId") Long targetMemberId,
            @RequestParam("chatRoomType")  ChatRoomType chatRoomType) {
        ChatResponseDTO.ChatRoomResponseDTO result
                = chatCommandService.getChatRoomName(memberId, targetMemberId, chatRoomType);
        return ApiResponse.onSuccess(result);
    }
}