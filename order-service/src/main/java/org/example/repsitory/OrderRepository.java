package com.example.order.repository;

import com.example.order.entity.Order;
import com.example.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId);
    
    List<Order> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.clientId = :clientId AND o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByClientIdAndDateRange(Long clientId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.orderDate < :date")
    List<Order> findOldOrdersByStatus(OrderStatus status, LocalDateTime date);
}