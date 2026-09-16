package my.academy.springbookstore.service.user;

import my.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import my.academy.springbookstore.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
