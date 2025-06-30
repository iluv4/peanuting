package com.example.mixmix.member.domain.repository;

import static com.example.mixmix.member.domain.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByNicknameAndNotId(String nickname, Long id) {
        Integer count = queryFactory
                .selectOne()
                .from(member)
                .where(member.nickname.eq(nickname)
                        .and(member.id.ne(id)))
                .fetchFirst();
        return count != null;
    }
}
