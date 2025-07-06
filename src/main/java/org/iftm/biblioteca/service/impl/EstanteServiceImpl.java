package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.iftm.biblioteca.service.EstanteService; // Importar a nova interface

import org.iftm.biblioteca.dto.EstanteDTO;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.exceptions.EstanteComLivrosException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstanteServiceImpl implements EstanteService { // Implementa a interface

    @Autowired
    private EstanteRepository estanteRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional(readOnly = true)
    public List<EstanteDTO> findAll() {
        return estanteRepository.findAll().stream().map(EstanteDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstanteDTO> findByNome(String nome) {
        // Utiliza o método do repositório para uma busca eficiente no banco de dados
        return estanteRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(EstanteDTO::new) // Converte cada Estante encontrada para EstanteDTO
                .collect(Collectors.toList());
    }

    @Transactional
    public EstanteDTO insert(EstanteDTO dto) {
        verificarNomeDuplicado(dto.getNome(), null);
        Estante entity = new Estante();
        entity.setId(gerarProximoId()); // Lógica para gerar o novo ID
        entity.setNome(dto.getNome());
        entity = estanteRepository.save(entity);
        return new EstanteDTO(entity);
    }

    @Transactional
    public EstanteDTO update(String id, EstanteDTO dto) {
        try {
            verificarNomeDuplicado(dto.getNome(), id);
            Estante entity = estanteRepository.getReferenceById(id);
            entity.setNome(dto.getNome());
            entity = estanteRepository.save(entity);
            return new EstanteDTO(entity);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new RecursoNaoEncontradoException("Estante não encontrada com ID: " + id + " para atualização.");
        }
    }

    @Transactional
    public void delete(String id) {
        // Busca a estante para garantir que ela existe antes de verificar os livros
        Estante estante = estanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estante não encontrada com ID: " + id));

        // Utiliza o livroRepository para verificar se existem livros associados a esta estante
        if (livroRepository.countByEstante(estante) > 0) {
            throw new EstanteComLivrosException("Não é possível excluir a estante '" + estante.getNome()
                    + "' pois ela possui livros associados.");
        }

        estanteRepository.deleteById(id); // Exclui apenas se não houver livros
    }

    private void verificarNomeDuplicado(String nome, String idAtual) {
        Optional<Estante> estanteExistente = estanteRepository.findByNomeIgnoreCase(nome);
        if (estanteExistente.isPresent() && !estanteExistente.get().getId().equals(idAtual)) {
            throw new NomeDuplicadoException("Já existe uma estante com o nome: " + nome);
        }
    }

    /**
     * Gera o próximo ID para uma estante no formato "E001", "E002", etc.
     * Este método é sincronizado para evitar condições de corrida em ambientes concorrentes.
     * @return O próximo ID formatado como String.
     */
    private synchronized String gerarProximoId() {
        Optional<Estante> ultimaEstante = estanteRepository.findTopByOrderByIdDesc();
        if (ultimaEstante.isPresent()) {
            String ultimoId = ultimaEstante.get().getId();
            // Remove o prefixo "E" e converte o restante para número
            int proximoNumero = Integer.parseInt(ultimoId.substring(1)) + 1;
            // Formata de volta para "E" com 3 dígitos, preenchendo com zeros à esquerda
            return String.format("E%03d", proximoNumero);
        } else {
            // Se não houver nenhuma estante no banco, começa com "E001"
            return "E001";
        }
    }

    @Override
    public Estante salvarNovaEstante(EstanteDTO estanteDTO) {
        
        throw new UnsupportedOperationException("Unimplemented method 'salvarNovaEstante'");
    }

    @Override
    public Estante atualizarEstante(Long id, EstanteDTO estanteDTO) {
       
        throw new UnsupportedOperationException("Unimplemented method 'atualizarEstante'");
    }

    @Override
    public void apagarEstantePorId(Long id) {
       
        throw new UnsupportedOperationException("Unimplemented method 'apagarEstantePorId'");
    }

    @Override
    public List<Estante> buscarTodas() {
        
        throw new UnsupportedOperationException("Unimplemented method 'buscarTodas'");
    }

    @Override
    public Optional<Estante> buscarPorId(Long id) {
       
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

    @Override
    public Optional<Estante> buscarPorNomeExato(String nome) {
       
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorNomeExato'");
    }

    @Override
    public long contarEstantes() {
        
        throw new UnsupportedOperationException("Unimplemented method 'contarEstantes'");
    }
}