package org.iftm.biblioteca.service;

import java.util.List;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.LivroDTOResumido;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LivroService {

    // --- Métodos de Busca ---
    Page<LivroDTO> findPaginated(String termo, String titulo, String autor, String isbn, Integer anoPublicacao,
            Long categoriaId, String estanteId, Pageable pageable);

    Page<LivroDTOResumido> findAvailableBooks(String termo, Pageable pageable);

    List<SugestaoDTO> buscarSugestoes(String termo, String filtro);

    // --- Métodos CRUD ---
    LivroDTO findById(Long id);

    LivroDTO insert(LivroDTO livroDTO);

    LivroDTO update(Long id, LivroDTO livroDTO);

    void delete(Long id);
}