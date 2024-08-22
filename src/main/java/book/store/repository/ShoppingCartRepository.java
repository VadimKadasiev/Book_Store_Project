package book.store.repository;

import book.store.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByUserId(Long userId);

    ShoppingCart findByUserId(Long userId);
}
