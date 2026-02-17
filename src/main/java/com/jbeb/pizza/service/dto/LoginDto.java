package com.jbeb.pizza.service.dto;

import lombok.Data;

// Dto que recibe el usuario y contraseña para logearse
@Data
public class LoginDto {
    private String username;
    private String password;
}
