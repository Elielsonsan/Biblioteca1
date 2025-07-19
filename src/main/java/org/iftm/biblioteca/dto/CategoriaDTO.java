package org.iftm.biblioteca.dto;

import org.iftm.biblioteca.entities.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para a entidade Categoria.
 * <p>
 * Usado para transferir dados entre o controller e o service, e como resposta da API.
 * A utilização de um 'record' Java é uma forma moderna e concisa de criar uma classe
 * de dados imutável, ideal para DTOs.
 * <p>
 * As anotações de validação garantem a integridade dos dados na entrada (criação/atualização).
 *
 * @param id   O identificador único da categoria (usado em respostas).
 * @param nome O nome da categoria.
 */
public record CategoriaDTO(
        Long id,
        @NotBlank(message = "Nome da categoria não pode ser vazio.")
        @Size(min = 3, max = 100, message = "Nome da categoria deve ter entre 3 e 100 caracteres.")
        @Pattern(regexp = "^[a-zA-Z0-9\\sáéíóúâêîôûãõçÁÉÍÓÚÂÊÎÔÛÃÕÇ-]+$", message = "Nome da categoria contém caracteres inválidos.")
        String nome
) {
    /**
     * Construtor adicional para converter uma entidade Categoria em um CategoriaDTO.
     * Isso é útil na camada de serviço para preparar os dados de resposta.
     */
    public CategoriaDTO(Categoria entity) {
        this(entity.getId(), entity.getNome());
    }
}