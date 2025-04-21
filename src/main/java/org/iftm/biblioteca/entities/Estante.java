package org.iftm.biblioteca.entities;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_estante")
public class Estante implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome = "Estante B";

    @OneToMany(mappedBy = "estante")
    private List<Livro> livros;


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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estante estante = (Estante) o;
        return Objects.equals(id, estante.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

   
}