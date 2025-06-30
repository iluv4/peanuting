package com.example.mixmix.comment.api.dto.request;

import com.example.mixmix.comment.domain.Comment;
import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.member.domain.Member;

public record CommentSaveReqDto(
        String contents,
        Long feedId
) {
    public Comment toEntity(Member member, Feed feed) {
        return Comment.builder()
                .contents(contents)
                .member(member)
                .feed(feed)
                .build();
    }
}