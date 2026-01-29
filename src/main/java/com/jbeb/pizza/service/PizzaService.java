package com.jbeb.pizza.service;

import com.jbeb.pizza.persistence.entity.PizzaEntity;
import com.jbeb.pizza.persistence.repository.PizzaPagSortRepository;
import com.jbeb.pizza.persistence.repository.PizzaRepository;
import com.jbeb.pizza.service.dto.UpdatePizzaPriceDto;
import com.jbeb.pizza.service.exception.EmailApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PizzaService {

    // Como se anoto como final, se agrega como parametro en un constructor
    private final PizzaRepository pizzaRepository;
    private final PizzaPagSortRepository pizzaPagSortRepository;

    //Constructor
    @Autowired
    public PizzaService(PizzaRepository pizzaRepository, PizzaPagSortRepository pizzaPagSortRepository){
        this.pizzaRepository = pizzaRepository;
        this.pizzaPagSortRepository = pizzaPagSortRepository;
    }

    // Metodo para consultar todas las pizzas. Retorna un Page (página)
    // PageRequest.of recibe el numero de pagina que queremos ver y el numero de elementos que se mostraran (las
    // paginas siempre empiezan desde cero)
    public Page<PizzaEntity> getAll(int page, int elements){
        Pageable pageRequest = PageRequest.of(page, elements);
        return this.pizzaPagSortRepository.findAll(pageRequest);
    }

    // Metodo para consultar todas las pizzas disponibles. Retorna un Page
    // Recibe como parametro la pagina a consultar, el numero de elementos a mostrar, a traves de que propiedad se
    // ordenara y si sera ascendente o descendente.
    public Page<PizzaEntity> getAvailable(int page, int elements, String sortBy, String sortDirection){
        System.out.println("Pizzas veganas disponibles: "+this.pizzaRepository.countByVeganTrue()); // Imprimimos en consola

        // sortBy recibe una direccion de ordenamiento (ASC o DESC) y la propiedad a traves de la cual se ordenara
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageRequest = PageRequest.of(page, elements, sort);
        return this.pizzaPagSortRepository.findByAvailableTrue(pageRequest);
    }

    // findById retorna un Optional, por lo que usamos orElse para indicar que retorne null si no encuentra nada
    public PizzaEntity getById(int idPizza){
        return this.pizzaRepository.findById(idPizza).orElse(null);
    }

    public PizzaEntity getByName(String name){
        // Otras formas de retornar un Pizza Entity cuando se recibe un Optional
        // return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElse(null); // retornar un null si no se encuentra nada
        // return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow(); // Lanzar una excepcion
        // Lanzamos una excepcion con programacion funcional e imprimimos en consola el error
        return this.pizzaRepository.findFirstByAvailableTrueAndNameIgnoreCase(name).orElseThrow( () -> new RuntimeException("La pizza no existe") );
    }

    public List<PizzaEntity> getWith(String ingredient){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionContainingIgnoreCase(ingredient);
    }

    public List<PizzaEntity> getWithout(String ingredient){
        return this.pizzaRepository.findAllByAvailableTrueAndDescriptionNotContainingIgnoreCase(ingredient);
    }

    // Metodo que retorna las tres pizzas mas baratas de un precio que se indique
    public List<PizzaEntity> getCheapest(double price) {
        return this.pizzaRepository.findTop3ByAvailableTrueAndPriceLessThanEqualOrderByPriceAsc(price);
    }

    public PizzaEntity save(PizzaEntity pizza){
        return this.pizzaRepository.save(pizza);
    }

    public void delete(int idPizza){
        this.pizzaRepository.deleteById(idPizza);
    }

    // @Transactional permite garantizar las cuatro caracteristicas ACID. Es muy importante que siempre que se tengan
    // dos o mas llamados a la BD desde un mismo metodo, si o si usar @Transactional
    // Supongamos que cuando quieramos actualizar el precio de la pizza, se envie un correo al cliente para informarle el cambio de precio
    // Hacemos que el metodo que envia el email provoque un error. De esta manera con la anotacion @Transactional se
    // hace primero el cambio de precio, pero como el email no se envio, se hace rollback para regresar a su precio original la pizza
    // Si a @Transactional le indicamos noRollbackFor y expecificamos la clase que maneja la excepcion, entonces no hara
    // rollback cuando ocurra esa excepcion, pero si lo hara cuando ocurra alguna otra excepcion. Por lo tanto si se hara el cambio de precio
    // Propagation.REQUIRED significa que debe existir una transaccion para poder ejecutar el metodo, si no existe, la creara automaticamente
    @Transactional(noRollbackFor = EmailApiException.class,
    propagation = Propagation.REQUIRED)
    public void updatePrice(UpdatePizzaPriceDto dto){
        this.pizzaRepository.updatePrice(dto.getPizzaId(), dto.getNewPrice());
        this.sendEmail();
       //this.pizzaRepository.updatePrice(updatePizzaPriceDto); // se envia el dto en caso de usar el metodo que usa Spring Expression Language
    }

    // Metodo solo para lanzar una excepcion. Provoca un error intencional
    private void sendEmail(){
        throw new EmailApiException();
    }

    // Metodo para comprobar si una pizza ya existe
    public boolean exists(int idPizza){
        return this.pizzaRepository.existsById(idPizza);
    }

}



/*
¿Qué es ACID y por qué es vital en las transacciones de bases de datos?

Cuando trabajamos con bases de datos en nuestras aplicaciones, la integridad y confiabilidad de las transacciones son
esenciales para asegurar que la información almacenada esté siempre precisa y segura. ACID es un conjunto de
cuatro propiedades que toda transacción debe cumplir para garantizar su confiabilidad.

- Atomicidad: Esta propiedad asegura que las transacciones sean "todo o nada". Si un error ocurre durante la ejecución,
              se realiza un rollback para restaurar el estado inicial.

- Consistencia: Valida que las transacciones solo realicen operaciones que mantienen la integridad de la
                información. Esto garantiza que las bases de datos no se corrompan con datos inválidos.

- Aislamiento: Garantiza que las transacciones se ejecuten de manera independiente, evitando que los datos se
               mezclen entre operaciones concurrentes.

- Durabilidad: Asegura que la información persista a lo largo del tiempo, incluso si la base de datos se apaga y se
               vuelve a encender.

 ¿Cómo implementa Spring Data JPA ACID con @Transactional?
Spring Data JPA proporciona la anotación @Transactional para manejar las propiedades ACID en transacciones que
involucran múltiples operaciones de base de datos.

Al anotar un método con @Transactional, se asegura que las transacciones sean atómicas, aisladas, durables y
consistentes.
Es vital usar @Transactional cuando se hacen múltiples llamados a la base de datos desde un mismo método, ya que
asegura que las operaciones se ejecuten de manera integral.
 */