package com.example.mixmix.translation.api.dto.response;

public record TranslateChatResDto(
        String language,
        String translated_message
) {
}
