package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    // Busca um cliente pelo email (deve retornar Optional pois email é único)
    Optional<Usuarios> findByEmail(String email);

    // Busca clientes cujo nome contenha a string fornecida (ignorando
    // maiúsculas/minúsculas)
    List<Usuarios> findByNameContainingIgnoreCase(String name);

    Page<Usuarios> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
