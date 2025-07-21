package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.service.CategoriaService;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll(String nome) {
        List<Categoria> list;
        if (nome == null || nome.trim().isEmpty()) {
            list = categoriaRepository.findAll();
        } else {
            list = categoriaRepository.findByNomeContainingIgnoreCase(nome);
        }
        return list.stream().map(CategoriaDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SugestaoDTO> findSugestoes(String termo) {
        // Adiciona paginação para consistência e performance, limitando a 10 sugestões.
        Pageable limit = PageRequest.of(0, 10);
        return categoriaRepository.findNomesParaSugestao(termo, limit).stream()
                .map(nome -> new SugestaoDTO(nome, "categoria"))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) {
        Categoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
        return new CategoriaDTO(entity);
    }

    @Override
    @Transactional
    public CategoriaDTO create(CategoriaDTO dto) {
        validarNomeDuplicado(dto.nome(), null);
        Categoria entity = new Categoria();
        entity.setNome(dto.nome());
        entity = categoriaRepository.save(entity);
        return new CategoriaDTO(entity);
    }

    @Override
    @Transactional
    public CategoriaDTO update(Long id, CategoriaDTO dto) {
        validarNomeDuplicado(dto.nome(), id);
        Categoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada para atualização com ID: " + id));
        entity.setNome(dto.nome());
        entity = categoriaRepository.save(entity);
        return new CategoriaDTO(entity);
    }

    @Override
    public void delete(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada para exclusão com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    private void validarNomeDuplicado(String nome, Long id) {
        categoriaRepository.findByNomeIgnoreCase(nome).ifPresent(categoriaExistente -> {
            if (id == null || !categoriaExistente.getId().equals(id)) {
                throw new RegraDeNegocioException("Já existe uma categoria com o nome '" + nome + "'.");
            }
        });
    }
}