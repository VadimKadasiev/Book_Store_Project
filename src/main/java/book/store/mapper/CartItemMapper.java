package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.CartItemResponseDto;
import book.store.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "bookId",source = "book.id")
    @Mapping(target = "title",source = "book.title")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);
}
