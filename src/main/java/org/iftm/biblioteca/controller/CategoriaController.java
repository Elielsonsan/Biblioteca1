package org.iftm.biblioteca.controller; // Pacote onde o controller está localizado

import java.util.List;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Importa todas as anotações de mapeamento
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias") // Caminho base para endpoints de Categoria
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Endpoint para buscar todas as categorias (GET /api/categorias)
    @GetMapping
    public ResponseEntity<List<Categoria>> buscarTodasCategorias() {
        List<Categoria> categorias = categoriaService.buscarTodas();
        return ResponseEntity.ok(categorias);
    }

    // Endpoint para buscar uma categoria por ID (GET /api/categorias/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable Long id) {
        // O método buscarPorId do serviço retorna Optional<Categoria>
        // Se não encontrar, o serviço deve lançar RecursoNaoEncontradoException,
        // que será tratado pelo @ControllerAdvice
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok) // Se encontrar, retorna 200 OK com a categoria
                .orElse(ResponseEntity.notFound().build()); // Senão, retorna 404 Not Found
    }

    // Endpoint para criar uma nova categoria (POST /api/categorias)
    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        // Exceções são tratadas pelo @ControllerAdvice
        Categoria novaCategoria = categoriaService.salvarNovaCategoria(categoriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
    }

    // Endpoint para atualizar uma categoria existente (PUT /api/categorias/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        Categoria categoriaAtualizada = categoriaService.atualizarCategoria(id, categoriaDTO);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    // Endpoint para apagar uma categoria (DELETE /api/categorias/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarCategoria(@PathVariable Long id) {
        categoriaService.apagarCategoriaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
