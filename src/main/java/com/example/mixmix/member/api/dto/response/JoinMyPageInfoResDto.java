package com.example.mixmix.member.api.dto.response;

import com.example.mixmix.member.domain.Member;
import lombok.Builder;

@Builder
public record JoinMyPageInfoResDto(
        String introduction,
        String nationality,
        String school,
        String nickName

) {
    public static JoinMyPageInfoResDto from(Member member) {
        return JoinMyPageInfoResDto.builder()
                .introduction(member.getIntroduction())
                .nationality(member.getNationality())
                .school(member.getSchool())
                .nickName(member.getNickname())
                .build();
    }
}