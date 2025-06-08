package org.iftm.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.ClientDTO;
import org.iftm.biblioteca.entities.Client;

public interface ClientService {

    List<Client> findAll();

    Client findById(Long id);

    Client create(ClientDTO clientDTO);

    Client update(Long id, ClientDTO clientDTO);

    void delete(Long id);

    Optional<Client> findByEmail(String email); // Exemplo de método adicional
}