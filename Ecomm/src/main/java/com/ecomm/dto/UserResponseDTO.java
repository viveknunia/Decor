package com.ecomm.dto;

import com.ecomm.entity.User;
import com.ecomm.enums.Role;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phoneNumber;
    private Date dateRegistered;
    private Role role;

    public static UserResponseDTO convertToDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setPhoneNumber(user.getPhoneNumber());
        userResponseDTO.setDateRegistered(user.getDateRegistered());
        userResponseDTO.setRole(user.getRole());
        return userResponseDTO;
    }
}
