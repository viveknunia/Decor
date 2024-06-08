package com.ecomm.entity;

import com.ecomm.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_registered")
    private Date dateRegistered = new Date();

    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;
}
