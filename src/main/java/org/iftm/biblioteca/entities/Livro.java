package org.iftm.biblioteca.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_livro")
public class Livro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private Integer anoPublicacao;
    private Integer edicao;
    private String capaUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "estante_id")
    private Estante estante;

    @OneToMany(mappedBy = "livro")
    private Set<Emprestimo> emprestimos = new HashSet<>();

    public Livro() {
    }

    public Livro(Long id, String titulo, String autor, String isbn, Integer anoPublicacao, Integer edicao,
            String capaUrl, Categoria categoria, Estante estante) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.edicao = edicao;
        this.capaUrl = capaUrl;
        this.categoria = categoria;
        this.estante = estante;
    }

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
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Estante getEstante() { return estante; }
    public void setEstante(Estante estante) { this.estante = estante; }

    public Set<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return Objects.equals(id, livro.id);
    }
}