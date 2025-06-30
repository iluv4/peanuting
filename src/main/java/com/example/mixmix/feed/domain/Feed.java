package com.example.mixmix.feed.domain;

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
public class Feed extends BaseEntity {

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private String title;

    private String contents;

    private String hashTags;

    private String feedImage;

    @Enumerated(value = EnumType.STRING)
    private FeedType feedType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Feed(String title, String contents, String hashTags, String feedImage, FeedType feedType, Member member) {
        this.feedImage = feedImage;
        this.title = title;
        this.contents = contents;
        this.hashTags = hashTags;
        this.status = Status.ACTIVE;
        this.feedType = feedType;
        this.member = member;
    }

    public void update(String title, String contents, String hashTag) {
        this.title = title;
        this.contents = contents;
        this.hashTags = hashTag;
    }

    public void delete() {
        this.status = Status.UN_ACTIVE;
    }
}