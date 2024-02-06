package book.store.repository;

import book.store.model.Book;
import org.hibernate.mapping.List;

public interface BookRepository {
    Book save(Book book);

    List findAll();
}
