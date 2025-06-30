package com.example.mixmix.translation.application;

import com.example.mixmix.translation.api.dto.response.DeeplInfoResDto;
import com.example.mixmix.translation.api.dto.response.DeeplListResDto;
import com.example.mixmix.translation.api.dto.response.TranslateChatResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TranslatorService {

    private static final String deeplApiUrl = "https://api-free.deepl.com/v2/translate";

    @Value("${deepl.rest-api-key}")
    private String restApiKey;

    private final RestTemplate restTemplate;

    public TranslatorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TranslateChatResDto translateChat(String targetLang, List<String> text) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "DeepL-Auth-Key "+restApiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("text", text);
        body.put("target_lang", targetLang);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<DeeplListResDto> response = restTemplate.postForEntity(deeplApiUrl, requestEntity, DeeplListResDto.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            DeeplListResDto responseBody = response.getBody();
            try {
                DeeplInfoResDto translation = responseBody.translations().get(0);

                return new TranslateChatResDto(
                        translation.detected_source_language(),
                        translation.text()
                );
            } catch (Exception e) {
                throw new RuntimeException("채팅 내용 번역에 실패했습니다.", e);
            }
        }
        throw new RuntimeException("Deepl API를 호출하는데 실패했습니다");
    }
}