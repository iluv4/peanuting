package com.example.mixmix.chatting.api.dto.request;

public record ChatRoomReqDto(
        String roomName,
        Long toMemberId
) {
}
