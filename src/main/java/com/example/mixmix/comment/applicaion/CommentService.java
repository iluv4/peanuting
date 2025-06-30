package com.example.mixmix.comment.applicaion;

import com.example.mixmix.comment.api.dto.request.CommentSaveReqDto;
import com.example.mixmix.comment.api.dto.response.CommentInfoResDto;
import com.example.mixmix.comment.api.dto.response.CommentListResDto;
import com.example.mixmix.comment.domain.Comment;
import com.example.mixmix.comment.domain.repository.CommentRepository;
import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.repository.FeedRepository;
import com.example.mixmix.feed.exception.FeedNotFoundException;
import com.example.mixmix.global.dto.PageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import java.util.List;
import com.example.mixmix.notification.application.NotificationService;
import com.example.mixmix.notification.domain.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final NotificationService notificationService;

    private static final String COMMENT_NOTIFICATION_MESSAGE = "%s님이 회원님의 게시물에 댓글을 남겼습니다.";

    @Transactional
    public CommentInfoResDto save(String email, CommentSaveReqDto commentSaveReqDto) {
        Member member = memberRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);
        Feed feed = feedRepository.findById(commentSaveReqDto.feedId())
                .orElseThrow(FeedNotFoundException::new);

        Comment comment = commentRepository.save(commentSaveReqDto.toEntity(member, feed));

        if (member != feed.getMember()) {
            String commentCreateMessage = String.format(COMMENT_NOTIFICATION_MESSAGE, member.getNickname());
            notificationService.sendNotification(feed.getMember(), Type.COMMENT, commentCreateMessage, feed.getId());
        }

        return CommentInfoResDto.from(comment, member);
    }

    public CommentListResDto findCommentsByFeedId(Long feedId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAllByFeedId(feedId, pageable);

        List<CommentInfoResDto> commentInfoResDtos = commentPage.getContent().stream()
                .map(comment -> CommentInfoResDto.builder()
                        .CommentWriterId(comment.getMember().getId())
                        .contents(comment.getContents())
                        .nickname(comment.getMember().getNickname())
                        .nationality(comment.getMember().getNationality())
                        .picture(comment.getMember().getPicture())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .toList();

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .currentPage(commentPage.getNumber())
                .totalPages(commentPage.getTotalPages())
                .totalItems(commentPage.getTotalElements())
                .build();

        return CommentListResDto.of(commentInfoResDtos, pageInfoResDto);
    }
}