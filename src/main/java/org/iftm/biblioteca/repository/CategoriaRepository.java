package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);
    // JpaRepository já fornece os métodos básicos de CRUD.
    // Métodos personalizados podem ser adicionados aqui se necessário.

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String trechoNome);

    @Query("SELECT c FROM Categoria c WHERE NOT EXISTS (SELECT l FROM Livro l WHERE l.categoria = c)")
    List<Categoria> findCategoriasSemLivros();
}