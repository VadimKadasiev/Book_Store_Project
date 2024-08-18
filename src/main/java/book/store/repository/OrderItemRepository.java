package book.store.repository;

import book.store.model.OrderItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findOrderItemByOrderIdAndId(long orderId, long orderItemId);

    Set<OrderItem> findOrderItemByOrderId(long orderId);
}
