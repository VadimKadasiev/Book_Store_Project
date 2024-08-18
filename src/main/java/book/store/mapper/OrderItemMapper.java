package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.OrderItemResponseDto;
import book.store.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);
}
