package org.iftm.biblioteca.controller;

import java.net.URI;
import java.util.List;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.LivroDTOResumido;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.service.LivroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<Page<LivroDTO>> findPaginated(
            @RequestParam(value = "termo", required = false) String termo,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "autor", required = false) String autor,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "anoPublicacao", required = false) Integer anoPublicacao,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "estanteId", required = false) String estanteId,
            Pageable pageable) {
        
        Page<LivroDTO> page = livroService.findPaginated(termo, titulo, autor, isbn, anoPublicacao, categoriaId, estanteId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/disponiveis")
    public ResponseEntity<Page<LivroDTOResumido>> findAvailableBooks(
            @RequestParam(value = "termo", defaultValue = "") String termo,
            Pageable pageable) {
        Page<LivroDTOResumido> page = livroService.findAvailableBooks(termo, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping(value = "/sugestoes")
    public ResponseEntity<List<SugestaoDTO>> getSuggestions(
            @RequestParam("termo") String termo,
            @RequestParam(value = "filtro", defaultValue = "geral") String filtro) {
        List<SugestaoDTO> sugestoes = livroService.buscarSugestoes(termo, filtro);
        return ResponseEntity.ok(sugestoes);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<LivroDTO> findById(@PathVariable Long id) {
        LivroDTO dto = livroService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<LivroDTO> insert(@Valid @RequestBody LivroDTO dto) {
        dto = livroService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<LivroDTO> update(@PathVariable Long id, @Valid @RequestBody LivroDTO dto) {
        dto = livroService.update(id, dto);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}