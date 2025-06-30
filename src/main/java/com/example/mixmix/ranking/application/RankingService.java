package com.example.mixmix.ranking.application;

import com.example.mixmix.global.entity.Status;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import com.example.mixmix.ranking.api.dto.response.RankingInfoResDto;
import com.example.mixmix.ranking.api.dto.response.RankingListResDto;
import com.example.mixmix.ranking.domain.Ranking;
import com.example.mixmix.ranking.domain.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingService {

    private final RankingRepository rankingRepository;
    private final MemberRepository memberRepository;

    public RankingListResDto getRankingList() {
        List<RankingInfoResDto> rankingTop10List = rankingRepository.findTop10ByOrderByStreakRankDesc()
                .stream()
                .map(RankingInfoResDto::from)
                .toList();

        return RankingListResDto.from(rankingTop10List);
    }

    public RankingInfoResDto getRanking(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Ranking ranking = rankingRepository.findByMember(member);
        return RankingInfoResDto.from(ranking);
    }

    @Transactional
    public void createRanking(Member member) {
        Ranking ranking = Ranking.builder()
                .member(member)
                .streakRank(null)
                .status(Status.ACTIVE)
                .build();

        rankingRepository.save(ranking);
    }

    @Transactional
    @Scheduled(fixedRate = 300000)
    public void updateRankings() {
        List<Ranking> sortedRankingList = rankingRepository.findAllRankingOrdered();

        int rank = 1;
        for (Ranking ranking : sortedRankingList) {
            ranking.updateRank(rank++);
        }

        rankingRepository.saveAll(sortedRankingList);
    }
}
