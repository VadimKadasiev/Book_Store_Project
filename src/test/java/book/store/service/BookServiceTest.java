package book.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import book.store.dto.BookDto;
import book.store.dto.CategoryDto;
import book.store.dto.CreateBookRequestDto;
import book.store.mapper.BookMapper;
import book.store.mapper.CategoryMapper;
import book.store.model.Book;
import book.store.model.Category;
import book.store.repository.BookRepository;
import book.store.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private static BookRepository bookRepository;

    @Mock
    private static BookMapper bookMapper;

    @Mock
    private static CategoryService categoryService;

    @Mock
    private static CategoryMapper categoryMapper;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    private final Book book = new Book();
    private final BookDto bookDto = new BookDto();
    private final CategoryDto categoryDto = new CategoryDto();

    @BeforeEach
    void beforeEach() {
        book.setId(1L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setPrice(BigDecimal.valueOf(1.20));
        book.setIsbn("A01");
        bookDto.setIsbn(book.getIsbn());
        bookDto.setTitle(book.getTitle());
    }

    @Test
    @DisplayName("Verify save() method works")
    void saveBook_ValidCreateBookRequestDto_returnsBookDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Title");
        bookRequestDto.setAuthor("Author");
        bookRequestDto.setIsbn("A01");
        bookRequestDto.setPrice(BigDecimal.valueOf(1.20));
        bookRequestDto.setCategoryIds(Set.of(1L));

        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toModel(bookRequestDto)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(categoryService.getById(anyLong())).thenReturn(categoryDto);
        when(categoryMapper.toModel(categoryDto)).thenReturn(new Category());

        BookDto savedBookDto = bookServiceImpl.save(bookRequestDto);

        assertEquals(bookRequestDto.getIsbn(), savedBookDto.getIsbn());
    }

    @Test
    @DisplayName("Returns all books from DB")
    void findAll_ValidPageable_returnsAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtoList = bookServiceImpl.findAll(pageable);
        assertEquals(bookDtoList.size(), books.size());
    }

    @Test
    @DisplayName("Returns book with existing Id")
    void getBookById_withExistingBookId_returnsBook() {
        Long bookId = 1L;
        when(bookRepository.getBookById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        BookDto actual = bookServiceImpl.getBookById(bookId);
        assertEquals(book.getIsbn(), actual.getIsbn());
        assertEquals(book.getTitle(), actual.getTitle());
    }

    @Test
    @DisplayName("Throws exception after requesting a book with nonExisting Id")
    void getBookById_withNoExistingId_throwsException() {
        Long bookId = 100L;
        when(bookRepository.getBookById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class,
                () -> bookServiceImpl.getBookById(bookId));
        String expectedMessage = "Can't find book by ID " + bookId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }
}
