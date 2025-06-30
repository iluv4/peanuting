package com.example.mixmix.chatting.api;

import com.example.mixmix.chatting.api.dto.request.ChatRoomReqDto;
import com.example.mixmix.chatting.api.dto.response.ChatRoomResDto;
import com.example.mixmix.chatting.api.dto.response.ChatRoomResList;
import com.example.mixmix.chatting.application.ChatRoomService;
import com.example.mixmix.global.annotation.CurrentUserEmail;
import com.example.mixmix.global.template.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    public RspTemplate<ChatRoomResDto> createChatRoom(
            @CurrentUserEmail String email,
            @RequestBody ChatRoomReqDto request) {
        ChatRoomResDto chatRoomResDto = chatRoomService.createChatRoom(email, request);

        return new RspTemplate<>(HttpStatus.CREATED, "채팅방 생성", chatRoomResDto);
    }

    @GetMapping
    public RspTemplate<ChatRoomResList> getChatRooms(
            @CurrentUserEmail String email,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        return new RspTemplate<>(HttpStatus.OK, "내 채팅방 조회",
                chatRoomService.getChatRooms(email, PageRequest.of(page, size)));
    }
}