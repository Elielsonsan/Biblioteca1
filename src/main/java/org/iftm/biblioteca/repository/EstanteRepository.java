package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Estante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstanteRepository extends JpaRepository<Estante, String> {
    List<Estante> findByNomeContainingIgnoreCase(String nome);

    Optional<Estante> findByNomeIgnoreCase(String nome);

    Optional<Estante> findTopByOrderByIdDesc();
}