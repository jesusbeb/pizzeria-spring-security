package com.jbeb.pizza.service.dto;

import lombok.Data;

// DTO para recibir los datos necesarios para actualizar el precio de una pizza
// @Data para que automaticamente cree todos los getter, setter y constructores
@Data
public class UpdatePizzaPriceDto {
    private int pizzaId;
    private double newPrice;

}
