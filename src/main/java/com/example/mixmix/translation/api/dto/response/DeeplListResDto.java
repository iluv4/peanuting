package com.example.mixmix.translation.api.dto.response;

import java.util.List;

public record DeeplListResDto(
        List<DeeplInfoResDto> translations
) {
}
