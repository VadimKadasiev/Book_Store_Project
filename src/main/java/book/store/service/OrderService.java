package book.store.service;

import book.store.dto.OrderItemResponseDto;
import book.store.dto.OrderRequestDto;
import book.store.dto.OrderResponseDto;
import book.store.model.Order;
import book.store.model.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(User user, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrders(User user, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, long orderItemId, User user);

    void changeOrderStatus(Long orderId, Order.OrderStatus orderStatus);

    Set<OrderItemResponseDto> getOrderItems(Long orderId, User user, Pageable pageable);
}
