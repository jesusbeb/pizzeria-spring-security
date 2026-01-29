package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.OrderEntity;
import com.jbeb.pizza.persistence.projection.OrderSummary;
import com.jbeb.pizza.persistence.repository.OrderRepository;
import com.jbeb.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    // Constantes con el valor que puede tener el atributo method de OrderEntity
    private static final String DELIVERY = "D";
    private static final String CARRYOUT = "C";
    private static final String ON_SITE = "S";

    @Autowired
    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    // Con forEach imprimimos en consola los nombres de los clientes, ya que en la relacion de OrderEntity con
    // Customer se tiene FetchType.LAZY y no trae automaticamente la informacion del Customer
    public List<OrderEntity> getAll() {
        List<OrderEntity> orders = this.orderRepository.findAll();
        orders.forEach(o -> System.out.println(o.getCustomer().getName()));
        return orders;
    }

    public List<OrderEntity> getTodayOrders(){
        LocalDateTime today = LocalDate.now().atTime(0,0); // Crea una fecha actual con la hora 0 y minuto 0
        return this.orderRepository.findAllByDateAfter(today);
    }

    // Metodo para obtener las ordenes que fueron para llevar o entregar en domicilio
    // Se crea un List con las coincidencias a buscar
    public List<OrderEntity> getOutsideOrders(){
        List<String> methods = Arrays.asList(DELIVERY, CARRYOUT);
        return this.orderRepository.findAllByMethodIn(methods);
    }

    // Metodo para obtener las ordenes de un cliente especificado por su id
    public List<OrderEntity> getCustomerOrders(String idCustomer){
        return this.orderRepository.findCustomerOrders(idCustomer);
    }

    // Metodo para obtener un resumen de una orden por su id
    public OrderSummary getSummary(int orderId){
        return this.orderRepository.findSummary(orderId);
    }

    // Metodo que guardara una orden aleatoria
    @Transactional
    public boolean saveRandomOrder(RandomOrderDto randomOrderDto) {
        return this.orderRepository.saveRandomOrder(randomOrderDto.getIdCustomer(), randomOrderDto.getMethod());
    }

}
