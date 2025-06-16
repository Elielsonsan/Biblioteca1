package org.iftm.biblioteca.service.impl;

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
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException; // Ou outra exceção apropriada
import org.springframework.beans.factory.annotation.Autowired;
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
        // Validações de DTO (ex: @Valid no controller) devem ter ocorrido.
        // Verificar duplicação de ISBN, se necessário
        if (livroRepository.findByIsbn(livroDTO.getIsbn()).isPresent()) {
            throw new RegraDeNegocioException("Já existe um livro com o ISBN informado: " + livroDTO.getIsbn());
        }

        Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + livroDTO.getCategoriaId()));

        Estante estante = estanteRepository.findById(livroDTO.getEstanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estante não encontrada com ID: " + livroDTO.getEstanteId()));

        Livro livro = new Livro();
        livro.setTitulo(livroDTO.getTitulo());
        livro.setAuthor(livroDTO.getAuthor()); // Corrigido para usar getAuthor()
        livro.setIsbn(livroDTO.getIsbn());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setCategoria(categoria);
        livro.setEstante(estante);

        return livroRepository.save(livro);
    }

    // Implementar os outros métodos da interface LivroService...

    @Override
    public Livro atualizarLivro(Long id, LivroDTO livroDTO) {
        // Similar ao salvar, mas primeiro busca o livro existente
        // e atualiza seus campos.
        throw new UnsupportedOperationException("Unimplemented method 'atualizarLivro'");
    }

    @Override
    public void apagarLivroPorId(Long id) {
        if (!livroRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id);
        }
        livroRepository.deleteById(id);
    }

    @Override
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
        // Você precisará adicionar o método findByAnoPublicacaoGreaterThan no LivroRepository
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
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada com ID: " + categoriaId));
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
        // Assumindo que você quer uma busca exata pelo nome do autor,
        // e que o nome do campo na entidade Livro é 'author'.
        return livroRepository.findByAuthor(autor);
    }
}