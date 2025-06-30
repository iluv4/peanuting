package com.example.mixmix.member.domain;

import com.example.mixmix.global.entity.BaseEntity;
import com.example.mixmix.global.entity.Status;
import com.example.mixmix.ranking.domain.Ranking;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Status status;

    private String email;

    private String name;

    private String nickname;

    private String picture;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String introduction = "";

    private String nationality= "";

    private String school= "";

    private Integer streak = 0;

    private boolean isStreakUpdated = false;

    private LocalDateTime lastStreakUpdate;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Ranking ranking;

    @Enumerated(value = EnumType.STRING)
    private SolvedStatus hasSolved;

    @Builder
    private Member(Status status,
                   String email, String name,
                   String picture,
                   SocialType socialType,
                   String introduction
    ) {
        this.status = status;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.socialType = socialType;
        this.introduction = introduction;
        this.nickname = email.split("@")[0];
    }

    public void update(String email, String introduction, String nationality, String school, String nickname) {
        if (isUpdateRequired(email, introduction, nationality, school, nickname)) {
            this.introduction = introduction;
            this.nationality = nationality;
            this.school = school;
            this.nickname = nickname;
        }
    }

    private boolean isUpdateRequired(String email, String updateIntroduction, String nationality, String school, String nickname) {
        return !this.email.equals(email) ||
                !this.introduction.equals(updateIntroduction) ||
                !this.nationality.equals(nationality) ||
                !this.school.equals(school) ||
                !this.nickname.equals(nickname);
    }

    public void incrementStreak() {
        LocalDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        if (this.lastStreakUpdate == null || !this.lastStreakUpdate.toLocalDate().isEqual(now.toLocalDate())) {
            this.streak++;
            this.lastStreakUpdate = now;
            this.isStreakUpdated = true;
        }
    }
}