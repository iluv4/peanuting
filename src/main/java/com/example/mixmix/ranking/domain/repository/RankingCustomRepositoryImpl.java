package com.example.mixmix.ranking.domain.repository;

import com.example.mixmix.global.entity.Status;
import com.example.mixmix.member.domain.QMember;
import com.example.mixmix.ranking.domain.QRanking;
import com.example.mixmix.ranking.domain.Ranking;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankingCustomRepositoryImpl implements RankingCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Ranking> findAllRankingOrdered() {
        QRanking ranking = QRanking.ranking;
        QMember member = QMember.member;

        return queryFactory.selectFrom(ranking)
                .join(ranking.member, member)
                .fetchJoin()
                .where(member.status.eq(Status.ACTIVE))
                .orderBy(
                        member.streak.desc(),
                        member.lastStreakUpdate.asc()
                )
                .fetch();
    }
}