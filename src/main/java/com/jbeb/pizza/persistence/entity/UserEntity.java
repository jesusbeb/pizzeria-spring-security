package com.jbeb.pizza.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(length = 50)
    private String email;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean locked;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Boolean disabled;

}



/*
Este Entity creara automaticamente la tabla en la BD gracias a la propiedad "spring.jpa.hibernate.ddl-auto=update" que
esta en application.properties. Despues podemos consultar la tabla en la BD y en la tabla de resultados vacia que nos
muestra, agregar directamente ahi los usuarios. La contraseña no la agregamos en texto plano, sino que vamos a
https://bcrypt.online/  y ahi la encriptamos para agregarla. Finalmente damos clic en Apply para generar el codigo SQL que
agregara los registros
* */