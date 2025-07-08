package org.iftm.biblioteca.repository;

import java.util.List;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>, JpaSpecificationExecutor<Livro> {

    // --- Métodos para validação e regras de negócio ---
    Optional<Livro> findByIsbn(String isbn);
    // Verifica se já existe um livro com o mesmo ISBN, ignorando o livro com o ID atual (útil para atualizações)
    Optional<Livro> findByIsbnAndIdNot(String isbn, Long id);
    long countByCategoria(Categoria categoria);
    long countByEstante(Estante estante);

    // --- Métodos para sugestões de autocomplete (limitados para performance) ---
    @Query("SELECT DISTINCT l.titulo FROM Livro l WHERE LOWER(l.titulo) LIKE LOWER(concat('%', :termo, '%')) ORDER BY l.titulo")
    List<String> findTitulosParaSugestao(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT DISTINCT l.autor FROM Livro l WHERE LOWER(l.autor) LIKE LOWER(concat('%', :termo, '%')) ORDER BY l.autor")
    List<String> findAutoresParaSugestao(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT DISTINCT l.isbn FROM Livro l WHERE LOWER(l.isbn) LIKE LOWER(concat('%', :termo, '%')) ORDER BY l.isbn")
    List<String> findIsbnsParaSugestao(@Param("termo") String termo, Pageable pageable);

    // Adicionado para buscar nomes de categorias para o autocomplete
    @Query("SELECT DISTINCT c.nome FROM Livro l JOIN l.categoria c WHERE LOWER(c.nome) LIKE LOWER(concat('%', :termo, '%')) ORDER BY c.nome")
    List<String> findNomesDeCategoriaParaSugestao(@Param("termo") String termo, Pageable pageable);

    // Adicionado para buscar nomes de estantes para o autocomplete
    @Query("SELECT DISTINCT e.nome FROM Livro l JOIN l.estante e WHERE LOWER(e.nome) LIKE LOWER(concat('%', :termo, '%')) ORDER BY e.nome")
    List<String> findNomesDeEstanteParaSugestao(@Param("termo") String termo, Pageable pageable);

    // --- Método para encontrar livros disponíveis para empréstimo ---
    @Query("SELECT l FROM Livro l WHERE " +
           "l.id NOT IN (SELECT e.livro.id FROM Emprestimo e WHERE e.dataDevolucao IS NULL) AND " +
           "(" +
           "  LOWER(l.titulo) LIKE LOWER(concat('%', :termo, '%')) OR " +
           "  LOWER(l.autor) LIKE LOWER(concat('%', :termo, '%')) OR " +
           "  LOWER(l.isbn) LIKE LOWER(concat('%', :termo, '%'))" +
           ")")
    Page<Livro> findAvailableByTerm(@Param("termo") String termo, Pageable pageable);
}