package com.jbeb.pizza.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Creamos nuestro propio filtro de seguridad
@Component
public class JwtFilter extends OncePerRequestFilter {
    // Al inyectar el UserDetailsService no tenemos que indicar explicitamente que es el UserSecurityService ya que este
    // ultimo lo implementa y es el unico que lo hace en la API, por lo que, gracias al polimorfismo se puede usar la interface
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Constructor
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    // Metodo que valida las peticiones a la API
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Validar que sea un Header Authorization valido. Obtenemos el encabezado
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // si no es valido, hacemos que la cadena de filtros continue su trabajo, ya que no se cargara nada en el contexto de seguridad
            return; // aunque el metodo es void, ponemos return para que aqui termine el metodo en caso de no ser valido
        }

        // 2. Validar que el JWT sea valido
        // Obtenemos el jwt del authHeader. Hacemos split() que divide una cadena de texto en un arreglo de subcadenas,
        // indicamos que un espacio en blanco es la separacion, por lo que la palabra "Bearer" queda en la cadena 0 y entonces
        // tomamos la cadena 1 donde queda el token. Usamos trim() para quitar espacios antes o despues del token que tomamos
        String jwt = authHeader.split(" ")[1].trim();

        // Si el token no es valido
        if (!this.jwtUtil.isValid(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cargar el usuario del UserDetailsService
        // Obtenermos el username del JWT, lo buscamos para cargarlo de la BD y casteamos a User
        String username = this.jwtUtil.getUsername(jwt);
        User user = (User) this.userDetailsService.loadUserByUsername(username);

        // 4. Cargar al usuario en el contexto de seguridad. Para indicar a los demas filtros que este filtro resolvio la
        // peticion de manera correcta en terminos de seguridad
        // Creamos el UsernamePasswordAuthentication
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities()
        );

        // Enviamos el authenticationToken al contexto de seguridad y decimos al filterChain que continue su trabajo,
        // pero esta vez ya con el usuario en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

}
