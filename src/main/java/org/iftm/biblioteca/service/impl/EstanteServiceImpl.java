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
        verificarNomeDuplicado(dto.nome(), null);
        
        Estante entity = new Estante();
        entity.setNome(dto.nome());
        // O ID agora é gerado automaticamente pelo banco de dados ao salvar.
        entity = estanteRepository.save(entity);
        
        return new EstanteDTO(entity);
    }

    @Transactional
    public EstanteDTO update(Long id, EstanteDTO dto) {
        Estante entity = estanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estante não encontrada com ID: " + id));
        
        verificarNomeDuplicado(dto.nome(), id);
        entity.setNome(dto.nome());
        entity = estanteRepository.save(entity);
        
        return new EstanteDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        // Busca a estante para garantir que ela existe antes de verificar os livros
        Estante estante = estanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estante não encontrada com ID: " + id));

        // Utiliza o livroRepository para verificar se existem livros associados a esta estante
        if (livroRepository.countByEstante(estante) > 0) {
            throw new EstanteComLivrosException(
                    "Não é possível excluir a estante '" + estante.getNome() + "' pois ela possui livros associados.");
        }

        estanteRepository.deleteById(id); // Exclui apenas se não houver livros
    }

    private void verificarNomeDuplicado(String nome, Long idAtual) {
        Optional<Estante> estanteExistente = estanteRepository.findByNomeIgnoreCase(nome);
        if (estanteExistente.isPresent() && !estanteExistente.get().getId().equals(idAtual)) {
            throw new NomeDuplicadoException("Já existe uma estante com o nome: " + nome);
        }
    }
}