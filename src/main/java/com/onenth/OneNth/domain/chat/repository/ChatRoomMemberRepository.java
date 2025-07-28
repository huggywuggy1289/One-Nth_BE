package com.onenth.OneNth.domain.chat.repository;

import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import com.onenth.OneNth.domain.chat.entity.ChatRoomMember;
import com.onenth.OneNth.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    Optional<ChatRoomMember> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}
