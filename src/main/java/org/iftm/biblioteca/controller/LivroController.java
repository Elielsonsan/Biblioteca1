package org.iftm.biblioteca.controller; // Pacote onde o controller está localizado

// Importa as classes necessárias para o controller

import java.util.List;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Importa todas as anotações de mapeamento
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController // Indica que esta classe é um Controller REST (combina @Controller e
                // @ResponseBody)
@RequestMapping("/api/livros") // Define o caminho base para todos os endpoints neste controller
@CrossOrigin(origins = "*")
public class LivroController {

    @Autowired // Injeta automaticamente uma instância de LivroRepository
    private LivroService livroService;

    // Endpoint para buscar todos os livros (GET /api/livros)
    @GetMapping
    public ResponseEntity<List<Livro>> buscarTodosLivros() {
        List<Livro> livros = livroService.buscarTodos();
        return ResponseEntity.ok(livros); // Retorna a lista com status HTTP 200 OK
    }

    // Endpoint para buscar um livro por ID (GET /api/livros/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        return livroService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, mapeia para ResponseEntity.ok()
                .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404
    }

    // Endpoint para criar um novo livro (POST /api/livros)
    @PostMapping
    public ResponseEntity<Livro> criarLivro(@Valid @RequestBody LivroDTO livroDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Livro novoLivro = livroService.salvarNovoLivro(livroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro);
    }

    // Endpoint para atualizar um livro existente (PUT /api/livros/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @Valid @RequestBody LivroDTO livroDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Livro livroAtualizado = livroService.atualizarLivro(id, livroDTO);
        return ResponseEntity.ok(livroAtualizado);
    }

    // Endpoint para apagar um livro (DELETE /api/livros/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarLivro(@PathVariable Long id) {
        // Exceções são tratadas pelo @ControllerAdvice
        livroService.apagarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }
}
