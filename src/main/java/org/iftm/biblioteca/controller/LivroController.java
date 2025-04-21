package org.iftm.biblioteca.controller; // Pacote onde o controller está localizado
// Importa as classes necessárias para o controller

import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas as anotações de mapeamento

import java.util.List;
import java.util.Optional;

@RestController // Indica que esta classe é um Controller REST (combina @Controller e @ResponseBody)
@RequestMapping("/api/livros") // Define o caminho base para todos os endpoints neste controller
public class LivroController {

    @Autowired // Injeta automaticamente uma instância de LivroRepository
    private LivroRepository livroRepository;

    // Endpoint para buscar todos os livros (GET /api/livros)
    @GetMapping
    public ResponseEntity<List<Livro>> buscarTodosLivros() {
        List<Livro> livros = livroRepository.findAll();
        return ResponseEntity.ok(livros); // Retorna a lista com status HTTP 200 OK
    }

    // Endpoint para buscar um livro por ID (GET /api/livros/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarLivroPorId(@PathVariable Long id) {
        Optional<Livro> livroOptional = livroRepository.findById(id);
        if (livroOptional.isPresent()) {
            return ResponseEntity.ok(livroOptional.get()); // Retorna o livro com status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Retorna status 404 Not Found
        }
        // Forma alternativa mais concisa:
        // return livroRepository.findById(id)
        //        .map(ResponseEntity::ok) // Se encontrar, mapeia para ResponseEntity.ok()
        //        .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404
    }

    // Endpoint para criar um novo livro (POST /api/livros)
    @PostMapping
    public ResponseEntity<Livro> criarLivro(@RequestBody Livro livro) {
        // @RequestBody: Converte o JSON enviado no corpo da requisição para um objeto Livro
        // Atenção: O JSON enviado precisa ter os campos corretos (titulo, autor, etc.)
        //          e pode precisar incluir IDs para categoria e estante se forem obrigatórios.
        //          Para simplificar, este exemplo não trata associações com Categoria/Estante.
        try {
            Livro novoLivro = livroRepository.save(livro);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoLivro); // Retorna o livro criado com status 201 Created
        } catch (Exception e) {
            // Tratamento básico de erro, pode ser mais elaborado
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // --- Outros Endpoints (PUT para atualizar, DELETE para remover) ---
    // @PutMapping("/{id}")
    // public ResponseEntity<Livro> atualizarLivro(...) { ... }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deletarLivro(...) { ... }
}
