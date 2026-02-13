package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.UserEntity;
import com.jbeb.pizza.persistence.entity.UserRoleEntity;
import com.jbeb.pizza.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


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
                .roles(roles) // .roles() recibe una lista de Strings
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

}
