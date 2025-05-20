package org.iftm.biblioteca.service;

import org.iftm.biblioteca.entities.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    // --- CRUD ---
    Categoria salvarNovaCategoria(Categoria categoria);
    List<Categoria> salvarTodasCategorias(List<Categoria> categorias);
    Categoria atualizarCategoria(Long id, Categoria categoria);
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