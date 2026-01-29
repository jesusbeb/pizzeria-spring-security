package com.jbeb.pizza.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Usamos @IdClass cuando en un Entity tenemos un Id o clave primaria compuesta (en este caso por dos Id) e indicamos el
// nombre de la clase que contiene los atributos de dicha clave primaria compuesta
@Entity
@Table(name = "order_item")
@IdClass(OrderItemId.class)
@Getter
@Setter
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    @Column(name = "id_order", nullable = false)
    private Integer idOrder;

    @Id
    @Column(name = "id_item", nullable = false)
    private Integer idItem;

    @Column(name = "id_pizza", nullable = false)
    private Integer idPizza;

    @Column(nullable = false, columnDefinition = "Decimal(2,1)")
    private Double quantity;

    @Column(nullable = false, columnDefinition = "Decimal(5,2)")
    private Double price;


    // Relacion de OrderItemEntity con OrderEntity
    // ManyToOne. Una orden puede tener muchos items
    // Indicamos el nombre de la columna con la que ocurre el Join
    // Con insertable y updatable evitamos que se actualicen elementos en OrderEntity a traves de esta relacion
    // @JsonIgnore evita el error de llamado infinito, ya que en OrderEntity hay una relacion con OrderItemEntity al
    // construir todos los items que tiene un order. El problema esta aqui en OrderItemEntity con la relacion que se
    // tiene con OrderEntity; al construir el Json del item trata al mismo tiempo de construir el Json de Order, y el
    // del Order vuelve y llama al del item, con lo que se vuelve un llamado infinito. Este error se evita creando DTO's
    // o clase de Dominio para no exponer los Entities en un servicio REST, en este caso por temas practicos solo evitaremos el error con JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_order", referencedColumnName = "id_order", insertable = false, updatable = false)
    @JsonIgnore
    private OrderEntity order;

    // Relacion de OrderItemEntity PizzaEntity
    // OneToOne. Un OrderItem solo puede tener una pizza
    @OneToOne
    @JoinColumn(name = "id_pizza", referencedColumnName = "id_pizza", insertable = false, updatable = false)
    private PizzaEntity pizza;

}
