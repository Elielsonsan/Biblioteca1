package org.iftm.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.service.UsuariosService; // A importação já deve ser da interface
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import org.iftm.biblioteca.dto.UsuariosDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios") // Rota padronizada
@CrossOrigin(origins = "*")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @PostMapping
    public ResponseEntity<UsuariosDTO> create(@Valid @RequestBody UsuariosDTO dto) {
        // Exceções são tratadas pelo @ControllerAdvice
        dto = usuariosService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuariosDTO> findById(@PathVariable Long id) {
        UsuariosDTO dto = usuariosService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuariosDTO> update(@PathVariable Long id, @Valid @RequestBody UsuariosDTO dto) {
        // Exceções são tratadas pelo @ControllerAdvice
        dto = usuariosService.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        // Exceções são tratadas pelo @ControllerAdvice
        usuariosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UsuariosDTO>> findAll(@RequestParam(value = "name", defaultValue = "") String name,
            Pageable pageable) {
        Page<UsuariosDTO> page;
        if (name.trim().isEmpty()) {
            page = usuariosService.findAll(pageable);
        } else {
            page = usuariosService.findByNameContaining(name, pageable);
        }
        return ResponseEntity.ok(page);
    }

    @GetMapping("/sugestoes")
    public ResponseEntity<List<SugestaoDTO>> getSugestoes(@RequestParam(defaultValue = "") String termo) {
        List<SugestaoDTO> sugestoes = usuariosService.buscarSugestoesDeNomes(termo);
        return ResponseEntity.ok(sugestoes);
    }
}
