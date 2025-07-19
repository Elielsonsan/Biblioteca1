package org.iftm.biblioteca.dto;

import org.iftm.biblioteca.entities.Livro;

/**
 * DTO (Data Transfer Object) resumido para a entidade Livro.
 * <p>
 * Usado em contextos onde apenas as informações essenciais do livro são necessárias,
 * como em listas de seleção para novos empréstimos. Isso otimiza a performance
 * ao transferir menos dados.
 * <p>
 * A conversão para um 'record' Java torna a classe imutável e mais concisa.
 *
 * @param id     O identificador único do livro.
 * @param titulo O título do livro.
 * @param autor  O autor do livro.
 */
public record LivroDTOResumido(Long id, String titulo, String autor) {
    /**
     * Construtor para converter uma entidade Livro em um LivroDTOResumido.
     */
    public LivroDTOResumido(Livro livro) {
        this(livro.getId(), livro.getTitulo(), livro.getAutor());
    }
}
