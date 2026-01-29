package com.jbeb.pizza.service.dto;

import lombok.Data;

// Dto que recibe los datos para una orden aleatoria
@Data
public class RandomOrderDto {
    private String idCustomer;
    private String method;

}
