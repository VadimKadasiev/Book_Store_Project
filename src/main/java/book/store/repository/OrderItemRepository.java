package book.store.repository;

import book.store.model.OrderItem;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findOrderItemByOrderIdAndId(long orderId, long orderItemId);

    Set<OrderItem> findOrderItemByOrderId(long orderId);

    @Query(value = "SELECT oi FROM OrderItem oi "
            + "JOIN FETCH oi.order o "
            + "JOIN FETCH o.user WHERE oi.id = :orderItemId "
            + "AND o.id =:orderId AND o.user.id=:userId")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(long orderItemId, long orderId, long userId);
}
