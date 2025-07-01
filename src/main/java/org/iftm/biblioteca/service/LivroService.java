package org.iftm.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LivroService {

    Livro salvarNovoLivro(LivroDTO livroDTO);

    Livro atualizarLivro(Long id, LivroDTO livroDTO);

    void apagarLivroPorId(Long id);

    void apagarTodosLivros();

    Page<LivroDTO> search(String id, String titulo, String autor, String isbn, String categoriaNome, String estanteNome,
            String termo, Pageable pageable);

    Page<LivroDTO> buscarTodos(Pageable pageable);

    Optional<Livro> buscarPorId(Long id);

    List<Livro> buscarLivrosPublicadosAposAno(Integer ano);

    Optional<Livro> buscarPorIsbn(String isbn);

    List<Livro> buscarPorCategoria(Long categoriaId);

    List<Livro> buscarPorTituloContendo(String trechoTitulo);

    List<Livro> buscarPorAutor(String autor);

    List<Livro> buscarLivrosPorTermoGeral(String termo);

    List<Livro> buscarPorIsbnContendo(String isbn);

    List<Livro> buscarPorCategoriaNomeContendo(String nomeCategoria);

    List<Livro> buscarPorEstanteNomeContendo(String nomeEstante);

    List<SugestaoDTO> buscarSugestoes(String termo, String filtro);
}