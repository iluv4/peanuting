package com.example.mixmix.chatting.domain.repository;

import com.example.mixmix.chatting.domain.ChatRoom;
import com.example.mixmix.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomCustomRepository {
    Page<ChatRoom> findChatRoomsByMember(Member member, Pageable pageable);
}
