package com.example.mixmix.translation.api;

import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.translation.api.dto.request.TranslateChatReqDto;
import com.example.mixmix.translation.api.dto.response.TranslateChatResDto;
import com.example.mixmix.translation.application.TranslatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/translations")
public class TranslatorController {

    private final TranslatorService translatorService;

    @Operation(summary = "채팅 내용 번역", description = "상대방의 채팅 내용을 사용자의 국적에 맞게 번역합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 국적의 언어로 번역했습니다"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다"),
            @ApiResponse(responseCode = "401", description = "헤더 없음 or 토큰 불일치",
                    content = @Content(schema = @Schema(example = "INVALID_HEADER or INVALID_TOKEN")))
    })
    @PostMapping
    public RspTemplate<TranslateChatResDto> translate(@RequestBody TranslateChatReqDto translateChatReqDto) {
        String targetLang = translateChatReqDto.targetLang();
        List<String> text = List.of(translateChatReqDto.text());

        return new RspTemplate<>(HttpStatus.OK, "번역 성공", translatorService.translateChat(targetLang, text));
    }
}
