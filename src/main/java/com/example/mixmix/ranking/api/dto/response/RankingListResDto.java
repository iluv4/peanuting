package com.example.mixmix.ranking.api.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record RankingListResDto(
        List<RankingInfoResDto> rankingInfoResDto
) {
    public static RankingListResDto from(List<RankingInfoResDto> rankingInfoResDtoList) {
        return RankingListResDto.builder()
                .rankingInfoResDto(rankingInfoResDtoList).build();
    }
}
