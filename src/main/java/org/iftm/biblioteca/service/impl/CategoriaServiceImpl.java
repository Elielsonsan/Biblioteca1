package org.iftm.biblioteca.service.impl;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.LivroRepository; // Para verificar livros associados
import org.iftm.biblioteca.service.CategoriaService;
import org.iftm.biblioteca.service.exceptions.CategoriaComLivrosException;
import org.iftm.biblioteca.service.exceptions.NomeDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LivroRepository livroRepository; // Para regra de negócio na exclusão

    // --- REGRAS DE NEGÓCIO (MÉTODOS PRIVADOS DE VALIDAÇÃO) ---

    // Regra 1: Nome da categoria não pode ser vazio e deve ter tamanho adequado
    private void validarNomeCategoria(String nome) {
        if (!StringUtils.hasText(nome)) {
            throw new RegraDeNegocioException("Nome da categoria não pode ser vazio.");
        }
        if (nome.length() < 3 || nome.length() > 100) {
            throw new RegraDeNegocioException("Nome da categoria deve ter entre 3 e 100 caracteres.");
        }
    }

    // Regra 2: Nome da categoria deve ser único
    private void verificarNomeDuplicado(String nome, Long idAtual) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findByNomeIgnoreCase(nome); // Supondo findByNomeIgnoreCase
        if (categoriaExistente.isPresent() &&
            (idAtual == null || !categoriaExistente.get().getId().equals(idAtual))) {
            throw new NomeDuplicadoException("Já existe uma categoria com o nome: " + nome);
        }
    }

    // Regra 3: Validação de ausência de caracteres especiais (simplificada)
    private void validarCaracteresNome(String nome) {
        if (nome.matches(".*[^a-zA-Z0-9\\sáéíóúâêîôûãõçÁÉÍÓÚÂÊÎÔÛÃÕÇ-]+.*")) { // Permite letras, números, espaços, acentos, hífen
            throw new RegraDeNegocioException("Nome da categoria contém caracteres inválidos.");
        }
    }


    // --- MÉTODOS CRUD (COM VALIDAÇÕES) ---

    @Override
    @Transactional
    public Categoria salvarNovaCategoria(Categoria categoria) {
        if (categoria.getId() != null) {
            throw new RegraDeNegocioException("Para salvar uma nova categoria, o ID deve ser nulo.");
        }
        validarNomeCategoria(categoria.getNome());
        validarCaracteresNome(categoria.getNome());
        verificarNomeDuplicado(categoria.getNome(), null);
        return categoriaRepository.save(categoria);
    }

    @Override
    @Transactional
    public List<Categoria> salvarTodasCategorias(List<Categoria> categorias) {
        // Valida cada categoria individualmente antes de tentar salvar o lote
        for (Categoria cat : categorias) {
             if (cat.getId() != null) {
                throw new RegraDeNegocioException("Para salvar uma nova categoria em lote, o ID de '" + cat.getNome() + "' deve ser nulo.");
            }
            validarNomeCategoria(cat.getNome());
            validarCaracteresNome(cat.getNome());
            verificarNomeDuplicado(cat.getNome(), null); // Pode ser ineficiente para lotes grandes, mas garante integridade
        }
        return categoriaRepository.saveAll(categorias);
    }

    @Override
    @Transactional
    public Categoria atualizarCategoria(Long id, Categoria categoriaAtualizada) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            validarNomeCategoria(categoriaAtualizada.getNome());
            validarCaracteresNome(categoriaAtualizada.getNome());
            verificarNomeDuplicado(categoriaAtualizada.getNome(), id);

            categoriaExistente.setNome(categoriaAtualizada.getNome());
            // Adicione outros campos para atualizar se houver
            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id));
    }

    @Override
    @Transactional
    public void apagarCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + id + " para exclusão."));

        // Regra de negócio: Não permitir excluir categoria se tiver livros associados
        if (livroRepository.countByCategoria(categoria) > 0) { // Supondo countByCategoria no LivroRepository
            throw new CategoriaComLivrosException("Não é possível excluir a categoria '" + categoria.getNome() + "' pois existem livros associados a ela.");
        }
        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void apagarTodasCategorias() {
        // CUIDADO: Esta operação pode ser perigosa.
        // Adicionar verificação se alguma categoria tem livros associados antes de deletar tudo?
        // List<Categoria> todas = categoriaRepository.findAll();
        // for (Categoria cat : todas) {
        //     if (livroRepository.countByCategoria(cat) > 0) {
        //         throw new CategoriaComLivrosException("Não é possível excluir todas as categorias. A categoria '" + cat.getNome() + "' possui livros associados.");
        //     }
        // }
        categoriaRepository.deleteAll();
        // Se quiser ser mais seguro, implemente a verificação acima.
        // Por simplicidade aqui, vamos direto ao deleteAll.
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
        // Utiliza uma regra de negócio (validação de nome) antes da consulta
        validarNomeCategoria(nome); // Garante que o nome tem formato/tamanho válido para busca
        validarCaracteresNome(nome);
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

    // Consulta que utiliza lógica relacionada a uma regra de negócio (não ter livros)
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> buscarCategoriasSemLivrosAssociados() {
        // Esta consulta é mais complexa se feita diretamente com Query Methods simples.
        // Uma forma é buscar todas e filtrar na aplicação, ou criar uma query customizada.
        // Exemplo filtrando na aplicação (pode não ser ideal para muitos dados):
        List<Categoria> todasCategorias = categoriaRepository.findAll();
        return todasCategorias.stream()
                .filter(cat -> livroRepository.countByCategoria(cat) == 0) // Supondo countByCategoria
                .collect(Collectors.toList());
        // Alternativa seria uma @Query customizada no CategoriaRepository
        // Ex: @Query("SELECT c FROM Categoria c WHERE NOT EXISTS (SELECT l FROM Livro l WHERE l.categoria = c)")
    }

    @Override
    @Transactional(readOnly = true)
    public long contarCategorias() {
        return categoriaRepository.count();
    }
}