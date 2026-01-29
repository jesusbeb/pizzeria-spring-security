package com.jbeb.pizza.persistence.repository;

import com.jbeb.pizza.persistence.entity.OrderEntity;
import com.jbeb.pizza.persistence.projection.OrderSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

// Extendemos e indicamos el nombre de la Entidad a trabajar y el tipo de su clave primaria
public interface OrderRepository extends ListCrudRepository<OrderEntity, Integer> {

    // Consultar todas las ordenes despues de una fecha especifica
    List<OrderEntity> findAllByDateAfter(LocalDateTime date);

    // Consultar ordenes por el atributo method que indica si la orden se consumio en el negocio, para llevar o para entrega
    // Como se usa el keyword "In" se debe recibir un List de tipo String que es el tipo del atributo method
    List<OrderEntity> findAllByMethodIn(List<String> methods);

    // @Query para consultas con SQL nativos.
    // Consulta para saber las ordenes que ha tenido un cliente en especifico
    @Query(value = "SELECT * FROM pizza_order WHERE id_customer = :id", nativeQuery = true)
    List<OrderEntity> findCustomerOrders(@Param("id") String idCustomer);

    // SQL nativo que responde al projection que queremos exponer al cliente
    // Utilizamos AS para indicar los nombres de atributos que se usaron en el projection OrderSummary
    @Query(value =
            "SELECT  po.id_order AS idOrder, cu.name AS customerName, po.date AS orderDate," +
                    "        po.total AS orderTotal, GROUP_CONCAT(pi.name) AS pizzaNames " +
                    "FROM   pizza_order po  " +
                    "   INNER JOIN customer cu ON po.id_customer = cu.id_customer  " +
                    "   INNER JOIN order_item oi ON po.id_order = oi.id_order  " +
                    "   INNER JOIN pizza pi ON oi.id_pizza = pi.id_pizza  " +
                    "WHERE  po.id_order = :orderId " +
                    "GROUP BY po.id_order, cu.name, po.date, po.total", nativeQuery = true)
    OrderSummary findSummary(@Param("orderId") int orderId);

    // @Procedure permite declarar y ejecutar un Store Procedure. Este metodo retorna true or false si es que la orden se pudo hacer
    // Supondremos que se tiene una promocion del 20% de descuento para los clientes que se animen a pedir una orden aleatoria.
    // El Store Procedure hara todo el proceso y ya debe estar creado dentro de la BD, tendra el nombre "take_random_pizza_order"
    // "order_taken" es el nombre de la variable del valor de retorno en el Store Procedure (variable booleana)
    @Procedure(value = "take_random_pizza_order", outputParameterName = "order_taken")
    boolean saveRandomOrder(@Param("id_Customer") String idCustomer, @Param("method") String method);

}



/*
Un store procedure es un bloque de código almacenado y reutilizable que permite realizar operaciones complejas en
la base de datos, agrupando múltiples instrucciones SQL. Esto puede incluir selecciones, inserciones, o control de
transacciones, incrementando tanto la eficiencia como la seguridad de las operaciones.
 */