package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.UserEntity;
import com.jbeb.pizza.persistence.entity.UserRoleEntity;
import com.jbeb.pizza.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


// Implementamos nuestro UserDetailsService para utilizar usuarios de la BD
@Service
public class UserSecurityService implements UserDetailsService {
    // Inyectamos userRepository
    private final UserRepository userRepository;

    // Constructor
    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Sobreescribimos este metodo por haber implementado UserDatailsService
    // Buscamos el usuario en la BD, si no lo encontramos lanzamos la excepcion
    // Si se encuentra el usuario, obtenemos sus atributos y lo construimos para retornarlo
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = this.userRepository.findById(username)
                .orElseThrow( () -> new UsernameNotFoundException("User " + username + " not found!!!") );

        // Obtenemos la lista de roles del usuario, aplicamos stream, mapeamos para obtener los nombres de sus roles y
        // los agregamos a un nuevo Array de Strings
        String[] roles = userEntity.getRoles().stream().map(UserRoleEntity::getRole).toArray(String[]::new);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                //.roles(roles) // .roles() recibe una lista de Strings
                .authorities(this.grantedAuthorities(roles)) // authorities recibe GrantedAuthority
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }


    // Metodo que retorna un Array de Strings con los permisos especificos para un usuario
    private String[] getAuthorities(String role) {

        // Si el usuario es un ADMIN o CUSTOMER tendra un permiso especifico llamado "random_order"
        if ("ADMIN".equals(role) || "CUSTOMER".equals(role)) {
            return new String[] {"random_order"};
        }

        // Para cualquier otro usuario no contemplado, no asignamos permisos especificos. Retornamos un Array vacio
        return new String[] {};
    }


    // Metodo que recibe los roles del usuario y aparte asigna permisos individuales
    private List<GrantedAuthority> grantedAuthorities( String[] roles ) {
        // Creamos la lista que retornaremos, que sera del tamaño del arreglo que tiene los roles
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);

        // Recorremos el Array para asignar los roles que contiene, a la lista de GrantedAuthority, asignando el prefijo "ROLE_"
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

            // Asignamos los permisos especificos al usuario. Recorremos el arreglo de Strings que retorna el metodo getAuthorities,
            // le enviamos el role del for que ya tenemos y a cada elemento que se recorre lo llamamos authority, el cual
            // agregaremos al List authorities
            for (String authority : this.getAuthorities(role)) {
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }

}



/*
¿Cuál es la diferencia entre Authorities y Roles?
En Spring Security, las Authorities y Roles aunque a primera vista podrían parecer similares, poseen diferencias cruciales:

- Roles: Son grupos de permisos que un usuario puede tener. Un rol puede agrupar varios permisos, facilitando la
gestión de permisos de usuarios que realizan funciones similares.
- Authorities: Son permisos específicos que un usuario puede tener para ejecutar acciones concretas dentro de la
aplicación. A diferencia de los roles, son más granulares y permiten asignar permisos muy específicos.

Spring asigna automáticamente el prefijo "ROLE" para diferenciar un rol de una autoridad.
 */