package com.example.mixmix.member.mypage.api.dto.response;

import com.example.mixmix.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record MyPageInfoResDto(
        String introduction,
        String nationality,
        String school,
        String nickName,
        String picture,
        String name,
        String streak,
        Integer streakRank,
        long studyCount,
        long socialCount,
        LocalDateTime lastStreakUpdate,
        boolean isStreakUpdated,
        boolean unreadNotification
) {
    public static MyPageInfoResDto from(Member member, long studyCount, long socialCount, boolean unreadNotification) {
        return MyPageInfoResDto.builder()
                .introduction(member.getIntroduction())
                .nationality(member.getNationality())
                .school(member.getSchool())
                .nickName(member.getNickname())
                .picture(member.getPicture())
                .name(member.getName())
                .streak(String.valueOf(member.getStreak()))
                .streakRank(member.getRanking().getStreakRank())
                .studyCount(studyCount)
                .socialCount(socialCount)
                .lastStreakUpdate(member.getLastStreakUpdate())
                .isStreakUpdated(member.isStreakUpdated())
                .unreadNotification(unreadNotification)
                .build();
    }
}