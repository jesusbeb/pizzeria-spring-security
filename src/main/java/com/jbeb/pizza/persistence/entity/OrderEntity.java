package com.jbeb.pizza.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pizza_order")
@Getter
@Setter
@NoArgsConstructor
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order", nullable = false)
    private Integer idOrder;

    @Column(name = "id_customer", nullable = false, length = 15)
    private String idCustomer;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime date;

    @Column(nullable = false, columnDefinition = "DECIMAL(6,2)")
    private Double total;

    // CHAR(1) para especificar que en la BD se cree de tamaño maximo 256
    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private String method;

    @Column(name = "additional_notes", length = 200)
    private String additionalNotes;


    // Relacion de OrderEntity con CustomerEntity
    // OneToOne. Una orden solo tiene un cliente
    // JsonIgnore hace que no se muestre la informacion del cliente al consultar una orden, sin embargo internamente la
    // consulta a la BD se sigue haciendo
    // FetchType.LAZY hace que no cargue los datos de esta relacion (informacion del cliente) hasta que se use, por lo
    // que no ya no se hace la consulta a la BD
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_customer", referencedColumnName = "id_customer", insertable = false, updatable = false)
    @JsonIgnore
    private CustomerEntity customer;

    // Relacion de OrderEntity con OrderItemEntity
    // OneToMany. Muchos item pueden estar en una sola orden. Por lo que se usa un List de OrderItemEntity
    // mappedBy se indica el nombre del atributo donde esta la otra relacion ManyToOne con este Entity
    // FetchType.EAGER hara que cuando se quiera recuperar un OrderEntity, automaticamente traiga esta relacion
    // @OrderBy ordenara los items de un Order en forma ASC o DESC segun el atributo de OrderItemEntity que se le indique
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @OrderBy("price ASC")
    private List<OrderItemEntity> items;

}
