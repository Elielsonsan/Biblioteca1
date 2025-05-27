package org.iftm.biblioteca.controller; // Pacote onde o controller está localizado

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa todas as anotações de mapeamento

import java.util.List;

@RestController
@RequestMapping("/api/categorias") // Caminho base para endpoints de Categoria
public class CategoriaController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Endpoint para buscar todas as categorias (GET /api/categorias)
    @GetMapping
    public ResponseEntity<List<Categoria>> buscarTodasCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return ResponseEntity.ok(categorias);
    }

    // Endpoint para buscar uma categoria por ID (GET /api/categorias/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Long id) {
        return categoriaRepository.findById(id)
               .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a categoria
               .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404 Not Found
    }

    // Endpoint para criar uma nova categoria (POST /api/categorias)
    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
        // @RequestBody converte o JSON do corpo da requisição para um objeto Categoria
        // O JSON precisa ter pelo menos o campo "nome". O ID será gerado.
        try {
            // Garante que o ID seja nulo para criar uma nova entidade
            categoria.setId(null);
            Categoria novaCategoria = categoriaRepository.save(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria); // Retorna 201 Created
        } catch (Exception e) {
             // Pode ser erro de validação (nome único duplicado, etc)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Retorna 400 Bad Request
        }
    }

    // Você pode adicionar @PutMapping("/{id}") e @DeleteMapping("/{id}") aqui depois
}
