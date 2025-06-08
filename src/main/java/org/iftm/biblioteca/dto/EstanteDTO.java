package org.iftm.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EstanteDTO {

    @NotBlank(message = "Nome da estante não pode ser vazio.")
    @Size(min = 2, max = 50, message = "Nome da estante deve ter entre 2 e 50 caracteres.")
    private String nome;

    public EstanteDTO() {
    }

    public EstanteDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}