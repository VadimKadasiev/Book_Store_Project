package book.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import book.store.dto.CategoryDto;
import book.store.mapper.CategoryMapper;
import book.store.model.Category;
import book.store.repository.CategoryRepository;
import book.store.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
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
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    private final Category category = new Category();
    private final CategoryDto categoryDto = new CategoryDto();

    @BeforeEach
    void beforeEach() {
        category.setId(1L);
        category.setName("Some Category");
        categoryDto.setName(category.getName());
    }

    @Test
    @DisplayName("")
    void findAll_ValidPageable_ReturnsListOfCategoryDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        List<CategoryDto> categoryDtoList = categoryServiceImpl.findAll(pageable);
        assertEquals(categoryDtoList.size(), categories.size());
    }

    @Test
    @DisplayName("Returns Category By Existing Id")
    void findById_ExistingId_ReturnsCategoryDto() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto actual = categoryServiceImpl.getById(categoryId);
        assertEquals(category.getName(), actual.getName());
    }

    @Test
    @DisplayName("Throws Exception After Searching For Category With NonExisting Id")
    void findById_NonExistingId_ThrowsException() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryServiceImpl.getById(categoryId));
        String expectedMessage = "Can't find category with ID " + categoryId;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Verify update() method")
    void updateCategory_withExistingCategoryId_ReturnsCategoryDto() {
        Long categoryId = 1L;

        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setName("Updated Category Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(updatedCategoryDto);

        CategoryDto actual = categoryServiceImpl.update(categoryId, updatedCategoryDto);
        assertEquals("Updated Category Name", actual.getName());
    }
}
