package book.store.service.impl;

import book.store.dto.BookDto;
import book.store.dto.BookDtoWithoutCategoryIds;
import book.store.dto.CreateBookRequestDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.BookMapper;
import book.store.mapper.CategoryMapper;
import book.store.model.Book;
import book.store.repository.BookRepository;
import book.store.service.BookService;
import book.store.service.CategoryService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        book.setCategories(bookRequestDto.getCategoryIds().stream()
                .map(categoryService::getById)
                .map(categoryMapper::toModel)
                .collect(Collectors.toSet()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.getBookById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find book by ID " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        book.setId(id);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
