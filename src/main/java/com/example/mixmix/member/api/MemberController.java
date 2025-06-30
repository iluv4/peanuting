package com.example.mixmix.member.api;

import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.member.api.dto.request.JoinMyPageUpdateReqDto;
import com.example.mixmix.member.api.dto.response.JoinMyPageInfoResDto;
import com.example.mixmix.member.application.MemberService;
import com.example.mixmix.member.mypage.api.dto.response.MyPageInfoResDto;
import com.example.mixmix.member.mypage.application.MyPageService;
import com.example.mixmix.notification.api.dto.response.NotificationListResDto;
import com.example.mixmix.notification.application.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/member")
public class MemberController {

    private final MemberService memberService;
    private final MyPageService myPageService;
    private final NotificationService notificationService;

    @Operation(summary = "회원가입할 때 추가 정보 요청 / 마이페이지 수정", description = "회원가입할 때 추가 정보를 입력받습니다. / 마이페이지를 수정합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh 토큰으로 access 토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/mypage/join")
    public RspTemplate<JoinMyPageInfoResDto> joinRequest(@CurrentUserEmail String email,
                                                         @RequestBody JoinMyPageUpdateReqDto joinMyPageUpdateReqDto) {
        return new RspTemplate<>(HttpStatus.OK,
                "가입 요청",
                memberService.joinRequest(email, joinMyPageUpdateReqDto));
    }

    @Operation(summary = "나의 마이페이지를 조회", description = "나의 마이페이지를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh 토큰으로 access 토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/mypage")
    public RspTemplate<MyPageInfoResDto> myProfileInfo(@CurrentUserEmail String email) {
        Boolean unreadNotification = notificationService.hasUnreadNotifications(email);
        MyPageInfoResDto memberResDto = myPageService.findMyProfileByEmail(email, unreadNotification);
        return new RspTemplate<>(HttpStatus.OK, "내 프로필 정보", memberResDto);
    }

    @Operation(summary = "상대방의 마이페이지를 조회", description = "상대방의 마이페이지를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh 토큰으로 access 토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/mypage/{otherId}")
    public RspTemplate<MyPageInfoResDto> getFriendProfileInfo(@CurrentUserEmail String email, @PathVariable Long otherId) {
        Boolean unreadNotification = notificationService.hasUnreadNotifications(email);
        MyPageInfoResDto friendProfile = myPageService.findMyProfileById(otherId, unreadNotification);
        return new RspTemplate<>(HttpStatus.OK, "친구 프로필 정보 조회", friendProfile);
    }

    @Operation(summary = "마이페이지에서 알림 조회", description = "마이페이지 내에서 알림을 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "refresh 토큰으로 access 토큰 재발급 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/mypage/notifications")
    public RspTemplate<NotificationListResDto> getAllNotifications(@CurrentUserEmail String email) {
        NotificationListResDto notifications = notificationService.findAllNotifications(email);
        notificationService.markAllNotificationsRead(email);

        return new RspTemplate<>(HttpStatus.OK, "마이페이지 내에서 전체 알림 조회", notifications);
    }
}