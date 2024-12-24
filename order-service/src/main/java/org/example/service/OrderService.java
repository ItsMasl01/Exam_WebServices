package com.example.order.service;

import com.example.order.dto.CreateOrderRequest;
import com.example.order.dto.OrderDTO;
import com.example.order.dto.UpdateOrderStatusRequest;
import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setClientId(request.getClientId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // Créer les items de la commande
        List<OrderItem> orderItems = request.getItems().stream()
            .map(itemRequest -> {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProductId(itemRequest.getProductId());
                item.setQuantity(itemRequest.getQuantity());
                // Récupérer le prix du produit via le service produit
                item.setUnitPrice(productService.getProductPrice(itemRequest.getProductId()));
                return item;
            })
            .collect(Collectors.toList());

        order.setItems(orderItems);

        // Calculer le montant total
        BigDecimal totalAmount = orderItems.stream()
            .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return mapToDTO(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getClientOrders(Long clientId) {
        return orderRepository.findByClientId(clientId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        order.setStatus(request.getStatus());
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }

    private OrderDTO mapToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setClientId(order.getClientId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());

        List<OrderItemDTO> itemDTOs = order.getItems().stream()
            .map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                itemDTO.setId(item.getId());
                itemDTO.setProductId(item.getProductId());
                itemDTO.setQuantity(item.getQuantity());
                itemDTO.setUnitPrice(item.getUnitPrice());
                return itemDTO;
            })
            .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}