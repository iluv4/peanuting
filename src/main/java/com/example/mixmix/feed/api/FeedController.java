package com.example.mixmix.feed.api;

import com.example.mixmix.feed.api.dto.request.FeedSaveReqDto;
import com.example.mixmix.feed.api.dto.response.FeedInfoResDto;
import com.example.mixmix.feed.api.dto.response.FeedListResDto;
import com.example.mixmix.feed.api.dto.response.FeedSaveInfoResDto;
import com.example.mixmix.feed.application.FeedService;
import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.s3.application.AwsS3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;
    private final AwsS3Service awsS3Service;

    @Operation(summary = "게시물(피드)을 생성합니다.", description = "게시물(피드)을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping
    public RspTemplate<FeedSaveInfoResDto> save(@CurrentUserEmail String email,
                                                @RequestPart("feedSaveReqDto") String feedSaveReqDtoJson,
                                                @RequestPart(value = "feedImage") List<MultipartFile> feedImage) {
        List<String> imageUrls = awsS3Service.uploadFile(feedImage);
        ObjectMapper objectMapper = new ObjectMapper();
        FeedSaveReqDto feedSaveReqDto;

        try {
            feedSaveReqDto = objectMapper.readValue(feedSaveReqDtoJson, FeedSaveReqDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 오류: " + e.getMessage());
        }

        return new RspTemplate<>(HttpStatus.CREATED, "피드 생성", feedService.save(email, feedSaveReqDto, imageUrls));
    }

    @Operation(summary = "게시물(피드)을 개별 조회합니다.", description = "게시물(피드)을 개별 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/{feedId}")
    public RspTemplate<FeedInfoResDto> getFeedDetail(@PathVariable(name = "feedId") Long feedId) {
        return new RspTemplate<>(HttpStatus.OK, "피드 개별 조회", feedService.findById(feedId));
    }

    @Operation(summary = "게시물(피드)을 전체 조회합니다", description = "게시물(피드)을 전체 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @GetMapping("/all")
    public RspTemplate<FeedListResDto> getFeedList(@RequestParam(name = "keyword") String keyword,
                                                   @RequestParam(name = "nationality") String nationality,
                                                   @RequestParam(name = "page", defaultValue = "0") int page,
                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        return new RspTemplate<>(HttpStatus.OK, "피드 soical/education 구분해서 전체 조회",
                feedService.findAllByFeedType(keyword,
                        nationality,
                        PageRequest.of(page, size)));
    }

    @Operation(summary = "게시물(피드)을 수정", description = "게시물(피드)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/{feedId}")
    public RspTemplate<FeedInfoResDto> update(@PathVariable(name = "feedId") Long feedId,
                                                  @RequestBody FeedSaveReqDto feedSaveReqDto) {
        return new RspTemplate<>(HttpStatus.OK, "피드 수정", feedService.update(feedId, feedSaveReqDto));
    }

    @Operation(summary = "게시물(피드)을 삭제", description = "게시물(피드)을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID 토큰 반환 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 값"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PatchMapping("/delete/{feedId}")
    public RspTemplate<Void> delete(@PathVariable(name = "feedId") Long feedId) {
        feedService.delete(feedId);
        return new RspTemplate<>(HttpStatus.OK, "피드 삭제");
    }
}