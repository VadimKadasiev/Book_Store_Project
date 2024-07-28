package book.store.repository;

import book.store.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

}
