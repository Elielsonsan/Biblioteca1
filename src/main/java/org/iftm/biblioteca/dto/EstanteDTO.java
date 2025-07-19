package org.iftm.biblioteca.dto;

import org.iftm.biblioteca.entities.Estante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para a entidade Estante.
 * <p>
 * Usado para transferir dados entre o controller e o service. A conversão para um
 * 'record' Java moderniza o código, tornando-o imutável e mais conciso.
 * <p>
 * As anotações de validação garantem a integridade dos dados na entrada.
 *
 * @param id   O identificador único da estante (Long), consistente com a entidade.
 * @param nome O nome da estante, que deve ser preenchido e ter um tamanho razoável.
 */
public record EstanteDTO(
        Long id,
        @NotBlank(message = "Nome da estante não pode ser vazio.")
        @Size(min = 3, max = 100, message = "Nome da estante deve ter entre 3 e 100 caracteres.")
        String nome
) {
    /**
     * Construtor adicional para converter uma entidade Estante em um EstanteDTO.
     */
    public EstanteDTO(Estante entity) {
        this(entity.getId(), entity.getNome());
    }
}