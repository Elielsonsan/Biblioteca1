package org.iftm.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.EstanteDTO;
import org.iftm.biblioteca.entities.Estante;

public interface EstanteService {

    Estante salvarNovaEstante(EstanteDTO estanteDTO);

    // List<Estante> salvarTodasEstantes(List<EstanteDTO> estanteDTOs); // Avaliar
    // necessidade

    Estante atualizarEstante(Long id, EstanteDTO estanteDTO);

    void apagarEstantePorId(Long id);

    List<Estante> buscarTodas();

    Optional<Estante> buscarPorId(Long id);

    Optional<Estante> buscarPorNomeExato(String nome);

    long contarEstantes();
}