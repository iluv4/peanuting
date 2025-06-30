package com.example.mixmix.chatting.api.dto.request;

import com.example.mixmix.chatting.domain.ChatMessage;
import com.example.mixmix.chatting.domain.ChatRoom;
import java.time.LocalDateTime;

public record ChatMessageReqDto(
        String roomId,
        String sender,
        String content,
        LocalDateTime timestamp
) {
    public static ChatMessage toEntity(ChatRoom chatRoom, String sender, String content, LocalDateTime timestamp) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .timestamp(timestamp)
                .build();
    }
}

