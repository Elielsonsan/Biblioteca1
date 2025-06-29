package org.iftm.biblioteca.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LivroDTO {

    @NotBlank(message = "Título do livro não pode ser vazio.")
    @Size(min = 2, max = 255, message = "Título do livro deve ter entre 2 e 255 caracteres.")
    private String titulo;

    @NotBlank(message = "Autor do livro não pode ser vazio.")
    @Size(min = 2, max = 150, message = "Nome do autor deve ter entre 2 e 150 caracteres.")
    private String autor;

    @NotBlank(message = "ISBN não pode ser vazio.")
    // Regex simplificada para ISBN-10 ou ISBN-13 (sem hífens)
    @Pattern(regexp = "^(?:[0-9]{9}[0-9X]|97[89][0-9]{10})$", message = "ISBN inválido. Deve ser um ISBN-10 ou ISBN-13 válido sem hífens.")
    private String isbn;

    @NotNull(message = "Ano de publicação não pode ser nulo.")
    @Min(value = 1400, message = "Ano de publicação deve ser no mínimo 1400.")
    @Max(value = 2099, message = "Ano de publicação inválido.") // Idealmente, o máximo seria o ano atual (requer validador customizado)
    private Integer anoPublicacao;

    // Opcional, pode ser uma URL
    private String capaUrl;

    private Integer edicao;

    @NotNull(message = "ID da categoria não pode ser nulo.")
    private Long categoriaId;

    @NotNull(message = "ID da estante não pode ser nulo.")
    private Long estanteId;

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }
    public Integer getEdicao() { return edicao; }
    public void setEdicao(Integer edicao) { this.edicao = edicao; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public Long getEstanteId() { return estanteId; }
    public void setEstanteId(Long estanteId) { this.estanteId = estanteId; }
}