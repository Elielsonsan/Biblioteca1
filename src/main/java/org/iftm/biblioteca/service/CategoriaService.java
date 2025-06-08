package org.iftm.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.entities.Categoria;

public interface CategoriaService {

    // --- CRUD ---
    Categoria salvarNovaCategoria(CategoriaDTO categoriaDTO);

    // List<Categoria> salvarTodasCategorias(List<CategoriaDTO> categoriaDTOs); //
    // Avaliar necessidade

    Categoria atualizarCategoria(Long id, CategoriaDTO categoriaDTO);

    void apagarCategoriaPorId(Long id);

    void apagarTodasCategorias(); // Cuidado com esta operação!

    // --- CONSULTAS (Mínimo 5) ---
    List<Categoria> buscarTodas();

    Optional<Categoria> buscarPorId(Long id);

    Optional<Categoria> buscarPorNomeExato(String nome); // Usa validação de nome

    List<Categoria> buscarPorNomeContendo(String trechoNome);

    List<Categoria> buscarCategoriasSemLivrosAssociados(); // Consulta que pode usar regra/lógica

    long contarCategorias(); // Consulta simples adicional
}