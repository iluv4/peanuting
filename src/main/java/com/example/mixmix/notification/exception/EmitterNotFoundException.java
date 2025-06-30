package com.example.mixmix.notification.exception;

import com.example.mixmix.global.error.exception.NotFoundGroupException;

public class EmitterNotFoundException extends NotFoundGroupException {
    public EmitterNotFoundException(String message) {
        super(message);
    }

    public EmitterNotFoundException() { this("해당 회원의 SSE 연결이 없습니다."); }
}