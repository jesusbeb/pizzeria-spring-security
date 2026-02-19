package com.jbeb.pizza.persistence.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//
@Service
public class AuditUsername implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // Obtenemos la autenticacion del SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Si la autenticacion es nula o no esta autenticado, retorna null. En realidad no deberia entrar en este if, ya
        // que en las reglas de seguridad esta especificado que solo un administrador podria hacer modificaciones y
        // enviar un JWT valido, pero mejor lo validamos
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // Obtenemos el nombre del usuario
        String username = authentication.getPrincipal().toString();

        // Retornamos el opcional del username
        return Optional.of(username);
    }

}
