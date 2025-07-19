package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.iftm.biblioteca.entities.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EstanteRepository extends JpaRepository<Estante, Long> {
    List<Estante> findByNomeContainingIgnoreCase(String nome);

    Optional<Estante> findByNomeIgnoreCase(String nome);

    @Query("SELECT e.nome FROM Estante e WHERE LOWER(e.nome) LIKE LOWER(concat('%', :termo, '%')) ORDER BY e.nome")
    List<String> findNomesParaSugestao(@Param("termo") String termo, Pageable pageable);

}