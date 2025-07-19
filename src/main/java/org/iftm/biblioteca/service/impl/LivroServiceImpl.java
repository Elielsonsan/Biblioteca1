package org.iftm.biblioteca.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.iftm.biblioteca.dto.LivroDTO;
import org.iftm.biblioteca.dto.LivroDTOResumido;
import org.iftm.biblioteca.dto.SugestaoDTO;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Emprestimo;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.EmprestimoRepository;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.LivroService;
import org.iftm.biblioteca.service.exceptions.IsbnDuplicadoException;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.specification.LivroSpecification;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;;

@Service
public class LivroServiceImpl implements LivroService  {

    private final LivroRepository livroRepository;
    private final CategoriaRepository categoriaRepository;
    private final EstanteRepository estanteRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final CapaService capaService; // Nova dependência

    // Injeção de dependência via construtor (melhor prática)
    public LivroServiceImpl(LivroRepository livroRepository, CategoriaRepository categoriaRepository,
            EstanteRepository estanteRepository, EmprestimoRepository emprestimoRepository, CapaService capaService) {
        this.livroRepository = livroRepository;
        this.categoriaRepository = categoriaRepository;
        this.estanteRepository = estanteRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.capaService = capaService; // Inicializa a nova dependência
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTOResumido> findAvailableBooks(String termo, Pageable pageable) {
        String termoBusca = (termo == null) ? "" : termo;
        Page<Livro> livrosDisponiveis = livroRepository.findAvailableByTerm(termoBusca.toLowerCase(), pageable);
        return livrosDisponiveis.map(LivroDTOResumido::new);
    }

    @Override
    @Transactional(readOnly = true)
    public LivroDTO findById(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));
        return toDTO(livro);
    }

    @Override
    @Transactional
    public LivroDTO insert(LivroDTO livroDTO) {
        verificarIsbnDuplicado(livroDTO.getIsbn(), null);
        Livro entity = new Livro();
        mapDtoToEntity(livroDTO, entity);
        entity = livroRepository.save(entity);
        return new LivroDTO(entity);
    }

    @Override
    @Transactional
    public LivroDTO update(Long id, LivroDTO livroDTO) {
        Livro entity = livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));
        verificarIsbnDuplicado(livroDTO.getIsbn(), id);
        mapDtoToEntity(livroDTO, entity);
        entity = livroRepository.save(entity);
        return new LivroDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 1. Busca o livro para garantir que ele existe e para obter seus dados.
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id));

        // 2. Regra de negócio CRÍTICA: Verifica se o livro possui empréstimos ativos.
        //    É necessário um método `existsByLivroAndDataDevolucaoIsNull` no EmprestimoRepository.
        if (emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro)) {
            throw new RegraDeNegocioException("Não é possível excluir o livro '" + livro.getTitulo() + "' pois ele está atualmente emprestado.");
        }

        // 3. Exclui o livro do banco de dados.
        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public List<SugestaoDTO> buscarSugestoes(String termo, String filtro) {
        if (termo == null || termo.trim().length() < 2) {
            return new ArrayList<>(); // Retorna lista vazia se o termo for muito curto
        }
        Pageable limit = PageRequest.of(0, 10); // Limita a 10 sugestões para performance
        return switch (filtro) {
            // Para cada filtro específico, busca no repositório e mapeia para um SugestaoDTO,
            // indicando o tipo da sugestão (ex: "autor", "titulo").
            case "autor" -> livroRepository.findAutoresParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "autor"))
                    .collect(Collectors.toList());
            case "titulo" -> livroRepository.findTitulosParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "titulo"))
                    .collect(Collectors.toList());
            case "isbn" -> livroRepository.findIsbnsParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "isbn"))
                    .collect(Collectors.toList());
            case "categoriaNome" -> categoriaRepository.findNomesParaSugestao(termo, limit).stream()
                    .map(s -> new SugestaoDTO(s, "categoria"))
                    .collect(Collectors.toList());
            case "estanteNome" -> estanteRepository.findNomesParaSugestao(termo, limit).stream()
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
                // 'yield' é usado em switch expressions para retornar um valor do bloco do case.
                yield Stream.of(autores, titulos, isbns).flatMap(s -> s)
                        .distinct()
                        .limit(10) // Garante que o total não exceda 10
                        .collect(Collectors.toList());
            }
        };
    }

    /**
     * Valida se o ISBN já existe para outro livro.
     */
    private void verificarIsbnDuplicado(String isbn, Long idExistente) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new RegraDeNegocioException("ISBN não pode ser nulo ou vazio.");
        }
        // Normaliza o ISBN removendo hifens e espaços para a verificação
        String normalizedIsbn = isbn.replaceAll("[\\s-]+", "");

        Optional<Livro> existenteComIsbn;
        if (idExistente == null) { // Caso de inserção de novo livro
            existenteComIsbn = livroRepository.findByIsbn(normalizedIsbn);
        } else { // Caso de atualização de livro existente
            existenteComIsbn = livroRepository.findByIsbnAndIdNot(normalizedIsbn, idExistente);
        }

        if (existenteComIsbn.isPresent()) {
            throw new IsbnDuplicadoException("Já existe um livro com o ISBN informado: " + isbn.trim());
        }
    }

    private void mapDtoToEntity(LivroDTO livroDTO, Livro livro) {
        Categoria categoria = categoriaRepository.findById(livroDTO.getCategoriaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Categoria não encontrada com ID: " + livroDTO.getCategoriaId()));
        Estante estante = estanteRepository.findById(livroDTO.getEstanteId()) // Assumindo que o ID da estante agora é Long
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estante não encontrada com ID: " + livroDTO.getEstanteId()));

        // 1. Normaliza o ISBN para um formato consistente (remove hifens e espaços).
        String normalizedIsbn = livroDTO.getIsbn().replaceAll("[\\s-]+", "");

        // 2. Chama o CapaService para baixar a capa da URL externa.
        // O serviço retornará um caminho local (ex: /images/capas/isbn.jpg) se for bem-sucedido,
        // ou a URL original em caso de falha.
        String caminhoCapaLocal = capaService.baixarSalvarCapa(livroDTO.getCapaUrl(), normalizedIsbn);

        livro.setTitulo(livroDTO.getTitulo());
        livro.setAutor(livroDTO.getAutor());
        livro.setIsbn(normalizedIsbn); // 3. Salva o ISBN já normalizado no banco.
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setEdicao(livroDTO.getEdicao());
        livro.setCapaUrl(caminhoCapaLocal); // 4. Salva o caminho da capa (local ou externa) no banco.
        livro.setCategoria(categoria);
        livro.setEstante(estante);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<LivroDTO> findPaginated(String termo, String titulo, String autor, String isbn, Integer anoPublicacao, Long categoriaId, Long estanteId, Pageable pageable) {
        Specification<Livro> spec = new LivroSpecification(termo, titulo, autor, isbn, anoPublicacao, categoriaId, estanteId);
        return livroRepository.findAll(spec, pageable).map(this::toDTO);
    }
    /**
     * Converte uma entidade Livro para LivroDTO, adicionando a lógica de
     * status de disponibilidade.
     * 
     * @param livro A entidade a ser convertida.
     * @return O DTO com as informações de status.
     */
    private LivroDTO toDTO(Livro livro) {
        LivroDTO dto = new LivroDTO(livro);
        Optional<Emprestimo> emprestimoAtivoOpt = emprestimoRepository.findFirstByLivroAndDataDevolucaoIsNull(livro);

        if (emprestimoAtivoOpt.isPresent()) {
            dto.setStatusDisponibilidade("Indisponível");
            int prazoDias = 10; // appProperties.getEmprestimo().getPrazoDias();  Substitui pela propriedade da classe
            Instant dataPrevista = emprestimoAtivoOpt.get().getDataEmprestimo().plus(prazoDias, ChronoUnit.DAYS);
            dto.setDataPrevistaDevolucao(dataPrevista);
        } else {
            dto.setStatusDisponibilidade("Disponível");
        }
        return dto;
    }
}