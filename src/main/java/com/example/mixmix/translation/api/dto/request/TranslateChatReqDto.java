package com.example.mixmix.translation.api.dto.request;

public record TranslateChatReqDto(
        String targetLang,
        String text
) {
}
