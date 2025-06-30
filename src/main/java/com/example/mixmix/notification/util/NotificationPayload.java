package com.example.mixmix.notification.util;

import com.example.mixmix.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationPayload {
    private Type type;
    private String message;
}