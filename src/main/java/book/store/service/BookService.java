package book.store.service;

import book.store.model.Book;
import org.hibernate.mapping.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
