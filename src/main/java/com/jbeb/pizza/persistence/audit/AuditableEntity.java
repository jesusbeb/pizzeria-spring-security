package com.jbeb.pizza.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

// Superclase de la que podran extender cualquier Entity para ser auditada
@MappedSuperclass
public class AuditableEntity {

    // Atributos (columnas) que seran agregados a cualquier Entity (tabla) que extienda de esta clase
    // Estos atributos no quedaran expuestos al cliente en la API desde el Entity que herede, pero persistiran en la BD
    // @CreatedDate para que automaticamente se inserte la fecha de creacion
    @Column(name = "created_date")
    @CreatedDate
    private LocalDateTime createdDate;

    // @LastModifiedDate para que automaticamente se inserte la fecha de la ultima modificacion
    @Column(name = "modify_date")
    @LastModifiedDate
    private LocalDateTime modifyDate;

}
