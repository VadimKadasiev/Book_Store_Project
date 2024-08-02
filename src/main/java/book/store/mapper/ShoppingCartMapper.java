package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.ShoppingCartResponseDto;
import book.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartResponseDto toShoppingCartResponseDto(ShoppingCart shoppingCart);
}
