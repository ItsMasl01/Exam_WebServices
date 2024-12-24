package org.example.service;

import org.example.entity.Notification;
import org.example.entity.NotificationType;
import org.example.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void createOrderNotification(Long userId, String message, NotificationType type) {
        log.info("Creating notification for user: {}, type: {}", userId, type);
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setType(type);
        
        notificationRepository.save(notification);
        
        // Ici, vous pourriez ajouter la logique pour envoyer la notification 
        // via email, SMS, ou WebSocket selon vos besoins
        sendNotification(notification);
    }

    private void sendNotification(Notification notification) {
        // Simulation d'envoi de notification
        log.info("Sending notification: {}", notification.getMessage());
        // Implémentez ici la logique d'envoi réel (email, SMS, etc.)
    }

    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }
}