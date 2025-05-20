package org.iftm.biblioteca.service.impl;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.LivroService;
import org.iftm.biblioteca.service.exceptions.IsbnDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // Para verificar strings vazias

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LivroServiceImpl implements LivroService {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private CategoriaRepository categoriaRepository; // Para validar/buscar categoria
    @Autowired
    private EstanteRepository estanteRepository; // Para validar/buscar estante

    // --- REGRAS DE NEGÓCIO (MÉTODOS PRIVADOS DE VALIDAÇÃO) ---

    // Regra 1: Título não pode ser vazio e deve ter tamanho mínimo/máximo
    private void validarTitulo(String titulo) {
        if (!StringUtils.hasText(titulo)) { // StringUtils.hasText verifica null, vazio e só espaços
            throw new RegraDeNegocioException("Título do livro não pode ser vazio.");
        }
        if (titulo.length() < 2 || titulo.length() > 255) {
            throw new RegraDeNegocioException("Título do livro deve ter entre 2 e 255 caracteres.");
        }
    }

    // Regra 2: Ano de publicação deve ser válido
    private void validarAnoPublicacao(Integer anoPublicacao) {
        if (anoPublicacao == null) {
            throw new RegraDeNegocioException("Ano de publicação não pode ser nulo.");
        }
        int anoAtual = Year.now().getValue();
        if (anoPublicacao > anoAtual || anoPublicacao < 1400) { // 1400 é um exemplo de limite inferior
            throw new RegraDeNegocioException("Ano de publicação inválido. Deve ser entre 1400 e " + anoAtual + ".");
        }
    }

    // Regra 3: ISBN não pode ser duplicado (exceto para o próprio livro em caso
    // deatualização
    private void validarIsbnUnico(String isbn, Long idLivroExistente) {
        if (StringUtils.hasText(isbn)) {
            Optional<Livro> livroExistenteComIsbn = livroRepository.findByIsbn(isbn);
            if (livroExistenteComIsbn.isPresent() &&
                    (idLivroExistente == null || !livroExistenteComIsbn.get().getId().equals(idLivroExistente))) {
                throw new IsbnDuplicadoException("ISBN '" + isbn + "' já cadastrado para outro livro.");
            }
        } else {
            throw new RegraDeNegocioException("ISBN não pode ser vazio."); // Ou nulo, dependendo do seu modelo
        }
    }

    private void validarAssociacoes(Livro livro) {
        if (livro.getCategoria() == null || livro.getCategoria().getId() == null) {
            throw new RegraDeNegocioException("Categoria do livro não pode ser nula ou sem ID.");
        }
        if (!categoriaRepository.existsById(livro.getCategoria().getId())) {
            throw new RecursoNaoEncontradoException(
                    "Categoria com ID " + livro.getCategoria().getId() + " não encontrada.");
        }
        if (livro.getEstante() == null || livro.getEstante().getId() == null) {
            throw new RegraDeNegocioException("Estante do livro não pode ser nula ou sem ID.");
        }
        if (!estanteRepository.existsById(livro.getEstante().getId())) {
            throw new RecursoNaoEncontradoException(
                    "Estante com ID " + livro.getEstante().getId() + " não encontrada.");
        }
        // Recarregar as associações para garantir que são entidades gerenciadas e
        // válidas
        livro.setCategoria(categoriaRepository.findById(livro.getCategoria().getId()).get());
        livro.setEstante(estanteRepository.findById(livro.getEstante().getId()).get());
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
    public Livro salvarNovoLivro(Livro livro) {
        if (livro.getId() != null) {
            throw new RegraDeNegocioException("Para salvar um novo livro, o ID deve ser nulo.");
        }
        // Validações antes de salvar
        // Se o livro já tiver um ID, isso indica que ele já existe e não deve ser
        // tratado como um novo livro.
        // Se o ID for nulo, significa que é um novo livro e deve ser salvo.
        // Se o livro já existir, não deve ser salvo novamente.
        validarTitulo(livro.getTitulo());
        validarAnoPublicacao(livro.getAnoPublicacao());
        validarIsbnUnico(livro.getIsbn(), null); // null porque é um livro novo
        validarAssociacoes(livro);
        return livroRepository.save(livro);
    }

    @Override
    @Transactional
    public List<Livro> salvarTodosLivros(List<Livro> livros) {
        return livros.stream().map(this::salvarNovoLivro).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Livro atualizarLivro(Long id, Livro livroAtualizado) {
        return livroRepository.findById(id).map(livroExistente -> {
            // Validações antes de atualizar
            validarTitulo(livroAtualizado.getTitulo());
            validarAnoPublicacao(livroAtualizado.getAnoPublicacao());
            validarIsbnUnico(livroAtualizado.getIsbn(), id); // Passa o ID do livro existente
            validarAssociacoes(livroAtualizado);

            // Atualiza os campos do livro existente com os novos valores

            livroExistente.setTitulo(livroAtualizado.getTitulo());
            livroExistente.setAutor(livroAtualizado.getAutor());
            livroExistente.setIsbn(livroAtualizado.getIsbn());
            livroExistente.setAnoPublicacao(livroAtualizado.getAnoPublicacao());
            livroExistente.setEdicao(livroAtualizado.getEdicao());
            livroExistente.setCategoria(livroAtualizado.getCategoria());
            livroExistente.setEstante(livroAtualizado.getEstante());

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
        // throw new RecursoNaoEncontradoException("Não foi possível apagar todos os livros.");
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
        // .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));
        // Ou, se preferir, pode usar o método acima e tratar o Optional no controller
        // ou onde for necessário
        // return livroRepository.findById(id);
        // .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));
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
        return livroRepository.findByTituloContainingIgnoreCase(trechoTitulo); // Supondo que este Query Method exista e seja
                                                                       // case-insensitive 
        // Caso contrário, retorna uma lista de livros que contêm o trecho no título
        // O método findByTituloContainingIgnoreCase é um Query Method do Spring Data JPA
        // que busca livros cujo título contém o trecho fornecido, ignorando maiúsculas e minúsculas
        // Se o trecho for vazio, retorna uma lista vazia
        // Se o trecho for nulo, lança uma exceção personalizada
        // Isso é importante para evitar consultas desnecessárias ao banco de dados
        // e garantir que o usuário forneça um valor válido para a busca
        // Caso contrário, retorna uma lista de livros que contêm o trecho no título
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorAutor(String autor) {
        if (!StringUtils.hasText(autor)) {
            throw new RegraDeNegocioException("Nome do autor para busca não pode ser vazio.");
        }
        return livroRepository.findByTituloContainingIgnoreCase(autor); // Supondo que este Query Method exista
        // Caso contrário, retorna uma lista de livros que contêm o nome do autor
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