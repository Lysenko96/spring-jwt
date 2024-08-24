package com.example.springjwt.dto;

import com.example.springjwt.entity.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationUserDto {

    String username;
    String password;
    String confirmPassword;
    String email;
    List<Role> roles;
}
