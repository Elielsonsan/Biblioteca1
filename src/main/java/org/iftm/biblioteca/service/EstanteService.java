package org.iftm.biblioteca.service;

import java.util.List;

import org.iftm.biblioteca.dto.EstanteDTO;

public interface EstanteService {

    List<EstanteDTO> findAll();

    List<EstanteDTO> findByNome(String nome);

    EstanteDTO insert(EstanteDTO dto);

    EstanteDTO update(Long id, EstanteDTO dto);

    void delete(Long id);
}
