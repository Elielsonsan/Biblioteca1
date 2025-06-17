package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository; // Adicionar este import

@Repository // Opcional, mas boa prática
public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTitulo(String titulo);
    // Método para buscar livros por título, ignorando maiúsculas/minúsculas
    // Exemplo: livroRepository.findByTituloContainingIgnoreCase("algum título")
    // Isso permite buscar livros que contenham a string "algum título" em qualquer parte do título
    
    List<Livro> findByTituloContainingIgnoreCase(String keyword);
    // JpaRepository já fornece métodos como findAll(), findById(), save(),
    // deleteById()
    // Você pode adicionar métodos de consulta personalizados aqui se precisar
    // Ex: List<Livro> findByTitulo(String titulo);

    List<Livro> findByCategoriaNome(String nomeCategoria);

    List<Livro> findByAuthor(String author);

    Optional<Livro> findByIsbn(String isbn);

    List<Livro> findByCategoria(Categoria categoria);

    List<Livro> findByAnoPublicacaoGreaterThanEqual(Integer anoMinimo);

    int countByCategoria(Categoria categoria);

    // Métodos relacionados à Estante
    List<Livro> findByEstante(Estante estante);

    List<Livro> findByEstanteNome(String nomeEstante);

    int countByEstante(Estante estante);

    List<Livro> findByAnoPublicacaoGreaterThan(Integer ano);

    // Método para busca geral por título, autor ou ISBN, com EAGER fetching para categoria e estante
    @Query("SELECT l FROM Livro l LEFT JOIN FETCH l.categoria LEFT JOIN FETCH l.estante WHERE " +
           "LOWER(l.titulo) LIKE LOWER(concat('%', :termo, '%')) OR " +
           "LOWER(l.author) LIKE LOWER(concat('%', :termo, '%')) OR " +
           "LOWER(l.isbn) LIKE LOWER(concat('%', :termo, '%'))")
    List<Livro> searchByTermoGeral(@Param("termo") String termo);

}