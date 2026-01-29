package com.jbeb.pizza.persistence.audit;

import com.jbeb.pizza.persistence.entity.PizzaEntity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import org.springframework.util.SerializationUtils;

// Listener para auditar el EntityPizza
// Este tipo de auditoria solo funcionara si se usan los metodos del ciclo de vida de los JPA Data Repository, con los
// Query nativos no funcionara porque al hacerse de forma nativa no pasan por el ciclo de vida de los Entity.
public class AuditPizzaListener {

    private PizzaEntity currentValue;

    // Metodo que se ejecuta despues de recibir un SELECT y se cargue un Entity de la BD
    // Asignamos a currentValue un clone del Entity que se esta cargando. Para hacer esto tenemos que hacer serializable al Entity
    // Si solo hicieramos this.currentValue = entity, por como se comporta la memoria de Java al cargar estos objetos,
    // sobrecargariamos la posicion en memoria al ejecutarse los siguientes metodos de esta clase y no veriamos los cambios
    @PostLoad
    public void postLoad(PizzaEntity entity) {
        System.out.println("POST LOAD!!!");
        this.currentValue = SerializationUtils.clone(entity);
    }

    // Metodo que se ejecuta despues de actualizar una pizza
    // @PostPersist solo se puede aplicar a un metodo que no retorne nada y debe ser public
    @PostPersist
    @PostUpdate
    public void onPostPersist(PizzaEntity entity) {
        System.out.println("POST PERSIST OR UPDATE!!!");
        System.out.println("OLD VALUE: " +this.currentValue); //.toString()); // no aplicamos toString a currentValue cuando se cree un registro nuevo en la BD, ya que se crearia un NullPointerException
        System.out.println("NEW VALUE: " +entity.toString());
    }

    // Metodo que se ejecuta antes de eliminar una pizza
    @PreRemove
    public void onPreDelete(PizzaEntity entity) {
        System.out.println(entity.toString());
    }

}
