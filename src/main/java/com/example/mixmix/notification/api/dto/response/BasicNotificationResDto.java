package com.example.mixmix.notification.api.dto.response;

import com.example.mixmix.notification.domain.Notification;
import com.example.mixmix.notification.domain.Type;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record BasicNotificationResDto(
        Long id,
        String message,
        Type type,
        Boolean isRead,
        LocalDateTime createdAt
) implements NotificationInfoResDto {
    public static BasicNotificationResDto from(Notification notification) {
        return BasicNotificationResDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
