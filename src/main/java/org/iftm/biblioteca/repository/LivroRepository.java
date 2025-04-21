package org.iftm.biblioteca.repository;

import org.iftm.biblioteca.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Opcional, mas boa prática
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // JpaRepository já fornece métodos como findAll(), findById(), save(), deleteById()
    // Você pode adicionar métodos de consulta personalizados aqui se precisar
    // Ex: List<Livro> findByTitulo(String titulo);
}