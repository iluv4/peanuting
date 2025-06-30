package com.example.mixmix.member.exception;

import com.example.mixmix.global.error.exception.NotFoundGroupException;

public class MemberNotFoundException extends NotFoundGroupException {
    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException() {
        this("존재하지 않는 회원입니다");
    }
}