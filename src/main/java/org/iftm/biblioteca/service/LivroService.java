package org.iftm.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.entities.Livro;

public interface LivroService {

    // --- CRUD ---
    Livro salvarNovoLivro(LivroDTO livroDTO); // Inserir um registro

    // List<Livro> salvarTodosLivros(List<LivroDTO> livroDTOs); // Inserir um
    // conjunto

    Livro atualizarLivro(Long id, LivroDTO livroDTO); // Modificar um registro

    void apagarLivroPorId(Long id); // Apagar um registro

    void apagarTodosLivros(); // Apagar todos os registros

    // --- CONSULTAS (Mínimo 5) ---
    List<Livro> buscarTodos();

    Optional<Livro> buscarPorId(Long id);

    List<Livro> buscarPorTituloContendo(String trechoTitulo);

    List<Livro> buscarPorAutor(String autor);

    List<Livro> buscarPorCategoria(Long categoriaId); // Consulta usando ID da categoria

    Optional<Livro> buscarPorIsbn(String isbn); // Consulta por ISBN (único)

    List<Livro> buscarLivrosPublicadosAposAno(Integer ano); // Consulta que pode se relacionar a uma regra

    List<Livro> buscarLivrosPorTermoGeral(String termo); // Adicionado para busca geral

    List<Livro> buscarPorIsbnContendo(String isbn);

    // --- Métodos para Funcionalidades Avançadas ---

    List<String> buscarSugestoes(String termo, String filtro);
}