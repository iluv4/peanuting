package com.example.mixmix.feed.domain.repository;

import com.example.mixmix.feed.api.dto.response.FeedInfoResDto;
import com.example.mixmix.feed.domain.FeedType;
import com.example.mixmix.feed.domain.QFeed;
import com.example.mixmix.global.entity.Status;
import com.example.mixmix.s3.application.AwsS3Service;
import com.example.mixmix.s3.util.S3Util;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedCustomRepositoryImpl implements FeedCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final S3Util s3Util;

    @Override
    public Page<FeedInfoResDto> findAllByFeedType(String keyword, String nationality, Pageable pageable) {
        QFeed feed = QFeed.feed;

        FeedType feedType = FeedType.valueOf(keyword);

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(feed.feedType.eq(feedType))
                .and(feed.status.eq(Status.ACTIVE));

        if (nationality != null && !nationality.isEmpty()) {
            condition.and(feed.member.nationality.eq(nationality));
        }

        List<FeedInfoResDto> content = queryFactory
                .select(Projections.constructor(
                        FeedInfoResDto.class,
                        feed.feedImage,
                        feed.title,
                        feed.contents,
                        feed.hashTags,
                        feed.feedType,
                        feed.member.id,
                        feed.id,
                        feed.createdAt,
                        feed.member.name,
                        feed.member.picture
                ))
                .from(feed)
                .where(condition)
                .orderBy(feed.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<FeedInfoResDto> parsedContent = content.stream()
                .map(feedInfoResDto -> FeedInfoResDto.builder()
                        .feedImage(s3Util.getFileUrl(feedInfoResDto.feedImage()))
                        .title(feedInfoResDto.title())
                        .contents(feedInfoResDto.contents())
                        .hashTags(feedInfoResDto.hashTags())
                        .feedType(feedInfoResDto.feedType())
                        .memberId(feedInfoResDto.memberId())
                        .feedId(feedInfoResDto.feedId())
                        .createdAt(feedInfoResDto.createdAt())
                        .memberName(feedInfoResDto.memberName())
                        .memberImage(feedInfoResDto.memberImage())
                        .build()
                )
                .toList();

        long total = queryFactory
                .select(feed.count())
                .from(feed)
                .where(condition)
                .fetchOne();

        return PageableExecutionUtils.getPage(parsedContent, pageable, () -> total);
    }


    @Override
    public long countStudyFeedsByMemberId(Long memberId) {
        QFeed feed = QFeed.feed;

        return queryFactory
                .select(feed.count())
                .from(feed)
                .where(
                        feed.member.id.eq(memberId)
                                .and(feed.feedType.eq(FeedType.STUDY))
                                .and(feed.status.eq(com.example.mixmix.global.entity.Status.ACTIVE))
                )
                .fetchOne();
    }

    @Override
    public long countSocialFeedsByMemberId(Long memberId) {
        QFeed feed = QFeed.feed;

        return queryFactory
                .select(feed.count())
                .from(feed)
                .where(
                        feed.member.id.eq(memberId)
                                .and(feed.feedType.eq(FeedType.SOCIAL))
                                .and(feed.status.eq(com.example.mixmix.global.entity.Status.ACTIVE))
                )
                .fetchOne();
    }
}