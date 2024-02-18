package book.store.repository;

import book.store.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> getBookById(Long id);
}
