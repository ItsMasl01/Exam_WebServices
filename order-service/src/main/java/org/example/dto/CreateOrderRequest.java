@Data
public class CreateOrderRequest {
    private Long clientId;
    private List<OrderItemRequest> items;
}