package com.ecomm.service.impl;

import com.ecomm.dto.UserRequestDTO;
import com.ecomm.dto.UserLoginDTO;
import com.ecomm.dto.UserResponseDTO;
import com.ecomm.entity.User;
import com.ecomm.exception.AuthenticationException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@ecomm.com");
        UserResponseDTO userResponseDTO = userService.registerUser(userRequestDTO);

        assertThat(userResponseDTO.getEmail()).isEqualTo("test@ecomm.com");
    }

    @Test
    public void testRegisterUser_EmailExists() {
        when(userRepository.existsByEmail(any())).thenReturn(true);

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@ecomm.com");

        assertThatThrownBy(() -> userService.registerUser(userRequestDTO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Email already exists");
    }

    @Test
    public void testLoginUser() {
        User user = new User();
        user.setEmail("test@ecomm.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("test@ecomm.com");
        userLoginDTO.setPassword("password");
        UserResponseDTO userResponseDTO = userService.loginUser(userLoginDTO);

        assertThat(userResponseDTO.getEmail()).isEqualTo("test@ecomm.com");
    }

    @Test
    public void testLoginUser_UserNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("test@ecomm.com");

        assertThatThrownBy(() -> userService.loginUser(userLoginDTO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testLoginUser_IncorrectPassword() {
        User user = new User();
        user.setEmail("test@ecomm.com");
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("test@ecomm.com");
        userLoginDTO.setPassword("wrongPassword");

        assertThatThrownBy(() -> userService.loginUser(userLoginDTO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Incorrect password");
    }

    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setEmail("test@ecomm.com");
        user.setPassword("encodedPassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("newEncodedPassword");
        when(userRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("new@ecomm.com");
        userRequestDTO.setPassword("newPassword");
        UserResponseDTO userResponseDTO = userService.updateUser(1L, userRequestDTO);

        assertThat(userResponseDTO.getEmail()).isEqualTo("new@ecomm.com");
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("new@ecomm.com");

        assertThatThrownBy(() -> userService.updateUser(1L, userRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void testUpdateUser_EmailExists() {
        User user = new User();
        user.setEmail("test@ecomm.com");
        user.setPassword("encodedPassword");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(any())).thenReturn(true);

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("new@ecomm.com");

        assertThatThrownBy(() -> userService.updateUser(1L, userRequestDTO))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("Email already exists");
    }
}
