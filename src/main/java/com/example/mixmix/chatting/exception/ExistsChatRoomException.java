package com.example.mixmix.chatting.exception;

import com.example.mixmix.global.error.exception.InvalidGroupException;

public class ExistsChatRoomException extends InvalidGroupException {
    public ExistsChatRoomException(String message) {
        super(message);
    }

    public ExistsChatRoomException() {
        this("채팅방이 존재하지 않습니다.");
    }
}