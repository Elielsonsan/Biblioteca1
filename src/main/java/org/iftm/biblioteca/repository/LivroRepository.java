package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // --- Métodos para validação e regras de negócio ---
    Optional<Livro> findByIsbn(String isbn);
    long countByCategoria(Categoria categoria);
    long countByEstante(Estante estante);
    
    // --- Métodos de busca não paginados (podem ser usados em outras partes do sistema) ---
    List<Livro> findByAnoPublicacaoGreaterThan(Integer ano);
    List<Livro> findByCategoria(Categoria categoria);
    List<Livro> findByAutor(String autor); // Usado no Teste
    List<Livro> findByCategoriaNome(String nomeCategoria); // Usado no Teste
    List<Livro> findByTituloContainingIgnoreCase(String trechoTitulo);
    List<Livro> findByAutorContainingIgnoreCase(String autor);
    List<Livro> findByIsbnContainingIgnoreCase(String isbn);
    List<Livro> findByCategoriaNomeContainingIgnoreCase(String nomeCategoria);
    List<Livro> findByEstanteNomeContainingIgnoreCase(String nomeEstante);

    // --- Métodos de busca paginados para o novo endpoint de search ---
    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
    Page<Livro> findByAutorContainingIgnoreCase(String autor, Pageable pageable);
    Page<Livro> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);
    Page<Livro> findByCategoriaNomeContainingIgnoreCase(String nomeCategoria, Pageable pageable);
    Page<Livro> findByEstanteNomeContainingIgnoreCase(String nomeEstante, Pageable pageable);

    // --- Busca Geral (Termo) ---
    // Query para busca geral, usada tanto para Page quanto para List.
    // Nota: A busca por categoria/estante na busca geral foi removida para simplificar e evitar joins complexos.
    // A busca por categoria/estante agora é feita pelo filtro específico.
    String SEARCH_QUERY = "SELECT l FROM Livro l WHERE " +
            "LOWER(l.titulo) LIKE LOWER(concat('%', :termo, '%')) OR " +
            "LOWER(l.autor) LIKE LOWER(concat('%', :termo, '%')) OR " +
            "LOWER(l.isbn) LIKE LOWER(concat('%', :termo, '%'))";

    @Query(SEARCH_QUERY)
    Page<Livro> searchByTermoGeral(@Param("termo") String termo, Pageable pageable);

    @Query(SEARCH_QUERY)
    List<Livro> searchByTermoGeral(@Param("termo") String termo);

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
}