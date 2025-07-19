package org.iftm.biblioteca.service;

import org.iftm.biblioteca.dto.EmprestimoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface que define o contrato para o serviço de gerenciamento de empréstimos.
 * <p>
 * Esta camada abstrai as regras de negócio e a lógica de acesso a dados
 * relacionadas à entidade Empréstimo.
 */
public interface EmprestimoService {

    /**
     * Busca todos os empréstimos de forma paginada.
     *
     * @param pageable Objeto que contém as informações de paginação e ordenação.
     * @return Uma página (Page) de {@link EmprestimoDTO}.
     */
    Page<EmprestimoDTO> findAll(Pageable pageable);

    /**
     * Busca um empréstimo específico pelo seu ID.
     *
     * @param id O ID do empréstimo a ser buscado.
     * @return O {@link EmprestimoDTO} correspondente ao ID.
     * @throws org.iftm.biblioteca.service.exceptions.RecursoNaoEncontradoException se nenhum empréstimo for encontrado com o ID fornecido.
     */
    EmprestimoDTO findById(Long id);

    /**
     * Cria um novo empréstimo, aplicando as regras de negócio (disponibilidade do livro, situação do usuário, etc.).
     *
     * @param dto O {@link EmprestimoDTO} contendo o ID do usuário e o ID do livro.
     * @return O {@link EmprestimoDTO} do empréstimo recém-criado.
     * @throws org.iftm.biblioteca.service.exceptions.RegraDeNegocioException se alguma regra de negócio for violada.
     */
    EmprestimoDTO create(EmprestimoDTO dto);

    /**
     * Registra a devolução de um livro com base no ID do empréstimo.
     *
     * @param id O ID do empréstimo a ser finalizado.
     * @return O {@link EmprestimoDTO} atualizado com a data de devolução.
     */
    EmprestimoDTO registrarDevolucao(Long id);

    /**
     * Registra a devolução de um livro com base no ID do próprio livro.
     * Útil para cenários onde o ID do empréstimo não é conhecido.
     *
     * @param livroId O ID do livro que está sendo devolvido.
     * @return O {@link EmprestimoDTO} do empréstimo correspondente, agora finalizado.
     */
    EmprestimoDTO registrarDevolucaoPorLivro(Long livroId);
}