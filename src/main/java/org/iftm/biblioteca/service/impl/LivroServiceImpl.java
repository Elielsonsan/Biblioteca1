package org.iftm.biblioteca.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.iftm.biblioteca.config.AppProperties;
import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.SugestaoDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroServiceImpl implements LivroService {

    private static final Logger logger = LoggerFactory.getLogger(LivroServiceImpl.class);

    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstanteRepository estanteRepository;
    private final String uploadPath;

    // Injeção de dependência via construtor (melhor prática)
    public LivroServiceImpl(LivroRepository livroRepository, CategoriaRepository categoriaRepository,
            EstanteRepository estanteRepository, AppProperties appProperties) {
        this.livroRepository = livroRepository;
        this.categoriaRepository = categoriaRepository;
        this.estanteRepository = estanteRepository;
        this.uploadPath = appProperties.getUpload().getPath();
    }

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
    @Transactional(readOnly = true)
    public Page<LivroDTO> buscarTodos(Pageable pageable) {
        Page<Livro> page = livroRepository.findAll(pageable);
        return page.map(LivroDTO::new); // Converte Page<Livro> para Page<LivroDTO>
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
            // A chamada `buscarTodos()` foi removida porque agora ela requer paginação e retorna um tipo diferente (Page<LivroDTO>).
            // Retornar uma lista vazia é um comportamento seguro quando nenhum termo de busca é fornecido.
            // Se a regra de negócio fosse retornar todos os livros, seria necessário implementar a paginação aqui também.
            return new ArrayList<>();
        }
        // A conversão para minúsculas é desnecessária, pois a query JPQL no repositório já utiliza a função LOWER() no parâmetro.
        return livroRepository.searchByTermoGeral(termo);
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
    public List<Livro> buscarPorCategoriaNomeContendo(String nomeCategoria) {
        if (nomeCategoria == null || nomeCategoria.trim().isEmpty()) {
            throw new RegraDeNegocioException("Nome da categoria para busca não pode ser nulo ou vazio.");
        }
        return livroRepository.findByCategoriaNomeContainingIgnoreCase(nomeCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livro> buscarPorEstanteNomeContendo(String nomeEstante) {
        if (nomeEstante == null || nomeEstante.trim().isEmpty()) {
            throw new RegraDeNegocioException("Nome da estante para busca não pode ser nulo ou vazio.");
        }
        return livroRepository.findByEstanteNomeContainingIgnoreCase(nomeEstante);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SugestaoDTO> buscarSugestoes(String termo, String filtro) {
        if (termo == null || termo.trim().length() < 2) {
            return new ArrayList<>(); // Retorna lista vazia se o termo for muito curto
        }
        Pageable limit = PageRequest.of(0, 10); // Limita a 10 sugestões para performance
        return switch (filtro) {
            case "autor" -> livroRepository.findAutoresParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "autor"))
                    .collect(Collectors.toList());
            case "titulo" -> livroRepository.findTitulosParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "titulo"))
                    .collect(Collectors.toList());
            case "isbn" -> livroRepository.findIsbnsParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "isbn"))
                    .collect(Collectors.toList());
            case "categoriaNome" -> livroRepository.findNomesDeCategoriaParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "categoria"))
                    .collect(Collectors.toList());
            case "estanteNome" -> livroRepository.findNomesDeEstanteParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "estante"))
                    .collect(Collectors.toList());
            default -> {
                // Combina sugestões de autor, título e ISBN, cada uma com seu tipo.
                Stream<SugestaoDTO> autores = livroRepository.findAutoresParaSugestao(termo, limit).stream()
                        .map(s -> new SugestaoDTO(s, "autor"));
                Stream<SugestaoDTO> titulos = livroRepository.findTitulosParaSugestao(termo, limit).stream()
                        .map(s -> new SugestaoDTO(s, "titulo"));
                Stream<SugestaoDTO> isbns = livroRepository.findIsbnsParaSugestao(termo, limit).stream()
                        .map(s -> new SugestaoDTO(s, "isbn"));

                // Remove duplicatas e limita o resultado final para não sobrecarregar o front-end.
                yield Stream.concat(autores, Stream.concat(titulos, isbns))
                        .distinct()
                        .limit(10) // Garante que o total não exceda 10
                        .collect(Collectors.toList());
            }
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
        // Normaliza o ISBN removendo hifens e espaços para a verificação
        String normalizedIsbn = isbn.replaceAll("[\\s-]+", "");
        Optional<Livro> existenteComIsbn = livroRepository.findByIsbn(normalizedIsbn);
        if (existenteComIsbn.isPresent()
                && (idExistente == null || !existenteComIsbn.get().getId().equals(idExistente))) {
            throw new IsbnDuplicadoException("Já existe um livro com o ISBN informado: " + isbn.trim());
        }
    }

    /**
     * Baixa a imagem de uma URL e a salva localmente.
     * Se a URL já for um caminho local ou a URL for inválida, retorna a URL original.
     *
     * @param urlExterna A URL da imagem a ser baixada.
     * @param isbn O ISBN do livro, usado para nomear o arquivo.
     * @return O novo caminho local da imagem ou a URL original em caso de falha.
     */
    public String baixarSalvarCapa(String urlExterna, String isbn) {
        if (urlExterna == null || urlExterna.trim().isEmpty() || !urlExterna.startsWith("http")) {
            return urlExterna; // Retorna o valor original se não for uma URL web válida
        }

        try {
            URL url = new URL(urlExterna);
            String extensao = urlExterna.substring(urlExterna.lastIndexOf("."));
            // Garante que a extensão seja válida (simples verificação)
            if (extensao.length() > 5 || extensao.contains("?")) {
                extensao = ".jpg"; // Padrão
            }

            String novoNomeArquivo = isbn.replaceAll("[^0-9]", "") + extensao;
            Path diretorioDestino = Paths.get(uploadPath);
            Files.createDirectories(diretorioDestino); // Cria o diretório se não existir

            Path arquivoDestino = diretorioDestino.resolve(novoNomeArquivo);

            try (InputStream in = url.openStream()) {
                Files.copy(in, arquivoDestino, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retorna o caminho acessível pela web
            return "/images/capas/" + novoNomeArquivo;

        } catch (IOException e) {
            logger.error("Erro ao baixar a capa do livro com ISBN {}. URL: {}. Erro: {}", isbn, urlExterna, e.getMessage());
            return urlExterna; // Em caso de erro, mantém a URL original para não quebrar a aplicação
        }
    }

    private void mapDtoToEntity(LivroDTO livroDTO, Livro livro) {
        Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + livroDTO.getCategoriaId()));
        Estante estante = estanteRepository.findById(livroDTO.getEstanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estante não encontrada com ID: " + livroDTO.getEstanteId()));

        // Normaliza o ISBN para um formato consistente antes de salvar e usar como nome de arquivo.
        String normalizedIsbn = livroDTO.getIsbn().replaceAll("[\\s-]+", "");

        // Baixa a capa e atualiza a URL antes de salvar
        String caminhoCapaLocal = baixarSalvarCapa(livroDTO.getCapaUrl(), normalizedIsbn);

        livro.setTitulo(livroDTO.getTitulo());
        livro.setAutor(livroDTO.getAutor());
        livro.setIsbn(normalizedIsbn); // Salva o ISBN normalizado
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setCapaUrl(caminhoCapaLocal);
        livro.setCategoria(categoria);
        livro.setEstante(estante);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTO> search(String id, String titulo, String autor, String isbn, String categoriaNome,
            String estanteNome, String termo, Pageable pageable) {
        
        if (id != null && !id.trim().isEmpty()) {
            // Valida se o ID é um número antes de tentar converter, para evitar o uso de exceção para controle de fluxo.
            // Isso resolve o aviso "Unnecessary temporary when converting from String".
            if (id.matches("\\d+")) {
                Optional<Livro> livroOpt = livroRepository.findById(Long.valueOf(id));
                // Se o livro for encontrado, cria uma página com um único elemento.
                List<Livro> result = livroOpt.map(List::of).orElse(List.of());
                return new PageImpl<>(result.stream().map(LivroDTO::new).collect(Collectors.toList()), pageable, result.size());
            } else {
                // Se o ID não for um número válido (contém letras, etc.), retorna uma página vazia.
                return new PageImpl<>(new ArrayList<>(), pageable, 0);
            }
        }

        Page<Livro> page;
        if (titulo != null && !titulo.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);` no LivroRepository
            page = livroRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        } else if (autor != null && !autor.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> findByAutorContainingIgnoreCase(String autor, Pageable pageable);` no LivroRepository
            page = livroRepository.findByAutorContainingIgnoreCase(autor, pageable);
        } else if (isbn != null && !isbn.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> findByIsbnContainingIgnoreCase(String isbn, Pageable pageable);` no LivroRepository
            page = livroRepository.findByIsbnContainingIgnoreCase(isbn, pageable);
        } else if (categoriaNome != null && !categoriaNome.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> findByCategoriaNomeContainingIgnoreCase(String nome, Pageable pageable);` no LivroRepository
            page = livroRepository.findByCategoriaNomeContainingIgnoreCase(categoriaNome, pageable);
        } else if (estanteNome != null && !estanteNome.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> findByEstanteNomeContainingIgnoreCase(String nome, Pageable pageable);` no LivroRepository
            page = livroRepository.findByEstanteNomeContainingIgnoreCase(estanteNome, pageable);
        } else if (termo != null && !termo.trim().isEmpty()) {
            // AVISO: Requer `Page<Livro> searchByTermoGeral(String termo, Pageable pageable);` no LivroRepository
            page = livroRepository.searchByTermoGeral(termo, pageable);
        } else {
            // Caso nenhum filtro seja aplicado, retorna todos os livros de forma paginada
            page = livroRepository.findAll(pageable);
        }
        // Mapeia o resultado final (Page<Livro>) para o formato que o frontend espera (Page<LivroDTO>)
        return page.map(LivroDTO::new);
    }
}