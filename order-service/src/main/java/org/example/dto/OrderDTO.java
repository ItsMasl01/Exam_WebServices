@Data
public class OrderDTO {
    private Long id;
    private Long clientId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemDTO> items;
}