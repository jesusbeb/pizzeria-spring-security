package com.jbeb.pizza.persistence.repository;

import com.jbeb.pizza.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

// Creamos UserRepository que interactuara con la tabla de la BD. Indicamos nombre del Entity y su tipo de clave primaria
public interface UserRepository extends CrudRepository<UserEntity, String> {
}
