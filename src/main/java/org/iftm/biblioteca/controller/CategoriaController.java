package org.iftm.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity; // Importa todas as anotações de mapeamento
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Endpoint para buscar todas as categorias (GET /api/categorias)
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll(@RequestParam(value = "nome", defaultValue = "") String nome) {
        List<CategoriaDTO> list = categoriaService.findAll(nome);
        return ResponseEntity.ok().body(list);
    }

    // Endpoint para buscar sugestões de autocomplete (GET /api/categorias/sugestoes)
    @GetMapping("/sugestoes")
    public ResponseEntity<List<SugestaoDTO>> getSugestoes(@RequestParam("termo") String termo) {
        List<SugestaoDTO> sugestoes = categoriaService.findSugestoes(termo);
        return ResponseEntity.ok(sugestoes);
    }

    // Endpoint para buscar uma categoria por ID (GET /api/categorias/{id})
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Long id) {
        CategoriaDTO dto = categoriaService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    // Endpoint para criar uma nova categoria (POST /api/categorias)
    @PostMapping
    public ResponseEntity<CategoriaDTO> create(@Valid @RequestBody CategoriaDTO dto) {
        dto = categoriaService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    // Endpoint para atualizar uma categoria existente (PUT /api/categorias/{id})
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        dto = categoriaService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    // Endpoint para apagar uma categoria (DELETE /api/categorias/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {        
        categoriaService.delete(id);
        // Adiciona header "reason" para feedback
        return ResponseEntity.noContent().header("reason", "Categoria excluída com sucesso").build();
    }
}
