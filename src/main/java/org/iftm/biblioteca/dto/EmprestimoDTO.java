package org.iftm.biblioteca.dto;

import java.time.Instant;

import org.iftm.biblioteca.entities.Emprestimo;

import jakarta.validation.constraints.NotNull;

public class EmprestimoDTO {

    private Long id;

    @NotNull(message = "ID do usuário é obrigatório.")
    private Long usuarioId;
    private String usuarioNome;

    @NotNull(message = "ID do livro é obrigatório.")
    private Long livroId;
    private String livroTitulo;

    private Instant dataEmprestimo;
    private Instant dataDevolucao;
    private boolean atrasado;

    public EmprestimoDTO() {
    }

    public EmprestimoDTO(Emprestimo entity) {
        this.id = entity.getId();
        if (entity.getUsuario() != null) {
            this.usuarioId = entity.getUsuario().getId();
            this.usuarioNome = entity.getUsuario().getName();
        }
        if (entity.getLivro() != null) {
            this.livroId = entity.getLivro().getId();
            this.livroTitulo = entity.getLivro().getTitulo();
        }
        this.dataEmprestimo = entity.getDataEmprestimo();
        this.dataDevolucao = entity.getDataDevolucao();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public String getLivroTitulo() {
        return livroTitulo;
    }

    public void setLivroTitulo(String livroTitulo) {
        this.livroTitulo = livroTitulo;
    }

    public Instant getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Instant dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Instant getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Instant dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }
}