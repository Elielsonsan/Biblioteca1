package org.iftm.biblioteca;

import java.util.List;

import org.iftm.biblioteca.entities.Livro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApplication.class, args);
	}

	public Livro create(Livro livro) {
		
		throw new UnsupportedOperationException("Unimplemented method 'create'");
	}

	public Livro findById(Long id) {
		
		throw new UnsupportedOperationException("Unimplemented method 'findById'");
	}

	public Livro update(Long id, Livro livro) {
		
		throw new UnsupportedOperationException("Unimplemented method 'update'");
	}

    public void delete(Long id) {
        
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public List<Livro> findAll() {
        
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

}
