package org.iftm.biblioteca.controller;

import java.util.List;

import org.iftm.biblioteca.dto.EstanteDTO;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.service.EstanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // Importa todas as anotações de mapeamento
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estantes") // Caminho base para endpoints de Estante
public class EstanteController {

    @Autowired
    private EstanteService estanteService;

    // Endpoint para buscar todas as estantes (GET /api/estantes)
    @GetMapping
    public ResponseEntity<List<Estante>> buscarTodasEstantes() {
        List<Estante> estantes = estanteService.buscarTodas();
        return ResponseEntity.ok(estantes);
    }

    // Endpoint para buscar uma estante por ID (GET /api/estantes/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Estante> buscarEstantePorId(@PathVariable Long id) {
        // O método buscarPorId do serviço retorna Optional<Estante>
        return estanteService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a estante
                .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404 Not Found
    }

    // Endpoint para criar uma nova estante (POST /api/estantes)
    @PostMapping
    public ResponseEntity<Estante> criarEstante(@Valid @RequestBody EstanteDTO estanteDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Estante novaEstante = estanteService.salvarNovaEstante(estanteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEstante);
    }

    // Endpoint para atualizar uma estante existente (PUT /api/estantes/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Estante> atualizarEstante(@PathVariable Long id, @Valid @RequestBody EstanteDTO estanteDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Estante estanteAtualizada = estanteService.atualizarEstante(id, estanteDTO);
        return ResponseEntity.ok(estanteAtualizada);
    }

    // Endpoint para apagar uma estante (DELETE /api/estantes/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagarEstante(@PathVariable Long id) {
        // Exceções são tratadas pelo @ControllerAdvice
        estanteService.apagarEstantePorId(id);
        return ResponseEntity.noContent().build();
    }
}
