package org.iftm.biblioteca;

import org.iftm.biblioteca.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Classe principal que inicia a aplicação Spring Boot do sistema de Biblioteca.
 */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class) // Explicitamente habilita e registra a classe de propriedades
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

}