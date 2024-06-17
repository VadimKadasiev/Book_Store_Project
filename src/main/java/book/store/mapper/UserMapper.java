package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.UserRegistrationRequestDto;
import book.store.dto.UserResponseDto;
import book.store.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toUser(UserRegistrationRequestDto user);
}
