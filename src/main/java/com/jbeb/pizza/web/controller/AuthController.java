package com.jbeb.pizza.web.controller;

import com.jbeb.pizza.service.dto.LoginDto;
import com.jbeb.pizza.web.config.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Inyectamos AuthenticationManager y JwtUtil
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // Constructor
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    // Metodo para logearse
    // Se crea un UsernamePasswordAuthenticationToken y le enviamos el usuario y la contraseña
    // Autenticamos el usuario con el authenticationManager (el cual a su vez llama al Authentication Provider (DaoAuthentication),
    // el cual igual llama a User Details Service (UserSecurityService que implementamos) que consulta los usuarios de la BD)
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(login);

        // Si llega a estas lineas es porque se autentico correctamente. Imprimimos en consola para verificar el usuario logueado
        System.out.println(authentication.isAuthenticated());
        System.out.println(authentication.getPrincipal());

        // Creamos el JWT que se asignara al usuario
        String jwt = this.jwtUtil.create(loginDto.getUsername());

        // Retornamos status 200 con encabezado AUTHORIZATION y el JWT
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).build();
    }
}
