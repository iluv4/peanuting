package com.example.mixmix.member.mypage.application;

import com.example.mixmix.feed.domain.repository.FeedRepository;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import com.example.mixmix.member.mypage.api.dto.response.MyPageInfoResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;

    public MyPageInfoResDto findMyProfileByEmail(String email, Boolean unreadNotification) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        long educationCount = feedRepository.countStudyFeedsByMemberId(member.getId());
        long socialCount = feedRepository.countSocialFeedsByMemberId(member.getId());

        return MyPageInfoResDto.from(member, educationCount, socialCount, unreadNotification);
    }

    public MyPageInfoResDto findMyProfileById(Long memberId, Boolean unreadNotification) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        long educationCount = feedRepository.countStudyFeedsByMemberId(member.getId());
        long socialCount = feedRepository.countSocialFeedsByMemberId(member.getId());

        return MyPageInfoResDto.from(member, educationCount, socialCount, unreadNotification);
    }
}