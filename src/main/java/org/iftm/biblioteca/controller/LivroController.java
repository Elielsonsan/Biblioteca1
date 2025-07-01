package org.iftm.biblioteca.controller;

import java.util.List;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @GetMapping
    public ResponseEntity<Page<LivroDTO>> search(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "autor", required = false) String autor,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "categoriaNome", required = false) String categoriaNome,
            @RequestParam(value = "estanteNome", required = false) String estanteNome,
            @RequestParam(value = "termo", required = false) String termo,
            Pageable pageable) {
        Page<LivroDTO> page = livroService.search(id, titulo, autor, isbn, categoriaNome, estanteNome, termo, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> findById(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return ResponseEntity.ok(livro);
    }

    @PostMapping
    public ResponseEntity<Livro> create(@Valid @RequestBody LivroDTO livroDTO) {
        Livro novoLivro = livroService.salvarNovoLivro(livroDTO);
        return ResponseEntity.ok(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> update(@PathVariable Long id, @Valid @RequestBody LivroDTO livroDTO) {
        Livro livroAtualizado = livroService.atualizarLivro(id, livroDTO);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livroService.apagarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sugestoes")
    public ResponseEntity<List<SugestaoDTO>> getSugestoes(
            @RequestParam(defaultValue = "") String termo,
            @RequestParam(defaultValue = "termo") String filtro) {
        List<SugestaoDTO> sugestoes = livroService.buscarSugestoes(termo, filtro);
        return ResponseEntity.ok(sugestoes);
    }
}