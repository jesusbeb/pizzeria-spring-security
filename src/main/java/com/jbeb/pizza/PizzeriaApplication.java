package com.jbeb.pizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Se habilita el uso de JpaRepositories en la app
// Habilitamos la auditoria de JPA
@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
public class PizzeriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaApplication.class, args);
	}

}
