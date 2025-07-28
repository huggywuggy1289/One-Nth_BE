package com.onenth.OneNth.domain.chat.repository;

import com.onenth.OneNth.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByName(String name);

    @Query("SELECT cr FROM ChatRoom cr " +
            "LEFT JOIN FETCH cr.chatMessages " +
            "WHERE cr.id = :id")
    Optional<ChatRoom> findWithChatMessagesById(@Param("id") Long id);
}
