package com.jbeb.pizza.persistence.projection;

import java.time.LocalDateTime;

// Projection o DTO. Para exponer un resumen de una orden
// Definimos los atributos que queremos tener en nuestro Query personalizado.
// Como una interface siempre debe expresarse en forma de metodos, nombramos los atributos como metodos get
public interface OrderSummary {
    Integer getIdOrder();
    String getCustomerName();
    LocalDateTime getOrderDate();
    Double getOrderTotal();
    String getPizzaNames();
}



/*
¿Qué son las projections y cómo funcionan en Java?

Las projections son DTOs (Data Transfer Objects) que nos permiten definir una estructura personalizada para
recuperar datos específicos de una base de datos. Son especialmente útiles cuando requerimos construir
consultas complejas que no se ajustan completamente a los campos de una sola tabla.

Imagina que en un proyecto de una Pizzería, necesitas detalles de una orden que involucran varias tablas: el
identificador de la orden, la fecha, y el total están en "PizzaOrder," pero para el nombre del cliente consultarías la
tabla "Customer," y para los nombres de las pizzas usarías la tabla "Pizza." Las projections ofrecen una
forma eficiente de manejar estos casos.

 */