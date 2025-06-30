package com.example.mixmix.comment.api;

import com.example.mixmix.comment.api.dto.request.CommentSaveReqDto;
import com.example.mixmix.comment.api.dto.response.CommentInfoResDto;
import com.example.mixmix.comment.api.dto.response.CommentListResDto;
import com.example.mixmix.comment.applicaion.CommentService;
import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "게시물(피드)에 댓글을 작성", description = "게시물(피드)에 댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping
    public RspTemplate<CommentInfoResDto> save(@CurrentUserEmail String email,
                                               @RequestBody CommentSaveReqDto commentSaveReqDto) {
        return new RspTemplate<>(HttpStatus.CREATED, "피드 생성", commentService.save(email, commentSaveReqDto));
    }

    @Operation(summary = "게시물(피드)에 작성된 댓글을 조회", description = "게시물(피드)에 작성된 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{feedId}")
    public RspTemplate<CommentListResDto> findCommentsByFeedId(@PathVariable Long feedId, Pageable pageable) {
        return new RspTemplate<>(HttpStatus.OK, "피드 댓글 조회", commentService.findCommentsByFeedId(feedId, pageable));
    }
}