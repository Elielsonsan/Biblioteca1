package org.iftm.biblioteca.dto;

import java.time.Instant;
import org.iftm.biblioteca.entities.Livro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO (Data Transfer Object) para a entidade Livro.
 * <p>
 * Usado para transferir dados completos de um livro, tanto para entrada (criação/atualização)
 * quanto para saída (respostas da API), incluindo status de disponibilidade calculado.
 */
public class LivroDTO {

    private Long id;

    @NotBlank(message = "Título não pode ser vazio.")
    @Size(min = 2, max = 100, message = "Título deve ter entre 2 e 100 caracteres.")
    private String titulo;

    @NotBlank(message = "Autor não pode ser vazio.")
    private String autor;

    // A validação de formato específico (ex: ISBN-10 vs ISBN-13) pode ser adicionada com @Pattern.
    // O serviço normaliza este campo removendo hifens e espaços antes de salvar.
    @NotBlank(message = "ISBN não pode ser vazio.")
    private String isbn;

    @NotNull(message = "Ano de publicação é obrigatório.")
    private Integer anoPublicacao;

    private Integer edicao;

    // Para entrada, pode ser uma URL externa (http://...).
    // Para saída, será um caminho local (ex: /images/capas/isbn.jpg) se o download for bem-sucedido.
    private String capaUrl;

    @NotNull(message = "Categoria é obrigatória.")
    private Long categoriaId;
    private String categoriaNome;

    // O ID da estante é um Long, refletindo a refatoração da entidade Estante.
    @NotNull(message = "Estante é obrigatória.")
    private Long estanteId;
    private String estanteNome;

    /**
     * Status de disponibilidade do livro ("Disponível" ou "Indisponível").
     * Este campo é calculado e preenchido pela camada de serviço.
     */
    private String statusDisponibilidade;
    /**
     * Se o livro estiver emprestado, esta é a data prevista para sua devolução.
     * Este campo é calculado e preenchido pela camada de serviço.
     */
    private Instant dataPrevistaDevolucao;

    public LivroDTO() {
    }

    // Construtor que converte uma Entidade Livro para LivroDTO
    public LivroDTO(Livro entity) {
        this.id = entity.getId();
        this.titulo = entity.getTitulo();
        this.autor = entity.getAutor();
        this.isbn = entity.getIsbn();
        this.anoPublicacao = entity.getAnoPublicacao();
        this.edicao = entity.getEdicao();
        this.capaUrl = entity.getCapaUrl();
        if (entity.getCategoria() != null) {
            this.categoriaId = entity.getCategoria().getId();
            this.categoriaNome = entity.getCategoria().getNome();
        }
        if (entity.getEstante() != null) {
            this.estanteId = entity.getEstante().getId(); // Corrigido para Long
            this.estanteNome = entity.getEstante().getNome();
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public Integer getEdicao() { return edicao; }
    public void setEdicao(Integer edicao) { this.edicao = edicao; }
    public String getCapaUrl() { return capaUrl; }
    public void setCapaUrl(String capaUrl) { this.capaUrl = capaUrl; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNome() { return categoriaNome; } 
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
    public Long getEstanteId() { return estanteId; }
    public void setEstanteId(Long estanteId) { this.estanteId = estanteId; }
    public String getEstanteNome() { return estanteNome; }
    public void setEstanteNome(String estanteNome) { this.estanteNome = estanteNome; }

    public String getStatusDisponibilidade() {
        return statusDisponibilidade;
    }

    public void setStatusDisponibilidade(String statusDisponibilidade) {
        this.statusDisponibilidade = statusDisponibilidade;
    }

    public Instant getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(Instant dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }
}