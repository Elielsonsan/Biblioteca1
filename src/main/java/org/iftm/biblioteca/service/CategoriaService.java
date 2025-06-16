package org.iftm.biblioteca.service;

import java.util.List;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.entities.Categoria;

import jakarta.validation.Valid;

public interface CategoriaService {

    // --- CRUD ---
    public List<CategoriaDTO> findAll();
    CategoriaDTO findById(Long id);
    Categoria create(CategoriaDTO dto);
    @Valid
    CategoriaDTO update(Long id, CategoriaDTO dto);
    void delete(Long id);

    // Você pode manter outros métodos de consulta específicos aqui, se necessário,
    // mas os métodos CRUD acima são os que o CategoriaController (DTO based) utiliza.
    // Os métodos abaixo são exemplos de consultas que estavam na sua implementação
    // e podem ser mantidos ou adaptados conforme a necessidade.

    // Optional<Categoria> buscarPorNomeExato(String nome);
    // List<Categoria> buscarPorNomeContendo(String trechoNome);
    // List<Categoria> buscarCategoriasSemLivrosAssociados();
    // long contarCategorias();
    // void apagarTodasCategorias(); // Cuidado com esta operação!
}