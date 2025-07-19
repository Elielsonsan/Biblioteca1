package org.iftm.biblioteca.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.iftm.biblioteca.entities.Usuarios;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para a entidade Usuarios.
 * <p>
 * Usado para transferir dados de usuários entre as camadas da aplicação,
 * aplicando validações na entrada de dados.
 * A conversão para um 'record' Java torna a classe imutável e mais concisa.
 */
public record UsuariosDTO(
        Long id,
        @NotBlank(message = "Nome do usuário não pode ser vazio.")
        @Size(min = 2, max = 100, message = "Nome do usuário deve ter entre 2 e 100 caracteres.")
        String name,
        @NotBlank(message = "Email do usuário não pode ser vazio.")
        @Email(message = "Formato de email inválido.")
        @Size(max = 100, message = "Email não pode exceder 100 caracteres.")
        String email,
        // Uma validação mais robusta para CPF pode ser feita com @Pattern ou uma anotação customizada.
        @NotBlank(message = "CPF não pode ser vazio.")
        @Size(min = 11, max = 14, message = "CPF deve ter entre 11 e 14 caracteres.")
        String cpf,
        @PositiveOrZero(message = "Renda deve ser um valor positivo ou zero.")
        BigDecimal income,
        @Past(message = "Data de nascimento deve ser no passado.")
        LocalDate birthDate,
        @PositiveOrZero(message = "Número de filhos deve ser positivo ou zero.")
        Integer childrenCount,
        @NotBlank(message = "Rua não pode ser vazia.")
        String street,
        @NotBlank(message = "Cidade não pode ser vazia.")
        String city,
        @NotBlank(message = "Estado não pode ser vazio.")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres.")
        String state,
        @NotBlank(message = "CEP não pode ser vazio.")
        String zipCode
) {
    /**
     * Construtor para converter uma entidade Usuarios em um UsuariosDTO.
     */
    public UsuariosDTO(Usuarios entity) {
        this(entity.getId(), entity.getName(), entity.getEmail(), entity.getCpf(), entity.getIncome(),
             entity.getBirthDate(), entity.getChildrenCount(), entity.getStreet(), entity.getCity(),
             entity.getState(), entity.getZipCode());
    }
}