package com.ecomm.service;

import com.ecomm.dto.UserRequestDTO;
import com.ecomm.dto.UserLoginDTO;
import com.ecomm.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO);
    UserResponseDTO loginUser(UserLoginDTO userLoginDTO);
    UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO);
}
