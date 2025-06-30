package com.example.mixmix.notification.domain.repository;

import com.example.mixmix.chatting.domain.ChatRoom;
import com.example.mixmix.member.domain.Member;
import com.example.mixmix.notification.domain.Notification;
import com.example.mixmix.notification.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationCustomRepository {
    List<Notification> findAllByReceiver(Member receiver);
    List<Notification> findAllByReceiverAndType(Member receiver, Type type);

    Boolean existsByReceiverAndIsReadFalse(Member receiver);

    @Query("SELECT count(n) FROM Notification n " +
            "join ChatMessage m on n.relatedEntityId = m.id " +
            "where n.type = 'CHAT' and n.receiver = :receiver and m.chatRoom = :chatRoom and n.isRead = false")
    Integer countUnreadChatNotifications(Member receiver, ChatRoom chatRoom);

    @Query("SELECT n FROM Notification n " +
            "where n.type = 'CHAT' and n.receiver = :receiver and n.relatedEntityId = :messageId and n.isRead = false")
    Optional<Notification> findUnreadChatNotification(Member receiver, Long messageId);
}