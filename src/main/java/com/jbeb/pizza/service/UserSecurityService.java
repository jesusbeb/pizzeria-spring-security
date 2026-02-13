package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.UserEntity;
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
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles("ADMIN")
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

}
