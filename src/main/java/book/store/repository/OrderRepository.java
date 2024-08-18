package book.store.repository;

import book.store.model.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId);

    Order findByIdAndUserId(long orderId, long userId);
}
