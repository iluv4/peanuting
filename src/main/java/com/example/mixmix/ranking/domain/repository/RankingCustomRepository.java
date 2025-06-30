package com.example.mixmix.ranking.domain.repository;

import com.example.mixmix.ranking.domain.Ranking;

import java.util.List;

public interface RankingCustomRepository {
    List<Ranking> findAllRankingOrdered();
}