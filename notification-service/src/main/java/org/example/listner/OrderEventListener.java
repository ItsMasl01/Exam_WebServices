package org.example.listner;

import com.example.notification.entity.NotificationType;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queue.order.notification}")
    public void handleOrderCreated(OrderEvent event) {
        log.info("Received order event: {}", event);

        switch (event.getEventType()) {
            case "ORDER_CREATED":
                notificationService.createOrderNotification(
                    event.getUserId(),
                    String.format("Votre commande #%d a été créée avec succès", event.getOrderId()),
                    NotificationType.ORDER_CREATED
                );
                break;
            case "ORDER_UPDATED":
                notificationService.createOrderNotification(
                    event.getUserId(),
                    String.format("Votre commande #%d a été mise à jour. Nouveau statut : %s", 
                        event.getOrderId(), event.getNewStatus()),
                    NotificationType.ORDER_UPDATED
                );
                break;
            default:
                log.warn("Unknown event type: {}", event.getEventType());
        }
    }
}