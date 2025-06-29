package org.iftm.biblioteca.controller; // Pacote onde o controller está localizado

// Importa as classes necessárias para o controller

import java.net.URI;
import java.util.List; // Adicionar para ServletUriComponentsBuilder

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin; // Importa todas as anotações de mapeamento
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; // Adicionar para parâmetros de busca
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid; // Adicionar para construir URI

@RestController // Indica que esta classe é um Controller REST (combina @Controller e
                // @ResponseBody)
@RequestMapping("/api/livros") // Define o caminho base para todos os endpoints neste controller
@CrossOrigin(origins = "*")
public class LivroController {

    @Autowired // Injeta automaticamente uma instância de LivroRepository
    private LivroService livroService;

    // Endpoint para buscar todos os livros ou filtrar por termo, título, autor ou
    // ISBN
    @GetMapping
    public ResponseEntity<List<Livro>> getAllLivros(
            @RequestParam(name = "termo", required = false) String termoBusca,
            @RequestParam(name = "titulo", required = false) String titulo,
            @RequestParam(name = "autor", required = false) String autor,
            @RequestParam(name = "isbn", required = false) String isbn) {
        List<Livro> livros;
        if (titulo != null && !titulo.trim().isEmpty()) {
            livros = livroService.buscarPorTituloContendo(titulo);
        } else if (autor != null && !autor.trim().isEmpty()) {
            livros = livroService.buscarPorAutor(autor);
        } else if (isbn != null && !isbn.trim().isEmpty()) {
            // Este método precisa ser criado no service e repository
            livros = livroService.buscarPorIsbnContendo(isbn);
        } else if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            livros = livroService.buscarLivrosPorTermoGeral(termoBusca);
        } else {
            livros = livroService.buscarTodos();
        }
        return ResponseEntity.ok(livros);
    }

    // Endpoint para obter sugestões de autocomplete
    @GetMapping("/sugestoes")
    public ResponseEntity<List<String>> getSugestoes(
            @RequestParam("termo") String termo,
            @RequestParam(name = "filtro", defaultValue = "termo") String filtro) {
        List<String> sugestoes = livroService.buscarSugestoes(termo, filtro);
        return ResponseEntity.ok(sugestoes);
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
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(novoLivro.getId()).toUri();
        return ResponseEntity.created(uri).body(novoLivro);
    }

    // Endpoint para atualizar um livro existente (PUT /api/livros/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable Long id, @Valid @RequestBody LivroDTO livroDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Livro livroAtualizado = livroService.atualizarLivro(id, livroDTO);
        // Lembre-se que o método atualizarLivro no LivroServiceImpl precisa ser implementado.
        // Atualmente ele lança UnsupportedOperationException.
        return ResponseEntity.ok(livroAtualizado);
    }

    // Endpoint para apagar um livro (DELETE /api/livros/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivro(@PathVariable Long id) { // Alterado para ResponseEntity<Void>
        // Exceções são tratadas pelo @ControllerAdvice
        livroService.apagarLivroPorId(id);
        return ResponseEntity.noContent().build();
    }
}
