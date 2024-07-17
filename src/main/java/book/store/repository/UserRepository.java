package book.store.repository;

import book.store.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Optional<User> findByEmail(String email);

}
