package com.example.mixmix.chatting.api.dto.response;

import com.example.mixmix.global.dto.PageInfoResDto;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomResList(
        List<ChatRoomResDto> chatRoomResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static ChatRoomResList of(List<ChatRoomResDto> chatRoomResDtos, PageInfoResDto pageInfoResDto) {
        return ChatRoomResList.builder()
                .chatRoomResDtos(chatRoomResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}
