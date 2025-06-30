package com.example.mixmix.feed.api.dto.request;

import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.FeedType;
import com.example.mixmix.member.domain.Member;
import java.util.List;

public record FeedSaveReqDto(
        String title,
        String contents,
        String hashTags,
        FeedType feedType
) {
    public Feed toEntity(Member member, List<String> imageUrls) {
        return Feed.builder()
                .title(title)
                .contents(contents)
                .hashTags(hashTags)
                .feedType(feedType)
                .member(member)
                .feedImage(String.join(",", imageUrls))
                .build();
    }
}