package com.example.mixmix.feed.api.dto.response;

import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.FeedType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record FeedInfoResDto(
        String feedImage,
        String title,
        String contents,
        String hashTags,
        FeedType feedType,
        Long memberId,
        Long feedId,
        LocalDateTime createdAt,
        String memberName,
        String memberImage
) {
    public static FeedInfoResDto of(Feed feed, String feedImage) {

        return FeedInfoResDto.builder()
                .feedImage(feedImage)
                .title(feed.getTitle())
                .contents(feed.getContents())
                .hashTags(feed.getHashTags())
                .feedType(feed.getFeedType())
                .memberId(feed.getMember().getId())
                .feedId(feed.getId())
                .createdAt(feed.getCreatedAt())
                .memberName(feed.getMember().getName())
                .memberImage(feed.getMember().getPicture())
                .build();
    }
}