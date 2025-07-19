package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String trechoNome);

    @Query("SELECT c.nome FROM Categoria c WHERE LOWER(c.nome) LIKE LOWER(concat('%', :termo, '%')) ORDER BY c.nome")
    List<String> findNomesParaSugestao(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT c FROM Categoria c WHERE NOT EXISTS (SELECT l FROM Livro l WHERE l.categoria = c)")
    List<Categoria> findCategoriasSemLivros();
}