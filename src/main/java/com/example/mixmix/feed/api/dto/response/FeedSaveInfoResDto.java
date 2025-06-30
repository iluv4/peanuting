package com.example.mixmix.feed.api.dto.response;

import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.FeedType;
import lombok.Builder;

@Builder
public record FeedSaveInfoResDto(
        String feedImage,
        String title,
        String contents,
        String hashTags,
        FeedType feedType,
        Long memberId
) {
    public static FeedSaveInfoResDto of(Feed feed, Long memberId) {
        return FeedSaveInfoResDto.builder()
                .feedImage(feed.getFeedImage())
                .title(feed.getTitle())
                .contents(feed.getContents())
                .hashTags(feed.getHashTags())
                .feedType(feed.getFeedType())
                .memberId(memberId)
                .build();
    }
}