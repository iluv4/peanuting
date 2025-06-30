package com.example.mixmix.ranking.api.dto.response;

import com.example.mixmix.ranking.domain.Ranking;

public record RankingInfoResDto(
        String email,
        Integer streakRank,
        String nationality,
        String profileImage,
        String name,
        Integer streak
) {
    public static RankingInfoResDto from(Ranking ranking) {
        return new RankingInfoResDto(
                ranking.getMember().getEmail(),
                ranking.getStreakRank(),
                ranking.getMember().getNationality(),
                ranking.getMember().getPicture(),
                ranking.getMember().getName(),
                ranking.getMember().getStreak()
        );
    }
}
