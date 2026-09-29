package my.academy.springbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.academy.springbookstore.dto.user.UserLoginRequestDto;
import my.academy.springbookstore.dto.user.UserLoginResponseDto;
import my.academy.springbookstore.dto.user.UserRegistrationRequestDto;
import my.academy.springbookstore.dto.user.UserResponseDto;
import my.academy.springbookstore.exception.RegistrationException;
import my.academy.springbookstore.security.AuthenticationService;
import my.academy.springbookstore.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user", description = "Register a new user")
    public UserResponseDto register(
            @RequestBody @Valid UserRegistrationRequestDto userRegistrationRequestDto)
            throws RegistrationException {
        return userService.register(userRegistrationRequestDto);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Authenticate user and return JWT-token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }

}
