package com.ecomm.service.impl;

import com.ecomm.dto.UserRequestDTO;
import com.ecomm.dto.UserLoginDTO;
import com.ecomm.dto.UserResponseDTO;
import com.ecomm.entity.User;
import com.ecomm.exception.AuthenticationException;
import com.ecomm.exception.ResourceNotFoundException;
import com.ecomm.repository.UserRepository;
import com.ecomm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new AuthenticationException("Email already exists");
        }
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setAddress(userRequestDTO.getAddress());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        return UserResponseDTO.convertToDTO(user);
    }

    @Override
    public UserResponseDTO loginUser(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new AuthenticationException("User not found"));
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }
        return UserResponseDTO.convertToDTO(user);
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (userRequestDTO.getName() != null) {
            existingUser.setName(userRequestDTO.getName());
        }
        if (userRequestDTO.getEmail() != null) {
            if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
                throw new AuthenticationException("Email already exists");
            }
            existingUser.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getAddress() != null) {
            existingUser.setAddress(userRequestDTO.getAddress());
        }
        if (userRequestDTO.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userRequestDTO.getPhoneNumber());
        }
        if (userRequestDTO.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        userRepository.save(existingUser);
        return UserResponseDTO.convertToDTO(existingUser);
    }
}
