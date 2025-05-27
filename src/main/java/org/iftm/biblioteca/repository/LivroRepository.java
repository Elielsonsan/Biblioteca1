package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Opcional, mas boa prática
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByTituloContainingIgnoreCase(String keyword);
    // JpaRepository já fornece métodos como findAll(), findById(), save(), deleteById()
    // Você pode adicionar métodos de consulta personalizados aqui se precisar
    // Ex: List<Livro> findByTitulo(String titulo);

    List<Livro> findByCategoriaNome(String nomeCategoria);

    List<Livro> findByAutor(String autor);

    Optional<Livro> findByIsbn(String isbn);

    List<Livro> findByCategoria(Categoria categoria);

    List<Livro> findByAnoPublicacaoGreaterThanEqual(Integer anoMinimo);

    int countByCategoria(Categoria categoria);
}