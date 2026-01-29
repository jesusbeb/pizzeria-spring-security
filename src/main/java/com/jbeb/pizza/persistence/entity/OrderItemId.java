package com.jbeb.pizza.persistence.entity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

// Clase que contiene los atributos de la clave primaria compuesta
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemId implements Serializable {

    private Integer idItem;
    private Integer idOrder;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof OrderItemId that)) return false;
        return Objects.equals(idOrder, that.idOrder) && Objects.equals(idItem, that.idItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idItem);
    }

}
