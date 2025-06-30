package com.example.mixmix.notification.api.dto.response;

import com.example.mixmix.notification.domain.Type;
import java.time.LocalDateTime;

public record MessageNotificationResDto(
        Long id,
        String message,
        Type type,
        Boolean isRead,
        LocalDateTime createdAt,
        Long chatRoomId,
        Long messageId,
        String messageContent,
        Integer messageNum
) implements NotificationInfoResDto {
}
