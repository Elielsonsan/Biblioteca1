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

/**
 * Representa a entidade Livro no sistema, mapeada para a tabela tb_livro no banco de dados.
 */
@Entity
@Table(name = "tb_livro")
public class Livro implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Identificador único do livro, gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título do livro.
     */
    private String titulo;

    /**
     * Autor(a) do livro.
     */
    private String autor;

    /**
     * ISBN (International Standard Book Number) do livro.
     */
    private String isbn;

    /**
     * Ano de publicação do livro.
     */
    private Integer anoPublicacao;

    /**
     * Número da edição do livro.
     */
    private Integer edicao;

    /**
     * URL para a capa do livro.
     */
    private String capaUrl;

    /**
     * Categoria à qual o livro pertence.
     * Representa um relacionamento muitos-para-um com a entidade Categoria.
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    /**
     * Estante onde o livro está localizado.
     * Representa um relacionamento muitos-para-um com a entidade Estante.
     */
    @ManyToOne
    @JoinColumn(name = "estante_id")
    private Estante estante;

    /**
     * Lista de empréstimos associados a este livro.
     * Representa um relacionamento um-para-muitos com a entidade Emprestimo.
     * O atributo mappedBy indica o campo na entidade Emprestimo que mapeia este relacionamento.
     */
    @OneToMany(mappedBy = "livro")
    private Set<Emprestimo> emprestimos = new HashSet<>();

    /**
     * Construtor padrão (vazio) da entidade Livro.
     * Necessário para o funcionamento do JPA.
     */
    public Livro() {
    }

    /**
     * Construtor com todos os atributos da entidade Livro.
     * Útil para criar instâncias da entidade com valores iniciais.
     */
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

    // Métodos Getters e Setters para todos os atributos

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

    /**
     * Implementação do método hashCode baseado no atributo id.
     * Garante que objetos Livro com o mesmo id tenham o mesmo código hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Implementação do método equals baseado no atributo id.
     * Define que dois objetos Livro são iguais se possuírem o mesmo id.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Livro livro = (Livro) obj;
        return Objects.equals(id, livro.id);
    }
}
