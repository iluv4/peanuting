package com.example.mixmix.member.application;

import com.example.mixmix.member.api.dto.request.JoinMyPageUpdateReqDto;
import com.example.mixmix.member.api.dto.response.JoinMyPageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.ExistsNicknameException;
import com.example.mixmix.member.exception.MemberNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public JoinMyPageInfoResDto joinRequest(String email, JoinMyPageUpdateReqDto joinMyPageUpdateReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        if (isNicknameDuplicate(joinMyPageUpdateReqDto.nickname(), member.getId())) {
            throw new ExistsNicknameException();
        }

        member.update(email,
                joinMyPageUpdateReqDto.introduction(),
                joinMyPageUpdateReqDto.nationality(),
                joinMyPageUpdateReqDto.school(),
                joinMyPageUpdateReqDto.nickname());

        return JoinMyPageInfoResDto.from(member);
    }

    private boolean isNicknameDuplicate(String nickname, Long memberId) {
        return memberRepository.existsByNicknameAndNotId(normalizeNickname(nickname), memberId);
    }

    private String normalizeNickname(String nickname) {
        return nickname.replaceAll("\\s+", "");
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetStreakStatus() {
        LocalDateTime yesterday = LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        memberRepository.resetStreakUpdated(yesterday);
    }
}