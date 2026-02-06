package com.jbeb.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

// Configuracion de Cors para que permita a los frontend que estan en otros origenes distintos al de nuestra API, interactuar con ella
// Gracias a esta configuracion no sera necesario usar @CrossOrigin en cada metodo del controlador
@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // Instanciamos un CorsConfiguration
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // indicamos los origenes permitidos
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*")); // Permite todos los headers que vengan atraves de Cors

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // "/**" indica que es para todos los controladores, y la configuracion de cors
        return source;
    }
}




/*
¿Qué es CORS y cómo afecta a tu aplicación?
Cuando trabajamos en proyectos web divididos en frontend y backend, a menudo tratamos con el intercambio de recursos entre
diferentes orígenes. Esto es especialmente común cuando la aplicación frontend se ejecuta desde un dominio y el
backend desde otro. Aquí es donde entra CORS (Cross-Origin Resource Sharing), un sistema crucial para permitir o
restringir tales interacciones por razones de seguridad.

Por defecto, los frameworks como Spring bloquean estas peticiones cruzadas. Esto puede ser un obstáculo si, por ejemplo,
nuestro frontend se ejecuta en localhost:4200 usando Angular, y nuestro API backend corre en localhost:8080.
Afortunadamente, Spring Security ofrece mecanismos para configurar y habilitar CORS, permitiendo así que aplicaciones de
frontend puedan comunicarse con APIs alojadas en diferentes dominios.
 */
