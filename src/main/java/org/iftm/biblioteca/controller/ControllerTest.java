package org.iftm.biblioteca.controller;

import org.iftm.biblioteca.BibliotecaApplication;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ControllerTest {
    @Autowired
    private BibliotecaApplication clientService;
    // Endpoint 0: Criar um novo cliente
    @PostMapping
    public ResponseEntity<Livro> create(@RequestBody Livro livro) {
        Livro createdClient = clientService.create(livro);
        return ResponseEntity.status(201).body(createdClient);
    }
    // Endpoint 2: Buscar um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Livro> findById(@PathVariable Long id) {
        Livro client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }
    // Endpoint 3: Atualizar um cliente
    @PutMapping("/{id}")
    public ResponseEntity<Livro> update(@PathVariable Long id, @RequestBody Livro livro) {
        Livro updatedClient = clientService.update(id, livro);
        return ResponseEntity.ok(updatedClient);
    }
    // Endpoint 4: Deletar um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
    // Endpoint 5: Listar todos os clientes
    @GetMapping
    public ResponseEntity<List<Livro>> findAll() {
        List<Livro> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }   
    
}