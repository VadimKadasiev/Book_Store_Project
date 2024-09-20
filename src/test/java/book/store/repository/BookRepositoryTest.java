package book.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import book.store.model.Book;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-categories-to-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-books-to-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/add-data-to-books-categories-table.sql"));
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books/remove-all-data-from-tables.sql"));
        }
    }

    @Test
    @DisplayName("Returns all books with existing categories")
    void findAllByCategoryId_WithExistingCategories_ReturnsBooksList() {
        long categoryId = 1L;
        List<Book> actualForCategoryOne = bookRepository.findAllByCategoryId(categoryId);
        assertEquals(3, actualForCategoryOne.size());

        categoryId = 2L;
        List<Book> actualForCategoryTwo = bookRepository.findAllByCategoryId(categoryId);
        assertEquals(2, actualForCategoryTwo.size());

        categoryId = 3L;
        List<Book> actualForCategoryThree = bookRepository.findAllByCategoryId(categoryId);
        assertEquals(1, actualForCategoryThree.size());
    }

    @Test
    @DisplayName("Returns Empty List After Searching For Books With NonExisting Category")
    void findAllByCategoryId_WithNoExistingCategory_ReturnsBooksList() {
        long categoryId = 100L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);
        List<Book> expected = List.of();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Returns Book With Existing Id")
    void getBookById_WithExistingId_ReturnsBook() {
        Long bookOneId = 1L;
        Optional<Book> actual = bookRepository.findById(bookOneId);
        assertEquals(actual.get().getTitle(), "Title 1");

        Long bookTwoId = 2L;
        Optional<Book> actual2 = bookRepository.findById(bookTwoId);
        assertEquals(actual2.get().getIsbn(), "A02");
    }

    @Test
    @DisplayName("Returns Empty Field After Searching For Book With NonExisting Id")
    void getBookById_withNonExistingId_ReturnsEmptyField() {
        Long bookId = 100L;
        Optional<Book> expected = Optional.empty();
        Optional<Book> actual = bookRepository.getBookById(bookId);
        assertEquals(expected, actual);
    }
}
