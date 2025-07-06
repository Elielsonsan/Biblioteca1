package org.iftm.biblioteca.service;

import java.util.Optional;

import org.iftm.biblioteca.dto.UsuariosDTO;
import org.iftm.biblioteca.entities.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuariosService {

    Page<UsuariosDTO> findAll(Pageable pageable);

    UsuariosDTO findById(Long id);

    UsuariosDTO create(UsuariosDTO clientDTO);

    UsuariosDTO update(Long id, UsuariosDTO clientDTO);

    void delete(Long id);

    Page<UsuariosDTO> findByNameContaining(String name, Pageable pageable);

    Optional<Usuarios> findByEmail(String email); // Exemplo de método adicional
}