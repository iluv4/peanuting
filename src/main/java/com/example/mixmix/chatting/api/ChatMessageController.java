package com.example.mixmix.chatting.api;

import com.example.mixmix.chatting.api.dto.response.ChatMessageResList;
import com.example.mixmix.chatting.application.ChatMessageService;
import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import com.example.mixmix.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final NotificationService notificationService;

    @GetMapping("/{roomId}")
    public RspTemplate<ChatMessageResList> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @CurrentUserEmail String email) {
        notificationService.markChatRead(email, roomId, PageRequest.of(page, size));

        return new RspTemplate<>(HttpStatus.OK, "채팅 메시지 조회",
                chatMessageService.findChatMessages(roomId, PageRequest.of(page, size)));
    }
}