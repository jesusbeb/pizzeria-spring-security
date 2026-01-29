package com.jbeb.pizza.persistence.repository;

import com.jbeb.pizza.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends ListCrudRepository<CustomerEntity, String> {

    // SELECT c (resultado) FROM CustomerEntity (Entity que recibe el alias "c") DONDE c.phoneNumber es igual a :phone
    // que es un parametro que se indica con dos puntos y el nombre exacto como se recibe
    // Esta consulta devuelve un CustomerEntity, recibe un parametro que se anota con @Param y se le indica siempre el
    // nombre del parametro sin importar si se llama igual
    @Query(value = "SELECT c FROM CustomerEntity c WHERE c.phoneNumber = :phone")
    CustomerEntity findByPhone(@Param("phone") String phone);

}



/*
¿Qué es JPQL y cómo utilizarlo?

JPQL, o Java Persistent Query Language, es un lenguaje que se utiliza para realizar consultas sobre una base de
datos desde las Entities en vez de las tablas tradicionales. Esto permite trabajar de manera más intuitiva para los
desarrolladores en Java, ya que se utilizan atributos de objetos en lugar de columnas y tablas.
 */