package book.store.service;

import book.store.dto.BookDto;
import book.store.dto.BookDtoWithoutCategoryIds;
import book.store.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    void deleteById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto book);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable);
}
