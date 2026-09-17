package my.academy.springbookstore.service.user;

import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import my.academy.springbookstore.dto.user.UserResponseDto;
import my.academy.springbookstore.exception.RegistrationException;
import my.academy.springbookstore.mapper.UserMapper;
import my.academy.springbookstore.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("Can`t register user, email already exists: "
                    + userRegistrationRequestDto.getEmail());
        }

        return userMapper.toDto(userRepository
                .save(userMapper.toModel(userRegistrationRequestDto)));
    }
}
