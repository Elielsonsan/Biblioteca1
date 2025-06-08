package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Busca um cliente pelo email (deve retornar Optional pois email é único)
    Optional<Client> findByEmail(String email);

    // Busca clientes cujo nome contenha a string fornecida (ignorando
    // maiúsculas/minúsculas)
    List<Client> findByNameContainingIgnoreCase(String name);
}
