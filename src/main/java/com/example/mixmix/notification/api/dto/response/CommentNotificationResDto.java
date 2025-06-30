package com.example.mixmix.notification.api.dto.response;

import com.example.mixmix.notification.domain.Type;
import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public record CommentNotificationResDto(
        Long id,
        String message,
        Type type,
        Boolean isRead,
        LocalDateTime createdAt,
        Long feedId,
        String feedImage
) implements NotificationInfoResDto {

}
