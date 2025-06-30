package com.example.mixmix.notification.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record NotificationListResDto(
        List<? extends NotificationInfoResDto> notificationInfoResDtos
) {
    public static NotificationListResDto of(List<? extends NotificationInfoResDto> notificationInfoResDtoList) {
        return NotificationListResDto.builder()
                .notificationInfoResDtos(notificationInfoResDtoList).build();
    }
}
