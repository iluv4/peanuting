package com.example.mixmix.notification.domain;

import com.example.mixmix.global.entity.BaseEntity;
import com.example.mixmix.member.domain.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member receiver;

    private String message;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    private Boolean isRead;

    private Long relatedEntityId;

    @Builder
    public Notification(Member receiver, String message, Type type,Boolean isRead, Long relatedEntityId) {
        this.receiver = receiver;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.relatedEntityId = relatedEntityId;
    }

    public void markAsRead() {
        if (isRead) {
            return;
        }
        isRead = true;
    }
}