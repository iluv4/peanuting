package com.example.mixmix.chatting.domain.repository;

import com.example.mixmix.chatting.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomCustomRepository {
}