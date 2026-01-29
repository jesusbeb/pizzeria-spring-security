package com.jbeb.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Implementamos y configuramos nuestro propio SecurityFilterChain
// Esta clase tendra toda la configuracion de seguridad del proyecto
@Configuration
public class SecurityConfig {

    // Metodo
    // Al final retornamos la configuracion que realizamos. build() puede lanzar una excepcion en la firma del metodo
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth // autoriza las peticiones http
                        .anyRequest().authenticated() // cualquier peticion necesitar estar autenticada
                )
                .httpBasic(Customizer.withDefaults()); // se autenticara con httpBasic

        return http.build();
    }

}




/*
¿Qué es Spring Security y cómo funciona su autenticación básica?
Spring Security es una poderosa herramienta que agrega una capa de seguridad a nuestras aplicaciones, protegiéndolas contra
accesos no autorizados. Al agregar la dependencia de Spring Security a tu proyecto, se habilita una configuración de
seguridad por defecto. Esta configuración genera automáticamente un usuario (user) y una contraseña genérica (visible en
consola) que puedes utilizar para acceder a los servicios de tu aplicación de forma segura durante el desarrollo.


 */