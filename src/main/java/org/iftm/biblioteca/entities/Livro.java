package org.iftm.biblioteca.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_livro") // Nome da tabela no banco de dados
public class Livro implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Garante que o título não seja nulo no banco
    private String titulo;

    @Column(nullable = false) // Autor geralmente é obrigatório
    private String autor;

    // Adicionado campo para ISBN
    @Column(nullable = false, unique = true) // ISBN é obrigatório e único
    private String isbn;

    private Integer anoPublicacao;

    // Adicionado campo para Edicao (Integer)
    private Integer edicao;

    // Adicionado campo para a URL da capa do livro
    private String capaUrl;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false) // Nome da coluna de chave estrangeira
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "estante_id", nullable = false) // Nome da coluna de chave estrangeira
    private Estante estante;

    // --- CONSTRUTORES ---

    // Construtor sem argumentos (OBRIGATÓRIO para JPA) - CORRIGIDO
    public Livro() {
    }

    // Construtor com TODOS os argumentos (para facilitar a criação de objetos)
    // (Incluindo ISBN e Edição)
    // O ID é gerado automaticamente, então não precisa ser passado
    public Livro(Long id, String titulo, String autor, String isbn, Integer anoPublicacao, Integer edicao,
            String capaUrl, Categoria categoria, Estante estante) {
        this.id = id;
        this.titulo = titulo; // Atribui o título recebido
        this.autor = autor;
        this.isbn = isbn; // Atribui o isbn recebido
        this.anoPublicacao = anoPublicacao;
        this.edicao = edicao; // Atribui a edição recebida
        this.capaUrl = capaUrl;
        this.categoria = categoria; // Atribui o objeto Categoria recebido
        this.estante = estante; // Atribui o objeto Estante recebido
    }

    // --- GETTERS E SETTERS ---

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Estante getEstante() {
        return estante;
    }

    public void setEstante(Estante estante) {
        this.estante = estante;
    }

    public String getCapaUrl() {
        return capaUrl;
    }

    public void setCapaUrl(String capaUrl) {
        this.capaUrl = capaUrl;
    }

    // --- equals() e hashCode() ---
    // Implementação de equals e hashCode para comparar objetos Livro

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Livro livro = (Livro) o;
        return Objects.equals(id, livro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}