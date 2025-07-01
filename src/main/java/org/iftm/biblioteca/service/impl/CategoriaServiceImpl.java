package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Adicionar este import

import org.iftm.biblioteca.dto.CategoriaDTO;
import org.iftm.biblioteca.entities.Categoria; // Para verificar livros associados
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.CategoriaService;
import org.iftm.biblioteca.service.exceptions.CategoriaComLivrosException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LivroRepository livroRepository; // Para regra de negócio na exclusão

    // Regra 2: Nome da categoria deve ser único
    private void verificarNomeDuplicado(String nome, Long idAtual) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeIgnoreCase(nome); // Supondo findByNomeIgnoreCase
        if (categoriaExistente.isPresent() &&
                (idAtual == null || !categoriaExistente.get().getId().equals(idAtual))) {
            throw new NomeDuplicadoException(
                    "Já existe uma categoria com o nome: " + nome);
        }
    }

    // --- MÉTODOS CRUD (COM VALIDAÇÕES) ---

    @Override
    @Transactional
    public Categoria create(CategoriaDTO categoriaDTO) { // Renomeado e alinhado com a interface
        // Validações básicas são feitas pelo @Valid no DTO.
        verificarNomeDuplicado(categoriaDTO.getNome(), null);

        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.getNome());
        // O ID é gerado pelo banco de dados ao salvar
        return categoriaRepository.save(categoria);
    }
    // @Override // Removido pois o método está comentado na interface
    // CategoriaService
    @Transactional
    public List<Categoria> salvarTodasCategorias(List<CategoriaDTO> categoriaDTOs) {
        // Implementação de lote com DTOs requer mapeamento e validação individual.
        // Por simplicidade, esta funcionalidade pode ser reavaliada ou implementada com
        // mais detalhes.
        throw new UnsupportedOperationException("Batch save from DTOs not fully implemented yet.");
    }

    @Override
    @Transactional
    public CategoriaDTO update(Long id, CategoriaDTO categoriaDTO) { // Renomeado e tipo de retorno ajustado
        try {
            Categoria categoriaExistente = categoriaRepository.getReferenceById(id); // Evita consulta desnecessária
            // Validações básicas são feitas pelo @Valid no DTO.
            verificarNomeDuplicado(categoriaDTO.getNome(), id);
            categoriaExistente.setNome(categoriaDTO.getNome());
            categoriaExistente = categoriaRepository.save(categoriaExistente);
            return new CategoriaDTO(categoriaExistente); // Converte para DTO
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id + " para atualização.");
        }
    }

    @Override
    @Transactional
    public void delete(Long id) { // Renomeado de apagarCategoriaPorId para delete
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + id + " para exclusão."));

        // Regra de negócio: Não permitir exclusão se houver livros associados
        if (livroRepository.countByCategoria(categoria) > 0) { // Supondo countByCategoria
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome()
                    + "' pois ela possui livros associados.");
        }

        try {
            categoriaRepository.deleteById(id);
            categoriaRepository.flush(); // Força a execução do SQL para capturar a exceção aqui mesmo.
        } catch (DataIntegrityViolationException e) {
            // Este bloco atua como uma segunda camada de proteção caso a verificação acima falhe.
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome()
                    + "' pois ela possui livros associados.");
        }
    }

    // @Override // Removido pois não está na interface CategoriaService (versão DTO)
    @Transactional
    public void apagarTodasCategorias() {
        // CUIDADO: Esta operação pode ser perigosa.
        // Verifica se alguma categoria tem livros associados antes de deletar tudo.
        List<Categoria> todas = categoriaRepository.findAll();
        for (Categoria cat : todas) {
            if (livroRepository.countByCategoria(cat) > 0) {
                throw new CategoriaComLivrosException("Não é possível excluir todas as categorias. A categoria '"
                        + cat.getNome() + "' possui livros associados.");
            }
        }
        categoriaRepository.deleteAll();
    }

    // --- MÉTODOS DE CONSULTA ---

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll() { // Renomeado e tipo de retorno ajustado
        List<Categoria> list = categoriaRepository.findAll();
        return list.stream().map(CategoriaDTO::new).collect(Collectors.toList()); // Converte para List<CategoriaDTO>
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id) { // Renomeado e tipo de retorno ajustado
        Optional<Categoria> obj = categoriaRepository.findById(id);
        Categoria entity = obj.orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
        return new CategoriaDTO(entity); // Converte para CategoriaDTO
    }
    // @Override // Removido pois não está na interface CategoriaService (versão DTO)
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNomeExato(String nome) {
        if (!StringUtils.hasText(nome)) {
            throw new RegraDeNegocioException("Nome da categoria para busca não pode ser vazio.");
        }
        // A validação de formato/tamanho/caracteres seria feita no DTO se fosse um
        // input de criação/atualização.
        return categoriaRepository.findByNomeIgnoreCase(nome); // Supondo findByNomeIgnoreCase
    }

    // @Override // Removido pois não está na interface CategoriaService (versão DTO)
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNomeContendo(String trechoNome) {
        if (!StringUtils.hasText(trechoNome) || trechoNome.length() < 2) {
            throw new RegraDeNegocioException("Trecho do nome para busca deve ter pelo menos 2 caracteres.");
        }
        return categoriaRepository.findByNomeContainingIgnoreCase(trechoNome); // Supondo Query Method
    }

    // Consulta que utiliza lógica relacionada a uma regra de negócio (não ter
    // livros)
    // @Override // Removido pois não está na interface CategoriaService (versão DTO)
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasSemLivrosAssociados() {
        return categoriaRepository.findCategoriasSemLivros();
    }

    // @Override // Removido pois não está na interface CategoriaService (versão DTO)
    @Transactional(readOnly = true)
    public long contarCategorias() {
        return categoriaRepository.count();
    }
}