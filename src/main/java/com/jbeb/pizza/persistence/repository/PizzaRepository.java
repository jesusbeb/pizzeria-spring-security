package com.jbeb.pizza.persistence.repository;

import com.jbeb.pizza.persistence.entity.PizzaEntity;
import com.jbeb.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// Extendemos y se le indica la Entidad y el tipo de dato de su clave primaria
public interface PizzaRepository extends ListCrudRepository<PizzaEntity, Integer> {

    // Consulta todas las pizzas que estan disponibles y las ordena por precio
    List<PizzaEntity> findAllByAvailableTrueOrderByPrice();

    // Consulta una pizza por nombre y que este disponible
    Optional<PizzaEntity> findFirstByAvailableTrueAndNameIgnoreCase(String name);

    // Consulta todas las pizzas con un ingrediente especificado
    List<PizzaEntity> findAllByAvailableTrueAndDescriptionContainingIgnoreCase(String description);

    List<PizzaEntity> findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(String description);

    List<PizzaEntity> findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(double price);

    // Consulta que cuenta el numero de pizzas veganas y retonar un entero
    int countByVeganTrue();

    // Metodo para actualizar el precio de una pizza
    // @Modifying sirve para que los @Query puedan hacer INSERT, UPDATE o DELETE, si no solo podran hacer SELECT
    @Query(value = """
            UPDATE pizza
            SET price = :newPrice
            WHERE id_pizza = :idPizza
            """, nativeQuery = true)
    @Modifying
    void updatePrice( @Param("idPizza") int id, @Param("newPrice") double newPrice );

    // Otra forma de actualizar el precio de una pizza recibiendo como parametro el DTO y accediendo a sus
    // atributos desde la consulta SQL, mediante Spring Expression Language
    // #{#nombreParametro.nombreAtributoDto}
//    @Query(value = """
//            UPDATE pizza
//            SET price = :#{#newPizzaPrice.newPrice}
//            WHERE id_pizza = :#{#newPizzaPrice.pizzaId}
//            """, nativeQuery = true)
//    @Modifying
//    void updatePrice(@Param("newPizzaPrice")UpdatePizzaPriceDto newPizzaPrice);

}



/*
¿Qué es el Spring Expression Language (SPEL)?

SPEL es un poderoso lenguaje que permite acceder a propiedades de objetos complejos de manera sencilla dentro de
consultas @Query. Esto significa que puedes usar un único parámetro, en este caso, el DTO, y acceder a sus
propiedades internas mediante expresiones.
 */
