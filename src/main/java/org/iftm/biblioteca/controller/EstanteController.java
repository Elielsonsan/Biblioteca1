package org.iftm.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.iftm.biblioteca.dto.EstanteDTO;
import org.iftm.biblioteca.service.EstanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estantes")
public class EstanteController {

    @Autowired
    private EstanteService estanteService;

    @GetMapping
    public ResponseEntity<List<EstanteDTO>> find(@RequestParam(value = "nome", defaultValue = "") String nome) {
        List<EstanteDTO> list;
        if (nome.isEmpty()) {
            list = estanteService.findAll();
        } else {
            list = estanteService.findByNome(nome);
        }
        return ResponseEntity.ok().body(list);
    }

    @PostMapping
    public ResponseEntity<EstanteDTO> insert(@Valid @RequestBody EstanteDTO dto) {
        dto = estanteService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstanteDTO> update(@PathVariable Long id, @Valid @RequestBody EstanteDTO dto) {
        dto = estanteService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estanteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}