package com.jbeb.pizza.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.CrossOrigin;

// Implementamos y configuramos nuestro propio SecurityFilterChain
// Esta clase tendra toda la configuracion de seguridad del proyecto
@Configuration
public class SecurityConfig {

    // Metodo
    // Al final retornamos la configuracion que realizamos. build() puede lanzar una excepcion en la firma del metodo
    // Se deshabilita csrf ya que la API Rest, mas adelante se configurara para que no almacene ninguna sesion y no
    // tenga estado (Stateless + JWT) y su sistema de autenticacion se basara con tokens (JSON Web Tokens). La combinacion entre
    // Api Stateless y JWT funcionara siempre y cuando no se utilice una autenticacion basada en cookies, lo cual es un
    // riesgo de seguridad.
    // Con requestMatchers creamos una regla para un tipo de peticion y enviamos a que path aplica. Permitimos, denegamos, autenticamos,...
    // Con un * indicamos que aplica para el primer nivel en la siguiente subruta. Con ** indicamos que aplica a todos los niveles de subrutas
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults()) // activamos cors
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth // autoriza las peticiones http
                        .requestMatchers(HttpMethod.GET, "/api/pizzas/**").hasAnyRole("ADMIN", "CUSTOMER") // ADMIN y CUSTOMER pueden hacer GET en el path indicado
                        .requestMatchers(HttpMethod.POST, "/api/pizzas/**").hasRole("ADMIN") // solo ADMIN puede hacer POST en el path indicado
                        .requestMatchers(HttpMethod.PUT).hasRole("ADMIN") // Solo ADMIN puede hacer PUT en cualquier parte de la API
                        .requestMatchers("/api/orders/**").hasRole("ADMIN") // Solo ADMIN puede hacer todos los metodos en el path indicado
                        .anyRequest().authenticated() // cualquier peticion (que no aplique a las configuraciones previas de requestMatchers) necesita estar autenticada
                )
                .httpBasic(Customizer.withDefaults()); // se autenticara con httpBasic

        return http.build();
    }


    // Como actualmente se estan utilizando usuarios de la BD, deshabilitamos este metodo
//    // Hacemos una implementacion de UserDatilsService para crear nuestros propios usuarios en memoria
//    // Con @Bean, Spring reconocera que estamos usando usuarios propios y dejara de generar el usuario y la contraseña por defecto
//    // En .password indicamos el encoder que encripta la contraseña, ya que Spring lo pide
//    @Bean
//    public UserDetailsService memoryUsers(){
//        // Creamos un usuario
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        // Usuario customer
//        UserDetails customer = User.builder()
//                .username("customer")
//                .password(passwordEncoder().encode("customer123"))
//                .roles("CUSTOMER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, customer);
//}


    // Usamos BCryptPasswordEncoder que Spring provee para encriptar las contraseñas
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}




/*
¿Qué es Spring Security y cómo funciona su autenticación básica?
Spring Security es una poderosa herramienta que agrega una capa de seguridad a nuestras aplicaciones, protegiéndolas contra
accesos no autorizados. Al agregar la dependencia de Spring Security a tu proyecto, se habilita una configuración de
seguridad por defecto. Esta configuración genera automáticamente un usuario (user) y una contraseña genérica (visible en
consola) que puedes utilizar para acceder a los servicios de tu aplicación de forma segura durante el desarrollo.


¿Qué es un ataque CSRF y por qué es peligroso?
Los ataques CSRF, o Cross-Site Request Forgery, son una vulnerabilidad web donde un atacante envia solicitudes hábilmente disfrazadas de
un usuario autorizado. Estos ataques aprovechan el hecho de que los navegadores web envían automáticamente información de
sesión guardada, como cookies, en cada solicitud a un dominio específico.

Ejemplo de ataque CSRF en acción
Un escenario común de un ataque CSRF podría ser el de una sesión bancaria en línea. Supongamos que iniciaste sesión en
tu banco y tienes un formulario de transferencia bancaria que usa un método POST para enviar datos cuando decides
transferir dinero. Mientras tienes esa sesión abierta, visitas otro sitio malintencionado que, al hacer clic en un
botón que aparentemente reproduce videos, realiza una solicitud de transferencia con tus credenciales al banco,
redirigiendo fondos de tu cuenta a la cuenta del atacante.

¿Cómo sucede esto? Básicamente, el botón en el sitio malintencionado ejecuta un formulario similar en segundo plano hacia la
URL del banco, enviando los datos de la transferencia fraudulenta, que el banco procesa como legítimos debido a que las
cookies válidas de sesión se enviaron automáticamente con la solicitud.

¿Cómo prevenir ataques CSRF?
Para evitar la explotación de CSRF, se utiliza comúnmente un token de seguridad único y aleatorio. Este token se
envía junto con los datos del formulario y el servidor lo valida, permitiendo solo las solicitudes con un token válido,
asegurando su origen legítimo. De esta manera, se previene que un sitio no autorizado realice solicitudes en tu nombre,
ya que no puede suministrar un token válido.

Solución utilizando tokens CSRF
La implementación de tokens CSRF generalmente implica:
- Generación de Tokens: El servidor genera un token único para cada sesión o solicitud.
- Validación del Token: Cuando el usuario envía una solicitud, el token debe ser incorporado como un campo oculto en el
formulario. El servidor valida si el token coincide antes de procesar la solicitud.

Deshabilitando CSRF en APIs RESTful
Dicho esto, puedes estar preguntándote por qué se describe cómo deshabilitar esta protección en una clase. La razón se
debe a la naturaleza sin estado (stateless) de las APIs RESTful modernas, generalmente orientadas a mejorar el
rendimiento y escalabilidad utilizando tokens de seguridad en headers HTTP, como los JSON Web Tokens (JWT).

Configuración para una API sin estado

En un contexto de API Stateless, las cookies no se utilizan para mantener el estado de autenticación del usuario. En su
lugar, cada solicitud incluye un token de autenticación en el cabezal HTTP. Así, podrías prescindir de la protección CSRF si
cumples los siguientes principios:
- Autenticación basada en tokens: Empleando sistemas de seguridad JWT, los tokens se agregan al header HTTP Authorization para
autenticar cada solicitud.
- Request Headers en lugar de Cookies: Las solicitudes se autentican mediante headers en lugar de cookies, eliminando uno
de los vectores comunes de ataque CSRF.

 */