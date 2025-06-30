package com.example.mixmix.chatting.api.dto.response;

import com.example.mixmix.global.dto.PageInfoResDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatMessageResList(
        List<ChatMessageResDto> chatMessages,
        PageInfoResDto pageInfo
) {
    public static ChatMessageResList of(List<ChatMessageResDto> messages, PageInfoResDto pageInfo) {
        return ChatMessageResList.builder()
                .chatMessages(messages)
                .pageInfo(pageInfo)
                .build();
    }
}
