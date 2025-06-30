package com.example.mixmix.member.domain.repository;

public interface MemberCustomRepository {
    boolean existsByNicknameAndNotId(String nickname, Long id);
}