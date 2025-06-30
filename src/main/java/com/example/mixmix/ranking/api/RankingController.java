package com.example.mixmix.ranking.api;

import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.ranking.api.dto.response.RankingInfoResDto;
import com.example.mixmix.ranking.api.dto.response.RankingListResDto;
import com.example.mixmix.ranking.application.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rankings")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "상위 10위 랭킹 조회", description = "Streak 랭킹 상위 10위의 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Streak 랭킹 상위 10위 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping
    public RspTemplate<RankingListResDto> rankList() {
        return new RspTemplate<>(HttpStatus.OK, "Streak 랭킹 상위 10위 조회 성공",
                rankingService.getRankingList());
    }

    @Operation(summary = "회원별 랭킹 조회", description = "해당 id를 가진 회원의 랭킹을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 회원의 랭킹 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{memberId}")
    public RspTemplate<RankingInfoResDto> memberRanking(@PathVariable("memberId") Long memberId) {
        return new RspTemplate<>(HttpStatus.OK, "해당 회원의 랭킹 조회 성공",
                rankingService.getRanking(memberId));
    }
}