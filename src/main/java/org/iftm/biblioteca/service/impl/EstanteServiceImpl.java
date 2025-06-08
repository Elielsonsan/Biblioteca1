package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.EstanteDTO;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.EstanteService;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class EstanteServiceImpl implements EstanteService {

    @Autowired
    private EstanteRepository estanteRepository;

    @Autowired
    private LivroRepository livroRepository; // Para verificar livros associados na exclusão

    private void verificarNomeDuplicado(String nome, Long idAtual) {
        Optional<Estante> estanteExistente = estanteRepository.findByNome(nome); // Assumindo que findByNome é
                                                                                 // case-sensitive ou ajustado conforme
                                                                                 // regra
        if (estanteExistente.isPresent() &&
                (idAtual == null || !estanteExistente.get().getId().equals(idAtual))) {
            throw new NomeDuplicadoException("Já existe uma estante com o nome: " + nome);
        }
    }

    @Override
    @Transactional
    public Estante salvarNovaEstante(EstanteDTO estanteDTO) {
        // Validações básicas são feitas pelo @Valid no DTO.
        verificarNomeDuplicado(estanteDTO.getNome(), null);

        Estante estante = new Estante();
        estante.setNome(estanteDTO.getNome());
        return estanteRepository.save(estante);
    }

    // @Override // Removido pois o método está comentado na interface
    // EstanteService
    @Transactional
    public List<Estante> salvarTodasEstantes(List<EstanteDTO> estanteDTOs) {
        // Implementação de lote com DTOs requer mapeamento e validação individual.
        // Por simplicidade, esta funcionalidade pode ser reavaliada ou implementada com
        // mais detalhes.
        throw new UnsupportedOperationException("Batch save from DTOs not fully implemented yet.");
    }

    @Override
    @Transactional
    public Estante atualizarEstante(Long id, EstanteDTO estanteDTO) {
        return estanteRepository.findById(id).map(estanteExistente -> {
            // Validações básicas são feitas pelo @Valid no DTO.
            verificarNomeDuplicado(estanteDTO.getNome(), id);
            estanteExistente.setNome(estanteDTO.getNome());
            return estanteRepository.save(estanteExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Estante não encontrada com ID: " + id));
    }

    @Override
    @Transactional
    public void apagarEstantePorId(Long id) {
        Estante estante = estanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estante não encontrada com ID: " + id + " para exclusão."));

        if (livroRepository.countByEstante(estante) > 0) {
            throw new RegraDeNegocioException(
                    "Não é possível excluir a estante '" + estante.getNome() + "' pois ela possui livros associados.");
        }
        estanteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estante> buscarTodas() {
        return estanteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estante> buscarPorId(Long id) {
        return estanteRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Estante> buscarPorNomeExato(String nome) {
        if (!StringUtils.hasText(nome)) {
            throw new RegraDeNegocioException("Nome para busca não pode ser vazio.");
        }
        return estanteRepository.findByNome(nome);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarEstantes() {
        return estanteRepository.count();
    }
}