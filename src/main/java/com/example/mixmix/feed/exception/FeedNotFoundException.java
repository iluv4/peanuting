package com.example.mixmix.feed.exception;


import com.example.mixmix.global.error.exception.NotFoundGroupException;

public class FeedNotFoundException extends NotFoundGroupException {
    public FeedNotFoundException(String message) {
        super(message);
    }
    public FeedNotFoundException() {
        this("존재하지 않는 게시물입니다");
    }
}