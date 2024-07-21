package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.CategoryDto;
import book.store.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryDto categoryDto);
}