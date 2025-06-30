package com.example.mixmix.chatting.api.dto.response;

import com.example.mixmix.chatting.domain.ChatMessage;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageResDto(
        String sender,
        String content,
        LocalDateTime timestamp
) {
    public static ChatMessageResDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageResDto.builder()
                .sender(chatMessage.getSender())
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getTimestamp())
                .build();
    }
}