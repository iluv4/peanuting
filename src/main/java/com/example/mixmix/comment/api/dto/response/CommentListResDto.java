package com.example.mixmix.comment.api.dto.response;

import com.example.mixmix.global.dto.PageInfoResDto;
import java.util.List;
import lombok.Builder;

@Builder
public record CommentListResDto(
        List<CommentInfoResDto> commentListResDtos,
        PageInfoResDto pageInfoResDto
) {
    public static CommentListResDto of(List<CommentInfoResDto> commentListResDtos, PageInfoResDto pageInfoResDto) {
        return CommentListResDto.builder()
                .commentListResDtos(commentListResDtos)
                .pageInfoResDto(pageInfoResDto)
                .build();
    }
}