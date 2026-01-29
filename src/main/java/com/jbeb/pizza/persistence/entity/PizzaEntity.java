package com.jbeb.pizza.persistence.entity;

import com.jbeb.pizza.persistence.audit.AuditPizzaListener;
import com.jbeb.pizza.persistence.audit.AuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

// @EntityListeners para indicar que pondremos Listeners en este Entity y el nombre de los Listener.class. De esta manera
// el Entity podra ser auditado. Extenderemos de la superclase creada AuditableEntity
@Entity
@Table(name = "pizza")
@EntityListeners({AuditingEntityListener.class, AuditPizzaListener.class})
@Getter // Lombok crea automaticamente los getters
@Setter // Lombok crea automaticamente los setters
@NoArgsConstructor // Lombok crea automaticamente un constructor sin parametros
public class PizzaEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY aumenta de a 1
    @Column(name = "id_pizza", nullable = false)
    private Integer idPizza;

    @Column(nullable = false, length = 30, unique = true)
    private String name;

    @Column(nullable = false, length = 150)
    private String description;

    @Column(nullable = false, columnDefinition = "Decimal(5,2)") // de 5 numeros y 2 de ellos son decimales
    private Double price;

    @Column(columnDefinition = "TINYINT")
    private Boolean vegetarian;

    @Column(columnDefinition = "TINYINT")
    private Boolean vegan;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Boolean available;

    // Implementamos toString para poder imprimir informacion del Entity
    @Override
    public String toString() {
        return "PizzaEntity{" +
                "idPizza=" + idPizza +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", vegetarian=" + vegetarian +
                ", vegan=" + vegan +
                ", available=" + available +
                '}';
    }

}
