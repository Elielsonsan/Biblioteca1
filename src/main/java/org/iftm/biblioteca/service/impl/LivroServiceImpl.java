package org.iftm.biblioteca.service.impl;

import java.time.Year;
import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.LivroService;
import org.iftm.biblioteca.service.exceptions.IsbnDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException; // Para verificar strings vazias
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class LivroServiceImpl implements LivroService {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // Para validar/buscar categoria
    @Autowired
    private EstanteRepository estanteRepository; // Para validar/buscar estante

    // --- REGRAS DE NEGÓCIO (MÉTODOS PRIVADOS DE VALIDAÇÃO) ---

    private void validarAnoPublicacaoDTORules(Integer anoPublicacao) {
        // Validações básicas (@NotNull, @Min, @Max) são feitas pelo @Valid no DTO.
        // Esta validação pode ser mais específica, como verificar contra o ano atual.
        int anoAtual = Year.now().getValue();
        if (anoPublicacao != null && (anoPublicacao > anoAtual || anoPublicacao < 1400)) {
            throw new RegraDeNegocioException("Ano de publicação inválido. Deve ser entre 1400 e " + anoAtual + ".");
        }
    }

    // Regra 3: ISBN não pode ser duplicado (exceto para o próprio livro em caso
    // de atualização
    private void validarIsbnUnico(String isbn, Long idLivroExistente) {
        // @NotBlank no DTO já valida se está vazio.
        if (StringUtils.hasText(isbn)) {
            Optional<Livro> livroExistenteComIsbn = livroRepository.findByIsbn(isbn);
            if (livroExistenteComIsbn.isPresent() &&
                    (idLivroExistente == null || !livroExistenteComIsbn.get().getId().equals(idLivroExistente))) {
                throw new IsbnDuplicadoException("ISBN '" + isbn + "' já cadastrado para outro livro.");
            }
        }
    }

    private Categoria carregarCategoriaValidada(Long categoriaId) {
        // @NotNull no DTO já valida se ID é nulo.
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria com ID " + categoriaId + " não encontrada."));
    }

    private Estante carregarEstanteValidada(Long estanteId) {
        // @NotNull no DTO já valida se ID é nulo.
        return estanteRepository.findById(estanteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estante com ID " + estanteId + " não encontrada."));
    }

    // --- MÉTODOS CRUD (COM VALIDAÇÕES) ---
    // O @Transactional é usado para garantir que as operações de banco de dados
    // sejam
    // atômicas. Se uma falhar, todas as alterações feitas na transação serão
    // revertidas.
    // Isso é especialmente importante em operações de escrita (inserção,
    // atualização,
    // exclusão).

    @Override
    @Transactional
    public Livro salvarNovoLivro(LivroDTO livroDTO) {
        validarAnoPublicacaoDTORules(livroDTO.getAnoPublicacao());
        validarIsbnUnico(livroDTO.getIsbn(), null);

        Categoria categoria = carregarCategoriaValidada(livroDTO.getCategoriaId());
        Estante estante = carregarEstanteValidada(livroDTO.getEstanteId());

        Livro livro = new Livro();
        livro.setTitulo(livroDTO.getTitulo());
        livro.setAuthor(livroDTO.getAuthor());
        livro.setIsbn(livroDTO.getIsbn());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setCategoria(categoria);
        livro.setEstante(estante);

        return livroRepository.save(livro);
    }

    // @Override // Removido pois o método está comentado na interface LivroService
    @Transactional
    public List<Livro> salvarTodosLivros(List<LivroDTO> livroDTOs) {
        // return
        // livroDTOs.stream().map(this::salvarNovoLivro).collect(Collectors.toList());
        throw new UnsupportedOperationException("Batch save from DTOs not fully implemented yet.");
    }

    @Override
    @Transactional
    public Livro atualizarLivro(Long id, LivroDTO livroDTO) {
        return livroRepository.findById(id).map(livroExistente -> {
            validarAnoPublicacaoDTORules(livroDTO.getAnoPublicacao());
            validarIsbnUnico(livroDTO.getIsbn(), id);

            Categoria categoria = carregarCategoriaValidada(livroDTO.getCategoriaId());
            Estante estante = carregarEstanteValidada(livroDTO.getEstanteId());

            livroExistente.setTitulo(livroDTO.getTitulo());
            livroExistente.setAuthor(livroDTO.getAuthor());
            livroExistente.setIsbn(livroDTO.getIsbn());
            livroExistente.setAnoPublicacao(livroDTO.getAnoPublicacao());
            livroExistente.setEdicao(livroDTO.getEdicao());
            livroExistente.setCategoria(categoria);
            livroExistente.setEstante(estante);

            return livroRepository.save(livroExistente);
        }).orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));
    }

    @Override
    @Transactional
    public void apagarLivroPorId(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id + " para exclusão.");
        }
        livroRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void apagarTodosLivros() {
        livroRepository.deleteAll();
        // Se você quiser garantir que a operação foi bem-sucedida, pode verificar
        // se a lista de livros está vazia após a exclusão
        // if (livroRepository.count() > 0) {
        // throw new RecursoNaoEncontradoException("Não foi possível apagar todos os
        // livros.");
        // }
        // Ou apenas logar a operação
        // logger.info("Todos os livros foram apagados com sucesso.");
        // Ou não fazer nada, já que o deleteAll() já lida com isso
    }

    // --- MÉTODOS DE CONSULTA ---

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
        // Se o livro não for encontrado, retorna um Optional vazio
        // Se o livro for encontrado, retorna um Optional com o livro
        // Caso queira lançar uma exceção, pode fazer assim:
        // return livroRepository.findById(id)
        // .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado
        // com ID: " + id));
        // Ou, se preferir, pode usar o método acima e tratar o Optional no controller
        // ou onde for necessário
        // return livroRepository.findById(id);
        // .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado
        // com ID: " + id));
        // Ou, se preferir, pode usar o método acima e tratar o Optional no controller
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorTituloContendo(String trechoTitulo) {
        if (!StringUtils.hasText(trechoTitulo)) {
            // Verifica se o trecho do título não é vazio ou nulo
            // Se for, lança uma exceção personalizada
            // Isso é importante para evitar consultas desnecessárias ao banco de dados
            // e garantir que o usuário forneça um valor válido para a busca
            throw new RegraDeNegocioException("Trecho do título para busca não pode ser vazio.");
        }
        return livroRepository.findByTituloContainingIgnoreCase(trechoTitulo); // Supondo que este Query Method exista e
                                                                               // seja
        // case-insensitive
        // Caso contrário, retorna uma lista de livros que contêm o trecho no título
        // O método findByTituloContainingIgnoreCase é um Query Method do Spring Data
        // JPA
        // que busca livros cujo título contém o trecho fornecido, ignorando maiúsculas
        // e minúsculas
        // Se o trecho for vazio, retorna uma lista vazia
        // Se o trecho for nulo, lança uma exceção personalizada
        // Isso é importante para evitar consultas desnecessárias ao banco de dados
        // e garantir que o usuário forneça um valor válido para a busca
        // Caso contrário, retorna uma lista de livros que contêm o trecho no título
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorAutor(String author) {
        if (!StringUtils.hasText(author)) {
            throw new RegraDeNegocioException("Nome do autor para busca não pode ser vazio.");
        }
        return livroRepository.findByAuthor(author);
        // Caso contrário, retorna uma lista de livros que contêm o nome do autor
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> buscarPorIsbn(String isbn) {
        if (!StringUtils.hasText(isbn)) {
            throw new RegraDeNegocioException("ISBN para busca não pode ser vazio.");
        }
        // Não é necessário validar unicidade aqui, apenas buscar.
        // A validação de unicidade é feita ao salvar/atualizar.
        return livroRepository.findByIsbn(isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorCategoria(Long categoriaId) {
        if (categoriaId == null) {
            throw new RegraDeNegocioException("ID da categoria para busca não pode ser nulo.");
        }
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(
                        () -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + categoriaId));
        return livroRepository.findByCategoria(categoria); // Supondo que este Query Method exista
    }

    // Consulta que se relaciona a uma regra (validação do ano)
    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarLivrosPublicadosAposAno(Integer anoMinimo) {
        // Regra de negócio aplicada ao parâmetro da consulta
        if (anoMinimo == null || anoMinimo < 1400 || anoMinimo > Year.now().getValue()) {
            throw new RegraDeNegocioException("Ano mínimo para busca de publicação inválido.");
        }
        return livroRepository.findByAnoPublicacaoGreaterThanEqual(anoMinimo); // Supondo Query Method
    }
}