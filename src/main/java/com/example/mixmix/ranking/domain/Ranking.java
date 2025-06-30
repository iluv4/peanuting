package com.example.mixmix.ranking.domain;

import com.example.mixmix.global.entity.BaseEntity;
import com.example.mixmix.global.entity.Status;
import com.example.mixmix.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Ranking extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    private Integer streakRank;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Ranking(Status status, Integer streakRank, Member member) {
        this.status = status;
        this.streakRank = streakRank;
        this.member = member;
    }

    public void updateRank(Integer newRank) {
        this.streakRank = newRank;
    }
}