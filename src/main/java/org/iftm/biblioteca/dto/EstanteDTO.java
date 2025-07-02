package org.iftm.biblioteca.dto;

import java.io.Serializable;

import org.iftm.biblioteca.entities.Estante;

public class EstanteDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;

    public EstanteDTO() {
    }

    public EstanteDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public EstanteDTO(Estante entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}