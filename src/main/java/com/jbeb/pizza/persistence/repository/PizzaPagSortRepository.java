package com.jbeb.pizza.persistence.repository;

import com.jbeb.pizza.persistence.entity.PizzaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface PizzaPagSortRepository extends ListPagingAndSortingRepository<PizzaEntity, Integer> {

    // Consulta que retorna una pagina con todas las pizzas disponibles. findBy funciona igual que findAllBy
    Page<PizzaEntity> findByAvailableTrue(Pageable pageable);

}



/*
Existen diferentes tipos de Spring Data Repositories

¿Qué es el Paging and Sorting Repository en Spring Data?

El Paging and Sorting Repository es una herramienta poderosa dentro de Spring Data que permite gestionar y
estructurar grandes volúmenes de datos, haciendo posible paginar y organizar consultas de manera eficiente. Es
especialmente útil cuando se trabaja con grandes cantidades de información o simplemente cuando se quiere
presentar datos de forma más manejable y accesible.
 */