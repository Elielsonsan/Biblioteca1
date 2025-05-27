package org.iftm.biblioteca.service;

import org.iftm.biblioteca.entities.Client;
import org.iftm.biblioteca.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client findById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow();
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Long id, Client client) {
        Client clientDB = findById(id);
        clientDB.setName(client.getName());
        clientDB.setEmail(client.getEmail());
        return clientRepository.save(clientDB);
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}
