package book.store.repository.impl;

import book.store.model.Book;
import book.store.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Book save(Book book) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't save to DB book " + book);
        }
    }

    @Override
    public List<Book> findAll() {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT b FROM Book b",
                    Book.class).getResultList();
        } catch (RuntimeException e) {
            throw new RuntimeException("Can't get all books from DB");
        }
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return Optional.ofNullable(entityManager.find(Book.class, id));
        } catch (RuntimeException e) {
            throw new RuntimeException("Can't get book by ID " + id);
        }
    }
}
