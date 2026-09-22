package my.academy.springbookstore.service.user;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import my.academy.springbookstore.dto.user.UserResponseDto;
import my.academy.springbookstore.exception.EntityNotFoundException;
import my.academy.springbookstore.exception.RegistrationException;
import my.academy.springbookstore.mapper.UserMapper;
import my.academy.springbookstore.model.Role;
import my.academy.springbookstore.model.RoleName;
import my.academy.springbookstore.model.User;
import my.academy.springbookstore.repository.role.RoleRepository;
import my.academy.springbookstore.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException("Can`t register user, email already exists: "
                    + userRegistrationRequestDto.getEmail());
        }

        User user = userMapper.toModel(userRegistrationRequestDto);

        user.setPassword(passwordEncoder.encode(userRegistrationRequestDto.getPassword()));

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find role ROLE_USER"));

        user.setRoles(Set.of(role));

        return userMapper.toDto(userRepository.save(user));
    }
}
