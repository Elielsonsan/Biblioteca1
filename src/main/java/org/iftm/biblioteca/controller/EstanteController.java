package org.iftm.biblioteca.controller; // ou org.iftm.biblioteca.api

import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas as anotações de mapeamento

import java.util.List;


@RestController
@RequestMapping("/api/estantes") // Caminho base para endpoints de Estante
public class EstanteController {

    @Autowired
    private EstanteRepository estanteRepository;

    // Endpoint para buscar todas as estantes (GET /api/estantes)
    @GetMapping
    public ResponseEntity<List<Estante>> buscarTodasEstantes() {
        List<Estante> estantes = estanteRepository.findAll();
        return ResponseEntity.ok(estantes);
    }

    // Endpoint para buscar uma estante por ID (GET /api/estantes/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Estante> buscarEstantePorId(@PathVariable Long id) {
        return estanteRepository.findById(id)
               .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a estante
               .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404 Not Found
    }

    // Endpoint para criar uma nova estante (POST /api/estantes)
    @PostMapping
    public ResponseEntity<Estante> criarEstante(@RequestBody Estante estante) {
        // @RequestBody converte o JSON do corpo da requisição para um objeto Estante
        // O JSON precisa ter pelo menos o campo "nome". O ID será gerado.
        try {
             // Garante que o ID seja nulo para criar uma nova entidade
            estante.setId(null);
            Estante novaEstante = estanteRepository.save(estante);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaEstante); // Retorna 201 Created
        } catch (Exception e) {
            // Pode ser erro de validação (nome único duplicado, etc)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Retorna 400 Bad Request
        }
    }

    // Você pode adicionar @PutMapping("/{id}") e @DeleteMapping("/{id}") aqui depois
}
