package com.example.mixmix.translation.api.dto.response;

public record DeeplInfoResDto(
        String detected_source_language,
        String text,
        String billed_characters,
        String model_type_used
) {
}
