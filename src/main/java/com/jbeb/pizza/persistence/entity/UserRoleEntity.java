package com.jbeb.pizza.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Entity para la Taba de roles existentes
@Entity
@Table(name = "user_role")
@IdClass(UserRoleId.class) // Como se tiene clave primaria compuesta se crea la classe indicada
@Getter
@Setter
@NoArgsConstructor
public class UserRoleEntity {

    @Id
    @Column(nullable = false, length = 20)
    private String username;

    @Id
    @Column(nullable = false, length = 20)
    private String role;

    @Column(name = "granted_date", nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime grantedDate; // Para saber desde que fecha el usuario tiene un permiso determinado

    // Mucho roles puede tener UN usuario
    // @JoinColumn indica que este Entity contiene la llave foranea, es dueña de la relacion.
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", insertable = false)
    private UserEntity user;

}



/*
Ya que se crea la tabla en la BD, la consultamos para que nos muestre la tabla vacia y ahi agregamos los roles y a que
usuario aplicaran.
Ejemplo:
role        username        granted_name
ADMIN       admin           1
CUSTOMER    customer        1

Aplicamos los cambios para que nos muestre el codigo SQL y ahi cambiamos el '1' por la funcion NOW() que insertara la
fecha actual
 */