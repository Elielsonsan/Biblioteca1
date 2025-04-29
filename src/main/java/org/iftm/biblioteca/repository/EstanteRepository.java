package org.iftm.biblioteca.repository;

import java.util.Optional;

import org.iftm.biblioteca.entities.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstanteRepository extends JpaRepository<Estante, Long> {

    Optional<Estante> findByNome(String nomeInexistente);
    // JpaRepository já fornece os métodos básicos de CRUD.
    // Métodos personalizados podem ser adicionados aqui se necessário.
}