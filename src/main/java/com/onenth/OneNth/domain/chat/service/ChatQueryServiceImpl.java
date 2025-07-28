package com.onenth.OneNth.domain.chat.service;

import com.onenth.OneNth.domain.chat.dto.ChatResponseDTO;
import com.onenth.OneNth.domain.chat.entity.ChatMessage;
import com.onenth.OneNth.domain.chat.entity.ChatRoomMember;
import com.onenth.OneNth.domain.chat.entity.enums.ChatRoomType;
import com.onenth.OneNth.domain.member.entity.Member;
import com.onenth.OneNth.domain.member.repository.memberRepository.MemberRepository;
import com.onenth.OneNth.global.apiPayload.code.status.ErrorStatus;
import com.onenth.OneNth.global.apiPayload.exception.handler.MemberHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static com.onenth.OneNth.domain.chat.converter.ChatConverter.toChatRoomPreviewDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {

    private final MemberRepository memberRepository;

    @Override
    public List<ChatResponseDTO.ChatRoomPreviewDTO> getMyChatRoomList(Long memberId, ChatRoomType chatRoomType) {
        Member member = memberRepository.findWithChatRoomsById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return member.getChatRoomMembers().stream()
                .map(ChatRoomMember::getChatRoom)
                .filter(chatRoom -> chatRoom.getChatRoomType().equals(chatRoomType))
                .map(chatRoom -> {
                    ChatMessage lastMessage = chatRoom.getChatMessages().stream()
                            .max(Comparator.comparing(ChatMessage::getCreatedAt))
                            .orElse(null);

                    Member opponent = chatRoom.getChatRoomMembers().stream()
                            .map(ChatRoomMember::getMember)
                            .filter(m -> !m.getId().equals(memberId))
                            .findFirst()
                            .orElse(null);

                    ChatResponseDTO.ChatRoomPreviewDTO dto = toChatRoomPreviewDTO(chatRoom,opponent.getId());

                    if (lastMessage != null) {
                        dto.setLastMessageContent(lastMessage.getContent());
                        dto.setLastMessageTime(lastMessage.getCreatedAt());
                    }

                    return dto;
                })
                .toList();
    }
}
