package org.iftm.biblioteca.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.iftm.biblioteca.config.AppProperties;
import org.iftm.biblioteca.dto.EmprestimoDTO;
import org.iftm.biblioteca.entities.Emprestimo;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.entities.Usuarios;
import org.iftm.biblioteca.repository.EmprestimoRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.repository.UsuariosRepository;
import org.iftm.biblioteca.service.EmprestimoService;
import org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException;
import org.iftm.biblioteca.service.exceptions.RegraDeNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuariosRepository usuarioRepository;
    private final LivroRepository livroRepository;
    private final AppProperties appProperties;

    public EmprestimoServiceImpl(EmprestimoRepository emprestimoRepository,
                                 UsuariosRepository usuarioRepository,
                                 LivroRepository livroRepository,
                                 AppProperties appProperties) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.livroRepository = livroRepository;
        this.appProperties = appProperties;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmprestimoDTO> findAll(Pageable pageable) {
        Page<Emprestimo> page = emprestimoRepository.findAll(pageable);
        return page.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EmprestimoDTO findById(Long id) {
        Emprestimo entity = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado com ID: " + id));
        return toDTO(entity);
    }

    @Override
    @Transactional
    public EmprestimoDTO create(EmprestimoDTO dto) {
        Usuarios usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + dto.getUsuarioId()));

        Livro livro = livroRepository.findById(dto.getLivroId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + dto.getLivroId()));

        validarDisponibilidadeLivro(livro);
        validarSituacaoUsuario(usuario);

        Emprestimo entity = new Emprestimo();
        entity.setUsuario(usuario);
        entity.setLivro(livro);
        entity.setDataEmprestimo(Instant.now());

        entity = emprestimoRepository.save(entity);
        return new EmprestimoDTO(entity); // Um novo empréstimo nunca está atrasado.
    }

    @Override
    @Transactional
    public EmprestimoDTO registrarDevolucao(Long id) {
        Emprestimo entity = emprestimoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado com ID: " + id));

        if (entity.getDataDevolucao() != null) {
            throw new RegraDeNegocioException("Este livro já foi devolvido em: " + entity.getDataDevolucao());
        }

        entity.setDataDevolucao(Instant.now());
        entity = emprestimoRepository.save(entity);
        return toDTO(entity);
    }

    @Override
    @Transactional
    public EmprestimoDTO registrarDevolucaoPorLivro(Long livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado com ID: " + livroId));

        Emprestimo emprestimoAtivo = emprestimoRepository.findFirstByLivroAndDataDevolucaoIsNull(livro)
                .orElseThrow(() -> new RegraDeNegocioException("Não há empréstimo ativo para o livro '" + livro.getTitulo() + "'."));

        emprestimoAtivo.setDataDevolucao(Instant.now());
        emprestimoAtivo = emprestimoRepository.save(emprestimoAtivo);
        return toDTO(emprestimoAtivo);
    }

    private void validarDisponibilidadeLivro(Livro livro) {
        emprestimoRepository.findFirstByLivroAndDataDevolucaoIsNull(livro).ifPresent(e -> {
            throw new RegraDeNegocioException(
                    "O livro '" + livro.getTitulo() + "' já está emprestado e não pode ser emprestado novamente até a devolução.");
        });
    }

    private void validarSituacaoUsuario(Usuarios usuario) {
        // REGRA 1: Verificar se o usuário tem empréstimos atrasados
        int prazoDias = appProperties.getEmprestimo().getPrazoDias();
        Instant dataLimiteAtraso = Instant.now().minus(prazoDias, ChronoUnit.DAYS);
        if (emprestimoRepository.existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore(usuario, dataLimiteAtraso)) {
            throw new RegraDeNegocioException(
                    "O usuário '" + usuario.getName() + "' possui empréstimos atrasados e não pode realizar novos empréstimos.");
        }

        // REGRA 2: Verificar se o usuário atingiu o limite de empréstimos ativos
        int limiteAtivos = appProperties.getEmprestimo().getLimiteAtivos();
        long emprestimosAtivos = emprestimoRepository.countByUsuarioAndDataDevolucaoIsNull(usuario);

        if (emprestimosAtivos >= limiteAtivos) {
            throw new RegraDeNegocioException(
                    "O usuário '" + usuario.getName() + "' já atingiu o limite de " + limiteAtivos
                            + " empréstimos ativos.");
        }
    }

    /**
     * Converte uma entidade Emprestimo para EmprestimoDTO, adicionando a lógica
     * para verificar se o empréstimo está atrasado.
     *
     * @param emprestimo A entidade a ser convertida.
     * @return O DTO com as informações de status.
     */
    private EmprestimoDTO toDTO(Emprestimo emprestimo) {
        EmprestimoDTO dto = new EmprestimoDTO(emprestimo);
        // Um empréstimo só pode estar atrasado se ainda não foi devolvido.
        if (emprestimo.getDataDevolucao() == null) {
            int prazoDias = appProperties.getEmprestimo().getPrazoDias();
            Instant dataLimite = Instant.now().minus(prazoDias, ChronoUnit.DAYS);
            if (emprestimo.getDataEmprestimo().isBefore(dataLimite)) {
                dto.setAtrasado(true);
            }
        }
        return dto;
    }
}