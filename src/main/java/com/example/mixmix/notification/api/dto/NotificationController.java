package com.example.mixmix.notification.api.dto;

import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.notification.api.dto.response.NotificationListResDto;
import com.example.mixmix.notification.application.NotificationService;
import com.example.mixmix.notification.domain.Type;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "SSE 연결 확인", description = "(로그인 시) 알림 수신 준비로 SSE 연결을 설정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 스트림 연결 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/stream")
    public SseEmitter streamNotifications(@CurrentUserEmail String email) {
        return notificationService.createEmitter(email);
    }

    @Operation(summary = "알림 목록 조회", description = "알림 정보를 조회합니다 (유형별 알림은 쿼리 파라미터 활용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping
    public RspTemplate<NotificationListResDto> getNotifications( @CurrentUserEmail String email,
            @RequestParam(value = "type", required = false) Type type) {
        NotificationListResDto response;

        if (type == null) {
            response = notificationService.findAllNotifications(email);
            notificationService.markNotificationsRead(email, Type.STREAK);
            notificationService.markNotificationsRead(email, Type.COMMENT);
        } else {
            response = notificationService.findNotificationsByType(email, type);
            notificationService.markNotificationsRead(email, type);
        }

        return new RspTemplate<>(HttpStatus.OK, "알림 조회 성공", response);
    }

    @Operation(summary = "전체 알림 읽음", description = "전체 알림을 읽음으로 표시합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 알림 읽음 처리 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping
    public RspTemplate<Void> markAllNotificationsAsRead(@CurrentUserEmail String email) {
        notificationService.markAllNotificationsRead(email);

        return new RspTemplate<>(HttpStatus.OK, "모든 알림을 읽음으로 처리했습니다.");
    }

    @Operation(summary = "SSE 연결 해제", description = "(로그아웃 시) SSE 연결을 해제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알림 스트림 연결 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @DeleteMapping("/disconnect")
    public RspTemplate<Void> disconnectNotification(@CurrentUserEmail String email) {
        notificationService.disconnectEmitter(email);

        return new RspTemplate<>(HttpStatus.OK, "연결 해제 성공");
    }
}