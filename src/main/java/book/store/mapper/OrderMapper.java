package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.OrderResponseDto;
import book.store.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItems")
    OrderResponseDto orderToOrderResponseDto(Order order);
}
