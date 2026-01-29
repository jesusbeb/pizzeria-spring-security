package com.jbeb.pizza.web.controller;

import com.jbeb.pizza.persistence.entity.PizzaEntity;
import com.jbeb.pizza.service.PizzaService;
import com.jbeb.pizza.service.dto.UpdatePizzaPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    // Instanciamos PizzaService y al ser final, se agrega en el constructor
    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    // Endpoint
    // Se reciben como parametros el numero de pagina que se consultara y el numero de elementos a mostrar por pagina
    // @RequestParam porque viajan como parametros de la peticion (p.e. http://localhost:8080/api/pizzas?page=0&elements=15)
    // defaultValue asigna valores por default
    @GetMapping
    public ResponseEntity<Page<PizzaEntity>> getAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int elements){
        return ResponseEntity.ok(this.pizzaService.getAll(page, elements));
    }

    // Endpoint, recibe el id en el path
    @GetMapping("/{idPizza}")
    public ResponseEntity<PizzaEntity> getById(@PathVariable int idPizza){
        return ResponseEntity.ok(this.pizzaService.getById(idPizza));
    }

    // Recibimos el name en otro path para no confundir a Spring
    @GetMapping("/name/{name}")
    public ResponseEntity<PizzaEntity> getByName(@PathVariable String name){
        return ResponseEntity.ok(this.pizzaService.getByName(name));
    }

    @GetMapping("/with/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWith(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getWith(ingredient));
    }

    @GetMapping("/without/{ingredient}")
    public ResponseEntity<List<PizzaEntity>> getWithout(@PathVariable String ingredient){
        return ResponseEntity.ok(this.pizzaService.getWithout(ingredient));
    }

    @GetMapping("/cheapest/{price}")
    public ResponseEntity<List<PizzaEntity>> getCheapestPizzas(@PathVariable double price){
        return ResponseEntity.ok(this.pizzaService.getCheapest(price));
    }

    // http://localhost:8080/api/pizzas/available?sortBy=price&sortDirection=DESC -> Path donde indicamos a traves de
    // que columna se ordenara y si sera ASC o DESC, no enviamos page ni elements, asi que tomara los valores por defecto
    @GetMapping("/available")
    public ResponseEntity<Page<PizzaEntity>> getAvailable(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "8") int elements,
                                                          @RequestParam(defaultValue = "price") String sortBy,
                                                          @RequestParam(defaultValue = "ASC") String sortDirection){
        return ResponseEntity.ok(this.pizzaService.getAvailable(page, elements, sortBy, sortDirection));
    }

    // Endpoint para guardar una pizza que recibe en el cuerpo de la peticion
    @PostMapping
    public ResponseEntity<PizzaEntity> add(@RequestBody PizzaEntity pizza){
        // Si el idPizza es null o la pizza no existe
        if (pizza.getIdPizza() == null || !this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        // En caso de que la pizza ya exista
        return ResponseEntity.badRequest().build();
    }

    // Endpoint para actualizar una pizza
    @PutMapping
    public ResponseEntity<PizzaEntity> update(@RequestBody PizzaEntity pizza){
        // Si el idPizza es diferente de null Y la pizza ya existe
        // Siempre se llama el metodo save, internamente los Spring Repository detectan si es un INSERT de una
        // pizza nueva o se quiere actualizar
        if (pizza.getIdPizza() != null && this.pizzaService.exists(pizza.getIdPizza())){
            return ResponseEntity.ok(this.pizzaService.save(pizza));
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/price")
    public ResponseEntity<Void> updatePrice(@RequestBody UpdatePizzaPriceDto dto){
        if (this.pizzaService.exists(dto.getPizzaId())){
            this.pizzaService.updatePrice(dto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{idPizza}")
    public ResponseEntity<Void> delete(@PathVariable int idPizza){
        if (pizzaService.exists(idPizza)) {
            pizzaService.delete(idPizza);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
