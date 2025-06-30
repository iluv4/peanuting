package com.example.mixmix.feed.api.dto.response;

import com.example.mixmix.global.dto.PageInfoResDto;
import java.util.List;
import lombok.Builder;


@Builder
public record FeedListResDto(
        List<FeedInfoResDto> feedListResDto,
        PageInfoResDto pageInfoResDto
) {
    public static FeedListResDto of(List<FeedInfoResDto> feedListResDtos, PageInfoResDto pageInfoResDto) {
        return FeedListResDto.builder()
                .feedListResDto(feedListResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}