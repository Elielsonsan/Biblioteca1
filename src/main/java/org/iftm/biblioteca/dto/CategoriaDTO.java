package org.iftm.biblioteca.dto;

import org.iftm.biblioteca.entities.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;

public class CategoriaDTO {

        private Long id; // Adicionado para respostas
    
    @NotBlank(message = "Nome da categoria não pode ser vazio.")
    @Size(min = 3, max = 100, message = "Nome da categoria deve ter entre 3 e 100 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9\\sáéíóúâêîôûãõçÁÉÍÓÚÂÊÎÔÛÃÕÇ-]+$", message = "Nome da categoria contém caracteres inválidos.")
    private String nome;

    public CategoriaDTO() {
    }

    public CategoriaDTO(String nome) {
        this.nome = nome;
    }

    // Construtor para incluir ID, útil para respostas
    public CategoriaDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor a partir da entidade, útil para conversões
    public CategoriaDTO(Categoria entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() { return id; } // Getter para ID
    public void setId(Long id) { this.id = id; } // Setter para ID
}