package org.iftm.biblioteca.dto;

import org.iftm.biblioteca.entities.Livro;

public class LivroDTOResumido {
    private Long id;
    private String titulo;
    private String autor;

    public LivroDTOResumido(Livro livro) {
        this.id = livro.getId();
        this.titulo = livro.getTitulo();
        this.autor = livro.getAutor();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
