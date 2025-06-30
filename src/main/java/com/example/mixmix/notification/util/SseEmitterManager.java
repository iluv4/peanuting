package com.example.mixmix.notification.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseEmitterManager {

    private static final String EMITTER_NAME = "notification";
    private static final String DUMMY_MESSAGE = "연결 성공";

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(Long memberId) {
        if (emitters.get(memberId) != null) {
            removeEmitter(memberId);
        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.put(memberId, emitter);

        sendNotification(memberId, DUMMY_MESSAGE);

        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError((e) -> emitters.remove(memberId));

        return emitter;
    }

    public void sendNotification(Long memberId, Object data) {
        SseEmitter emitter = emitters.get(memberId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                                .name(EMITTER_NAME)
                                .data(data));
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        }
    }

    public void removeEmitter(Long memberId) {
        SseEmitter emitter = emitters.remove(memberId);

        if (emitter != null) {
            emitter.complete();
            log.info("해당 회원의 기존 연결이 종료되었습니다: {}", memberId);
        } else {
            log.warn("연결되어있는 에미터가 존재하지 않습니다.");
        }
    }
}