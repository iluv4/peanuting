package com.example.mixmix.chatting.application;

import com.example.mixmix.chatting.api.dto.request.ChatMessageReqDto;
import com.example.mixmix.chatting.api.dto.response.ChatMessageResList;
import com.example.mixmix.chatting.api.dto.response.ChatMessageResDto;
import com.example.mixmix.chatting.domain.ChatMessage;
import com.example.mixmix.chatting.domain.ChatRoom;
import com.example.mixmix.chatting.domain.repository.ChatMessageRepository;
import com.example.mixmix.chatting.domain.repository.ChatRoomRepository;
import com.example.mixmix.chatting.exception.ExistsChatRoomException;
import com.example.mixmix.global.dto.PageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.notification.application.NotificationService;
import com.example.mixmix.notification.domain.Type;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationService notificationService;
    private static final String CHAT_NOTIFICATION_MESSAGE = "새로운 메시지가 있습니다.";

    @Transactional
    public void saveMessage(String roomId, String sender, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(roomId)).
                orElseThrow(ExistsChatRoomException::new);

        ChatMessage chatMessage = ChatMessageReqDto.toEntity(chatRoom, content, sender, LocalDateTime.now());

        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        Member member;
        if (!chatRoom.getFromMember().getName().equals(sender)) {
            member = chatRoom.getFromMember();
        } else {
            member = chatRoom.getToMember();
        }
        notificationService.sendNotification(member, Type.CHAT, CHAT_NOTIFICATION_MESSAGE, savedChatMessage.getId());
    }

    public ChatMessageResList findChatMessages(Long roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ExistsChatRoomException::new);

        Page<ChatMessage> messagePage = chatMessageRepository.findByChatRoom(chatRoom, pageable);

        List<ChatMessageResDto> messageDtos = messagePage.getContent().stream()
                .map(ChatMessageResDto::fromEntity)
                .toList();

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .totalPages(messagePage.getTotalPages())
                .totalItems(messagePage.getTotalElements())
                .currentPage(messagePage.getNumber())
                .build();

        return ChatMessageResList.of(messageDtos, pageInfoResDto);
    }
}