package com.example.mixmix.notification.application;

import com.example.mixmix.chatting.domain.ChatMessage;
import com.example.mixmix.chatting.domain.ChatRoom;
import com.example.mixmix.chatting.domain.repository.ChatMessageRepository;
import com.example.mixmix.chatting.domain.repository.ChatRoomRepository;
import com.example.mixmix.chatting.exception.ExistsChatRoomException;
import com.example.mixmix.feed.domain.Feed;
import com.example.mixmix.feed.domain.repository.FeedRepository;
import com.example.mixmix.feed.exception.FeedNotFoundException;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.member.domain.repository.MemberRepository;
import com.example.mixmix.member.exception.MemberNotFoundException;
import com.example.mixmix.notification.api.dto.response.BasicNotificationResDto;
import com.example.mixmix.notification.api.dto.response.CommentNotificationResDto;
import com.example.mixmix.notification.api.dto.response.NotificationInfoResDto;
import com.example.mixmix.notification.api.dto.response.NotificationListResDto;
import com.example.mixmix.notification.domain.Notification;
import com.example.mixmix.notification.domain.Type;
import com.example.mixmix.notification.domain.repository.NotificationRepository;
import com.example.mixmix.notification.util.NotificationPayload;
import com.example.mixmix.notification.util.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationService {
    private static final String STREAK_NOTIFICATION_MESSAGE = "오늘의 도전을 완료하고 새로운 연결을 만들어보세요! \uD83C\uDF0F";

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final FeedRepository feedRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SseEmitterManager sseEmitterManager;

    public SseEmitter createEmitter(String email){
        Member member = findByEmail(email);

        return sseEmitterManager.createEmitter(member.getId());
    }

    public void disconnectEmitter(String email){
        Member member = findByEmail(email);

        sseEmitterManager.removeEmitter(member.getId());
    }

    @Transactional
    public void sendNotification(Member member, Type type, String message, Long relatedEntityId) {
        Notification savedNotification = saveNotification(member, type, message, relatedEntityId);

        sseEmitterManager.sendNotification(member.getId(), createPayload(savedNotification));
    }

    public NotificationListResDto findAllNotifications(String email) {
        Member member = findByEmail(email);
        List<? extends NotificationInfoResDto> notifications = notificationRepository.findAllByReceiver(member)
                .stream()
                .filter(notification -> notification.getType() != Type.CHAT)
                .map(notification -> {
                    if(notification.getType() == Type.COMMENT) {
                        return createComment(notification);
                    } else {
                        return BasicNotificationResDto.from(notification);
                    }
                })
                .toList();

        return NotificationListResDto.of(notifications);
    }

    public NotificationListResDto findNotificationsByType(String email, Type type) {
        Member member = findByEmail(email);
        List<? extends NotificationInfoResDto> notifications;

        if (type == Type.COMMENT) {
            notifications = findCommentNotifications(member);
        } else {
            notifications = findBasicNotifications(member, type);
        }

        return NotificationListResDto.of(notifications);
    }

    @Transactional
    public void markAllNotificationsRead(String email) {
        Member member = findByEmail(email);
        List<Notification> notifications = notificationRepository.findAllByReceiver(member);

        notifications.forEach(Notification::markAsRead);
    }

    @Transactional
    public void markNotificationsRead(String email, Type type) {
        Member member = findByEmail(email);
        List<Notification> notifications = notificationRepository.findAllByReceiverAndType(member, type);

        notifications.forEach(Notification::markAsRead);
    }

    @Transactional
    public void markChatRead(String email, Long roomId, Pageable pageable) {
        Member member = findByEmail(email);
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(ExistsChatRoomException::new);

        List<Long> messageIds = chatMessageRepository.findByChatRoom(chatRoom, pageable)
                .getContent().stream().map(ChatMessage::getId).toList();

        messageIds.forEach(id -> {
            Optional<Notification> notification = notificationRepository.findUnreadChatNotification(member, id);
            if (notification.isPresent()) {
                notification.get().markAsRead();
            }
        });
    }

    public boolean hasUnreadNotifications(String email) {
        Member member = findByEmail(email);

        return notificationRepository.existsByReceiverAndIsReadFalse(member);
    }

    @Transactional
    @Scheduled(cron = "0 00 18 * * ?")
    public void sendStreakNotification() {
        List<Member> members = memberRepository.findAllByIsStreakUpdatedFalse();
        for (Member member : members) {
            Notification savedNotification = saveNotification(member, Type.STREAK, STREAK_NOTIFICATION_MESSAGE, member.getId());
            sseEmitterManager.sendNotification(member.getId(), createPayload(savedNotification));
        }
    }

    private Notification saveNotification(Member member, Type type, String message, Long relatedEntityId) {
        Notification notification = Notification.builder()
                .receiver(member)
                .message(message)
                .type(type)
                .isRead(false)
                .relatedEntityId(relatedEntityId)
                .build();

        return notificationRepository.save(notification);
    }

    private Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    private NotificationPayload createPayload(Notification notification) {
        return new NotificationPayload(notification.getType(), notification.getMessage());
    }

    private CommentNotificationResDto createComment(Notification notification) {
        String feedImage = getFeedImage(notification.getRelatedEntityId());

        return CommentNotificationResDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .type(Type.COMMENT)
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .feedId(notification.getRelatedEntityId())
                .feedImage(feedImage).build();
    }

    private String getFeedImage(Long feedId) {
        Optional<Feed> feed = feedRepository.findById(feedId);
        if (feed.isPresent()) {
            return feed.get().getFeedImage();
        }
        throw new FeedNotFoundException();
    }

    private List<BasicNotificationResDto> findBasicNotifications(Member member, Type type) {
        return notificationRepository.findAllByReceiverAndType(member, type)
                .stream()
                .map(BasicNotificationResDto::from)
                .toList();
    }

    private List<CommentNotificationResDto> findCommentNotifications(Member member) {
        return notificationRepository.findAllByReceiverAndType(member, Type.COMMENT)
                .stream()
                .map(this::createComment)
                .toList();
    }
}
