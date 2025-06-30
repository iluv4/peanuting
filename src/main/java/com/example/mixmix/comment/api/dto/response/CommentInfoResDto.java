package com.example.mixmix.comment.api.dto.response;

import com.example.mixmix.comment.domain.Comment;
import com.example.mixmix.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CommentInfoResDto(
        Long CommentWriterId,
        String contents,
        String nickname,
        String picture,
        LocalDateTime createdAt,
        String nationality
) {
    public static CommentInfoResDto from(Comment comment, Member member) {
        return CommentInfoResDto.builder()
                .CommentWriterId(member.getId())
                .contents(comment.getContents())
                .nickname(member.getNickname())
                .picture(member.getPicture())
                .createdAt(comment.getCreatedAt())
                .nationality(member.getNationality())
                .build();
    }
}