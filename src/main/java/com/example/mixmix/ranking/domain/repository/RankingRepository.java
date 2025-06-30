package com.example.mixmix.ranking.domain.repository;

import com.example.mixmix.member.domain.Member;
import com.example.mixmix.ranking.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long>, RankingCustomRepository {

    Ranking findByMember(Member member);

    List<Ranking> findTop10ByOrderByStreakRankDesc();
}