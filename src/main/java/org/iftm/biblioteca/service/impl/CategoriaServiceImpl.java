package org.iftm.biblioteca.service.impl;

import java.util.List;
import java.util.Optional;

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
    public Categoria salvarNovaCategoria(CategoriaDTO categoriaDTO) {
        // Validações básicas são feitas pelo @Valid no DTO.
        verificarNomeDuplicado(categoriaDTO.getNome(), null);

        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.getNome());
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
    public Categoria atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            // Validações básicas são feitas pelo @Valid no DTO.
            verificarNomeDuplicado(categoriaDTO.getNome(), id);
            categoriaExistente.setNome(categoriaDTO.getNome());
            // Adicione outros campos para atualizar se houver
            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
    }

    @Override
    @Transactional
    public void apagarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + id + " para exclusão."));

        // Regra de negócio: Não permitir exclusão se houver livros associados
        if (livroRepository.countByCategoria(categoria) > 0) { // Supondo countByCategoria
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome()
                    + "' pois ela possui livros associados.");
        }

        categoriaRepository.deleteById(id);
    }

    @Override
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
    public List<Categoria> buscarTodas() {
        return categoriaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNomeExato(String nome) {
        if (!StringUtils.hasText(nome)) {
            throw new RegraDeNegocioException("Nome da categoria para busca não pode ser vazio.");
        }
        // A validação de formato/tamanho/caracteres seria feita no DTO se fosse um
        // input de criação/atualização.
        return categoriaRepository.findByNomeIgnoreCase(nome); // Supondo findByNomeIgnoreCase
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarPorNomeContendo(String trechoNome) {
        if (!StringUtils.hasText(trechoNome) || trechoNome.length() < 2) {
            throw new RegraDeNegocioException("Trecho do nome para busca deve ter pelo menos 2 caracteres.");
        }
        return categoriaRepository.findByNomeContainingIgnoreCase(trechoNome); // Supondo Query Method
    }

    // Consulta que utiliza lógica relacionada a uma regra de negócio (não ter
    // livros)
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasSemLivrosAssociados() {
        return categoriaRepository.findCategoriasSemLivros();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategorias() {
        return categoriaRepository.count();
    }
}