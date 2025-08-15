package com.onenth.OneNth.domain.chat.controller;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;
import com.onenth.OneNth.domain.chat.service.ChatCommandService;
import com.onenth.OneNth.domain.chat.service.ChatQueryService;
import com.onenth.OneNth.domain.member.entity.enums.ReportType;
import com.onenth.OneNth.global.apiPayload.ApiResponse;
import com.onenth.OneNth.global.auth.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "채팅 관련 API", description = "채팅 도메인 전반적인 API 지원")
public class ChatController {

    private final ChatCommandService chatCommandService;
    private final ChatQueryService chatQueryService;

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

    @Operation(
            summary = "참여중인 채팅방 목록 조회 API",
            description = """
        현재 사용자가 참여 중인 채팅방 목록을 반환합니다.
        - chatRoomType: 조회하고자 하는 채팅방 리스트 유형
           - DEAL: N분의1 채팅
           - TIP_SHARE: 꿀팁 N분의1 채팅
        """
    )
    @GetMapping("/rooms")
    public ApiResponse<List<ChatResponseDTO.ChatRoomPreviewDTO>> getMyChatRoom(
            @AuthUser Long memberId,
            @RequestParam("chatRoomType")  ChatRoomType chatRoomType){
        List<ChatResponseDTO.ChatRoomPreviewDTO> result = chatQueryService.getMyChatRoomList(memberId, chatRoomType);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "채팅방 메시지 조회 API",
            description = """
        특정 채팅방의 메시지 내역을 조회합니다. 본인이 참여 중인 채팅방이 아니라면 조회가 불가능합니다.
        - chatRoomId: 조회하고자 하는 체팅방의 ID
        """
    )
    @GetMapping("{chatRoomId}/messages")
    public ApiResponse<List<ChatResponseDTO.ChatMessageDTO>> getMyChatMessage(
            @AuthUser Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId){
        List<ChatResponseDTO.ChatMessageDTO> result = chatQueryService.getMyChatMessageList(memberId, chatRoomId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "채팅방 나가기 API",
            description = """
        특정 채팅방에서 현재 사용자를 나가게합니다. 본인이 참여 중인 채팅방이 아니라면 권한이 없습니다.
        - chatRoomId: 나가고자 하는 체팅방의 ID
        """
    )
    @DeleteMapping("/{chatRoomId}/leave")
    public ApiResponse<String> leaveChatRoom(
            @AuthUser Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId) {
        chatCommandService.leaveChatRoom(memberId, chatRoomId);
        return ApiResponse.onSuccess("채팅방에서 나갔습니다.");
    }

    @Operation(
            summary = "사용자 신고 API",
            description = "현재 채팅방의 ID를 통해 상대방 사용자를 신고합니다."
    )
    @PostMapping("/reports/{chatRoomId}")
    public ApiResponse<String> reportMember(
            @AuthUser Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId,
            @RequestParam("reportType") ReportType reportType){
        chatCommandService.reportMember(memberId, chatRoomId, reportType);
        return ApiResponse.onSuccess("신고가 완료되었습니다.");
    }
}