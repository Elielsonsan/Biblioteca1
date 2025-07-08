package org.iftm.biblioteca.service;

import org.iftm.biblioteca.dto.EmprestimoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmprestimoService {

    Page<EmprestimoDTO> findAll(Pageable pageable);

    EmprestimoDTO findById(Long id);

    EmprestimoDTO create(EmprestimoDTO dto);

    EmprestimoDTO registrarDevolucao(Long id);

    EmprestimoDTO registrarDevolucaoPorLivro(Long livroId);
}