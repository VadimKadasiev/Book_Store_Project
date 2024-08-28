package book.store.repository;

import book.store.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId);

    Optional<Order> findByIdAndUserId(long orderId, long userId);
}
