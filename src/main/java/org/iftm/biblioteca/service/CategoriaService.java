package org.iftm.biblioteca.service;

import java.util.List;
import org.iftm.biblioteca.dto.CategoriaDTO;

/**
 * Interface para o serviço de gerenciamento de Categorias.
 * Define o contrato para as operações de negócio relacionadas a categorias.
 */
public interface CategoriaService {
    List<CategoriaDTO> findAll();
    CategoriaDTO findById(Long id);
    List<CategoriaDTO> findByNomeContaining(String nome);
    CategoriaDTO create(CategoriaDTO categoriaDTO);
    CategoriaDTO update(Long id, CategoriaDTO categoriaDTO);
    void delete(Long id);
}