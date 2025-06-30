package com.example.mixmix.member.exception;

import com.example.mixmix.global.error.exception.InvalidGroupException;

public class ExistsNicknameException extends InvalidGroupException {
    public ExistsNicknameException(String message) {
        super(message);
    }

    public ExistsNicknameException() {
        this("이미 사용중인 닉네임 입니다.");
    }
}