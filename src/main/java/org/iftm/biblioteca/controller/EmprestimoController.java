package org.iftm.biblioteca.controller;

import java.net.URI;

import org.iftm.biblioteca.dto.EmprestimoDTO;
import org.iftm.biblioteca.service.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @GetMapping
    public ResponseEntity<Page<EmprestimoDTO>> findAll(Pageable pageable) {
        Page<EmprestimoDTO> page = emprestimoService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoDTO> findById(@PathVariable Long id) {
        EmprestimoDTO dto = emprestimoService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<EmprestimoDTO> create(@Valid @RequestBody EmprestimoDTO dto) {
        dto = emprestimoService.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoDTO> registrarDevolucao(@PathVariable Long id) {
        EmprestimoDTO dto = emprestimoService.registrarDevolucao(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Endpoint para registrar a devolução de um livro com base no ID do livro.
     * Útil para cenários onde o ID do empréstimo não é conhecido, como na tela de gerenciamento de livros.
     *
     * @param livroId O ID do livro a ser devolvido.
     * @return Um ResponseEntity com o DTO do empréstimo atualizado.
     */
    @PatchMapping("/livro/{livroId}/devolucao")
    public ResponseEntity<EmprestimoDTO> registrarDevolucaoPorLivro(@PathVariable Long livroId) {
        EmprestimoDTO dto = emprestimoService.registrarDevolucaoPorLivro(livroId);
        return ResponseEntity.ok(dto);
    }
}