package org.example.entity;

import lombok.Data;

@Data
public class OrderEvent {
    private String eventType;
    private Long orderId;
    private Long userId;
    private String newStatus;
}