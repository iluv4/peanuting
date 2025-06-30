package com.example.mixmix.auth.application;

import com.example.mixmix.auth.api.dto.response.MemberLoginResDto;
import com.example.mixmix.auth.api.dto.response.UserInfo;
import com.example.mixmix.auth.exception.EmailNotFoundException;
import com.example.mixmix.auth.exception.ExistsMemberEmailException;
import com.example.mixmix.global.entity.Status;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.SocialType;
import com.example.mixmix.member.domain.repository.MemberRepository;
import java.util.Optional;

import com.example.mixmix.ranking.application.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthMemberService {

    private final MemberRepository memberRepository;
    private final RankingService rankingService;

    @Transactional
    public MemberLoginResDto saveUserInfo(UserInfo userInfo, SocialType provider) {
        validateNotFoundEmail(userInfo.email());

        Member member = getExistingMemberOrCreateNew(userInfo, provider);

        validateSocialType(member, provider);

        return MemberLoginResDto.from(member);
    }

    private void validateNotFoundEmail(String email) {
        if (email == null) {
            throw new EmailNotFoundException();
        }
    }

    private Member getExistingMemberOrCreateNew(UserInfo userInfo, SocialType provider) {
        return memberRepository.findByEmail(userInfo.email()).orElseGet(() -> createMember(userInfo, provider));
        // email로 확인 : email 값이 있다면 get, 없으면 create
    }

    private Member createMember(UserInfo userInfo, SocialType provider) {
        String userPicture = getUserPicture(userInfo.picture());
        String name = unionName(userInfo.name(), userInfo.nickname());

        Member member = Member.builder()
                .status(Status.ACTIVE)
                .email(userInfo.email())
                .name(name)
                .picture(userPicture)
                .socialType(provider)
                .introduction("")
                .build();

        rankingService.createRanking(member);
        return memberRepository.save(member);
    }

    private String unionName(String name, String nickname) {
        return nickname != null ? nickname : name;
    }
    
    private String getUserPicture(String picture) {
        return Optional.ofNullable(picture)
                .map(this::convertToHighRes)
                .orElseThrow();
    }

    private String convertToHighRes(String url){
        return url.replace("s96-c", "s2048-c");
    }

    private void validateSocialType(Member member, SocialType provider) {
        if (!provider.equals(member.getSocialType())) {
            throw new ExistsMemberEmailException();
        }
    }
}
