package org.iftm.biblioteca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CategoriaDTO {

    @NotBlank(message = "Nome da categoria não pode ser vazio.")
    @Size(min = 3, max = 100, message = "Nome da categoria deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\sáéíóúâêîôûãõçÁÉÍÓÚÂÊÎÔÛÃÕÇ-]+$", message = "Nome da categoria contém caracteres inválidos.")
    private String nome;

    public CategoriaDTO() {
    }

    public CategoriaDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}