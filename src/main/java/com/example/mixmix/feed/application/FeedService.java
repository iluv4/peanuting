package com.example.mixmix.feed.application;

import com.example.mixmix.feed.api.dto.request.FeedSaveReqDto;
import com.example.mixmix.feed.api.dto.response.FeedInfoResDto;
import com.example.mixmix.feed.api.dto.response.FeedListResDto;
import com.example.mixmix.feed.api.dto.response.FeedSaveInfoResDto;
import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.repository.FeedRepository;
import com.example.mixmix.feed.exception.FeedNotFoundException;
import com.example.mixmix.global.dto.PageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import com.example.mixmix.s3.application.AwsS3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final AwsS3Service awsS3Service;

    // 피드 생성
    @Transactional
    public FeedSaveInfoResDto save(String email, FeedSaveReqDto feedSaveReqDto, List<String> imageUrls) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        member.incrementStreak();

        Feed feed = feedRepository.save(feedSaveReqDto.toEntity(member, imageUrls));

        return FeedSaveInfoResDto.of(feed, member.getId());
    }

    // 피드 개별 조회
    public FeedInfoResDto findById(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        String imageUrl = awsS3Service.getFileUrl(feed.getFeedImage());

        return FeedInfoResDto.of(feed, imageUrl);
    }

    // 피드 social/study 구분해서 전체 조회
    public FeedListResDto findAllByFeedType(String keyword, String nationality, Pageable pageable) {
        Page<FeedInfoResDto> feedPage = feedRepository.findAllByFeedType(keyword, nationality, pageable);

        return FeedListResDto.of(
                feedPage.getContent(),
                PageInfoResDto.from(feedPage)
        );
    }

    // 게시물 수정
    @Transactional
    public FeedInfoResDto update(Long feedId, FeedSaveReqDto feedSaveReqDto) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);
        String imageUrl = awsS3Service.getFileUrl(feed.getFeedImage());

        feed.update(feedSaveReqDto.title(),
                feedSaveReqDto.contents(),
                feedSaveReqDto.hashTags());

        return FeedInfoResDto.of(feed, imageUrl);
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(FeedNotFoundException::new);

        feed.delete();
    }
}