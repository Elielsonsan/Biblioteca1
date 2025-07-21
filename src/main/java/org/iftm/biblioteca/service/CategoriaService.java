package org.iftm.biblioteca.service;

import java.util.List;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;

/**
 * Interface para o serviço de gerenciamento de Categorias.
 * Define o contrato para as operações de negócio relacionadas a categorias.
 */
public interface CategoriaService {
    List<CategoriaDTO> findAll(String nome);
    CategoriaDTO findById(Long id);
    CategoriaDTO create(CategoriaDTO categoriaDTO);
    CategoriaDTO update(Long id, CategoriaDTO categoriaDTO);
    void delete(Long id);
    List<SugestaoDTO> findSugestoes(String termo);
}