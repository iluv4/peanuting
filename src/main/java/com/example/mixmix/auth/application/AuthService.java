package com.example.mixmix.auth.application;


import com.example.mixmix.auth.api.dto.response.IdTokenResDto;
import com.example.mixmix.auth.api.dto.response.UserInfo;

public interface AuthService {
    UserInfo getUserInfo(String authCode);

    String getProvider();

    IdTokenResDto getIdToken(String code);
}
