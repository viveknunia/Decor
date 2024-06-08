package com.ecomm.dto;

import com.ecomm.entity.User;
import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private String password;

    public static UserRequestDTO convertToDTO(User user) {
        UserRequestDTO userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName(user.getName());
        userRequestDTO.setEmail(user.getEmail());
        userRequestDTO.setAddress(user.getAddress());
        userRequestDTO.setPhoneNumber(user.getPhoneNumber());
        userRequestDTO.setPassword(user.getPassword());
        return userRequestDTO;
    }
}
