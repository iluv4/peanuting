package com.example.mixmix.comment.domain;

import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.global.entity.BaseEntity;
import com.example.mixmix.global.entity.Status;
import com.example.mixmix.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Comment(String contents, Member member, Feed feed) {
        this.status = Status.ACTIVE;
        this.contents = contents;
        this.member = member;
        this.feed = feed;
    }
}