package org.iftm.biblioteca.dto;

import java.time.Instant;

import org.iftm.biblioteca.entities.Emprestimo;

import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) para a entidade Emprestimo.
 * <p>
 * Usado para criar novos empréstimos (recebendo `usuarioId` e `livroId`) e para
 * retornar dados detalhados de empréstimos existentes.
 *
 * @param id             O ID único do empréstimo.
 * @param usuarioId      O ID do usuário que realizou o empréstimo. Obrigatório na criação.
 * @param usuarioNome    O nome do usuário (preenchido em respostas da API).
 * @param livroId        O ID do livro que foi emprestado. Obrigatório na criação.
 * @param livroTitulo    O título do livro (preenchido em respostas da API).
 * @param dataEmprestimo A data e hora em que o empréstimo foi realizado.
 * @param dataDevolucao  A data e hora em que o livro foi devolvido (nulo se ainda estiver ativo).
 * @param atrasado       Um booleano que indica se o empréstimo está atrasado. Este campo é
 *                       calculado e preenchido pela camada de serviço.
 */
public record EmprestimoDTO(
        Long id,
        @NotNull(message = "ID do usuário é obrigatório.")
        Long usuarioId,
        String usuarioNome,
        @NotNull(message = "ID do livro é obrigatório.")
        Long livroId,
        String livroTitulo,
        Instant dataEmprestimo,
        Instant dataDevolucao,
        boolean atrasado
) {
    /**
     * Construtor que converte uma entidade Emprestimo para um DTO.
     * Note que este construtor não calcula o status 'atrasado'. A camada de serviço
     * é responsável por essa lógica e por construir o DTO final.
     */
    public EmprestimoDTO(Emprestimo entity) {
        this(entity.getId(),
             entity.getUsuario() != null ? entity.getUsuario().getId() : null,
             entity.getUsuario() != null ? entity.getUsuario().getName() : null,
             entity.getLivro() != null ? entity.getLivro().getId() : null,
             entity.getLivro() != null ? entity.getLivro().getTitulo() : null,
             entity.getDataEmprestimo(),
             entity.getDataDevolucao(),
             false); // O status de atraso é calculado no serviço.
    }
}