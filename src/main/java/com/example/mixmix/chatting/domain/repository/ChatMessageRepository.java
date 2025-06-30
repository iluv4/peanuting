package com.example.mixmix.chatting.domain.repository;

import com.example.mixmix.chatting.domain.ChatMessage;
import com.example.mixmix.chatting.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoom(ChatRoom chatRoom, Pageable pageable);

    Optional<ChatMessage> findFirstByChatRoomOrderByTimestampDesc(ChatRoom chatRoom);
}