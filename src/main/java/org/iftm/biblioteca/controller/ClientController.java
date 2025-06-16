package org.iftm.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.iftm.biblioteca.dto.ClientDTO;
import org.iftm.biblioteca.entities.Client;
import org.iftm.biblioteca.service.ClientService; // A importação já deve ser da interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clients") // Alterado para corresponder ao frontend
@CrossOrigin(origins = "*")
public class ClientController {

    @Autowired
    private ClientService clientService; // Garanta que o tipo aqui é a interface

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody ClientDTO clientDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Client createdClient = clientService.create(clientDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdClient.getId()) // Assumindo que Client tem um método getId()
                .toUri();
        return ResponseEntity.created(location).body(createdClient);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        // clientService.findById(id) lança RecursoNaoEncontradoException se não
        // encontrado,
        // que é tratada pelo @ControllerAdvice.
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Client updatedClient = clientService.update(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Exceções são tratadas pelo @ControllerAdvice
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Client>> findAll() {
        List<Client> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }
}
