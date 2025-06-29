package org.iftm.biblioteca.service.impl;

import java.util.ArrayList;
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
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroServiceImpl implements LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EstanteRepository estanteRepository;

    @Override
    @Transactional
    public Livro salvarNovoLivro(LivroDTO livroDTO) {
        // Validações de DTO (ex: @Valid no controller) devem garantir que os campos
        // obrigatórios estejam preenchidos.
        // Valida se o ISBN já existe para outro livro.
        verificarIsbnDuplicado(livroDTO.getIsbn(), null);
        Livro livro = new Livro();
        // Mapeia os dados do DTO para a entidade
        mapDtoToEntity(livroDTO, livro);
        return livroRepository.save(livro);
    }

    // Implementar os outros métodos da interface LivroService...

    @Override
    @Transactional
    public Livro atualizarLivro(Long id, LivroDTO livroDTO) {
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));

        // Valida se o novo ISBN já existe para outro livro
        verificarIsbnDuplicado(livroDTO.getIsbn(), id);
        // Mapeia os dados do DTO para a entidade existente
        mapDtoToEntity(livroDTO, livroExistente);
        return livroRepository.save(livroExistente);
    }

    @Override
    @Transactional
    public void apagarLivroPorId(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id);
        }
        livroRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void apagarTodosLivros() {
        livroRepository.deleteAll();
    }

    @Override
    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarLivrosPublicadosAposAno(Integer ano) {
        if (ano == null) {
            throw new RegraDeNegocioException("O ano para busca não pode ser nulo.");
        }

        return livroRepository.findByAnoPublicacaoGreaterThan(ano);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Livro> buscarPorIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new RegraDeNegocioException("ISBN para busca não pode ser nulo ou vazio.");
        }
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
        return livroRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorTituloContendo(String trechoTitulo) {
        if (trechoTitulo == null || trechoTitulo.trim().length() < 2) {
            throw new RegraDeNegocioException("Trecho do título para busca deve ter pelo menos 2 caracteres.");
        }
        return livroRepository.findByTituloContainingIgnoreCase(trechoTitulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new RegraDeNegocioException("Nome do autor para busca não pode ser nulo ou vazio.");
        }
        // Realiza uma busca case-insensitive que busca por parte do nome do autor.
        return livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarLivrosPorTermoGeral(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return buscarTodos(); // Ou lançar exceção se preferir que um termo seja sempre fornecido
        }
        return livroRepository.searchByTermoGeral(termo.toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorIsbnContendo(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new RegraDeNegocioException("ISBN para busca não pode ser nulo ou vazio.");
        }
        return livroRepository.findByIsbnContainingIgnoreCase(isbn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> buscarSugestoes(String termo, String filtro) {
        if (termo == null || termo.trim().length() < 2) {
            return new ArrayList<>(); // Retorna lista vazia se o termo for muito curto
        }
        Pageable limit = PageRequest.of(0, 10); // Limita a 10 sugestões para performance
        return switch (filtro) {
            case "autor" -> livroRepository.findAutoresParaSugestao(termo, limit);
            case "titulo" -> livroRepository.findTitulosParaSugestao(termo, limit);
            case "isbn" -> livroRepository.findIsbnsParaSugestao(termo, limit);
            default -> livroRepository.findAutoresOuTitulosParaSugestao(termo, limit);
        };
    }

    /**
     * Método auxiliar para mapear dados de um LivroDTO para uma entidade Livro.
     * Isso evita a repetição de código nos métodos de criação e atualização.
     * 
     * @param livroDTO O objeto de transferência de dados.
     * @param livro    A entidade a ser populada.
     */
    private void verificarIsbnDuplicado(String isbn, Long idExistente) {
        if (isbn == null || isbn.trim().isEmpty()) {
            // A validação de @NotBlank no DTO deve pegar isso.
            return;
        }
        Optional<Livro> existenteComIsbn = livroRepository.findByIsbn(isbn);
        if (existenteComIsbn.isPresent()
                && (idExistente == null || !existenteComIsbn.get().getId().equals(idExistente))) {
            throw new IsbnDuplicadoException("Já existe um livro com o ISBN informado: " + isbn);
        }
    }

    private void mapDtoToEntity(LivroDTO livroDTO, Livro livro) {
        Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + livroDTO.getCategoriaId()));
        Estante estante = estanteRepository.findById(livroDTO.getEstanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estante não encontrada com ID: " + livroDTO.getEstanteId()));

        livro.setTitulo(livroDTO.getTitulo());
        livro.setAutor(livroDTO.getAutor());
        livro.setIsbn(livroDTO.getIsbn());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setCapaUrl(livroDTO.getCapaUrl());
        livro.setCategoria(categoria);
        livro.setEstante(estante);
    }
}