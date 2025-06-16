package org.iftm.biblioteca.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_estante")
public class Estante implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @JsonIgnore // Adicionado para evitar loop de serialização JSON
    @OneToMany(mappedBy = "estante")
    private List<Livro> livros = new java.util.ArrayList<>();

    // Construtor sem argumentos (obrigatorio para JPA)
    public Estante() {
    }

    // Construtor com argumentos
    public Estante(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    // Implementação de equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Estante estante = (Estante) o;
        return Objects.equals(id, estante.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}