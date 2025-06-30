package com.example.mixmix.chatting.application;

import com.example.mixmix.chatting.api.dto.request.ChatRoomReqDto;
import com.example.mixmix.chatting.api.dto.response.ChatRoomResList;
import com.example.mixmix.chatting.api.dto.response.ChatRoomResDto;
import com.example.mixmix.chatting.domain.ChatMessage;
import com.example.mixmix.chatting.domain.ChatRoom;
import com.example.mixmix.chatting.domain.repository.ChatMessageRepository;
import com.example.mixmix.chatting.domain.repository.ChatRoomRepository;
import com.example.mixmix.global.dto.PageInfoResDto;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import java.util.List;
import java.util.Optional;
import com.example.mixmix.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoomResDto createChatRoom(String email, ChatRoomReqDto chatRoomReqDto) {
        Member frommember = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
        Member toMember = memberRepository.findById(chatRoomReqDto.toMemberId())
                .orElseThrow(MemberNotFoundException::new);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomReqDto.roomName())
                .fromMember(frommember)
                .toMember(toMember)
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return ChatRoomResDto.from(savedChatRoom.getId(),
                savedChatRoom.getRoomName(),
                frommember.getId(),
                toMember.getId(),
                frommember.getName(),
                null,
                0,
                toMember.getPicture());
    }

    public ChatRoomResList getChatRooms(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        Page<ChatRoom> chatRoomPage = chatRoomRepository.findChatRoomsByMember(member, pageable);

        List<ChatRoomResDto> chatRoomResDtos = chatRoomPage.getContent().stream()
                .map(chatRoom -> ChatRoomResDto.builder()
                        .roomId(chatRoom.getId())
                        .name(chatRoom.getRoomName())
                        .fromMemberId(chatRoom.getFromMember().getId())
                        .toMemberId(chatRoom.getToMember().getId())
                        .loginUserName(member.getName())
                        .recentMessage(getRecentMessage(chatRoom))
                        .unreadNotification(notificationRepository.countUnreadChatNotifications(member, chatRoom))
                        .memberImage(getOtherMemberImage(chatRoom, member))
                        .build()
                )
                .toList();

        PageInfoResDto pageInfoResDto = PageInfoResDto.builder()
                .totalPages(chatRoomPage.getTotalPages())
                .totalItems(chatRoomPage.getTotalElements())
                .currentPage(chatRoomPage.getNumber())
                .build();

        return ChatRoomResList.of(chatRoomResDtos, pageInfoResDto);
    }

    private String getOtherMemberImage(ChatRoom chatRoom, Member currentUser) {
        if (chatRoom.getFromMember().getId().equals(currentUser.getId())) {
            return chatRoom.getToMember().getPicture();
        } else {
            return chatRoom.getFromMember().getPicture();
        }
    }

    private String getRecentMessage(ChatRoom chatRoom) {
        Optional<ChatMessage> chatMessage = chatMessageRepository.findFirstByChatRoomOrderByTimestampDesc(chatRoom);
        if (chatMessage.isPresent()) {
            return chatMessage.get().getContent();
        }
        return null;
    }
}