package com.example.mixmix.auth.api.dto.response;

import lombok.Builder;

@Builder
public record AccessAndRefreshTokenResDto(
        String accessToken,
        String refreshToken
) {
}
