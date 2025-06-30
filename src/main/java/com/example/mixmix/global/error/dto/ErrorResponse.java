package com.example.mixmix.global.error.dto;

public record ErrorResponse(
        int statusCode,
        String message
) {
}