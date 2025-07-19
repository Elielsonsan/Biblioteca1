package org.iftm.biblioteca.specification;

import jakarta.persistence.criteria.*;
import org.iftm.biblioteca.entities.Livro;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LivroSpecification implements Specification<Livro> {

    private final String termo;
    private final String titulo;
    private final String autor;
    private final String isbn;
    private final Integer anoPublicacao;
    private final Long categoriaId;
    private final Long estanteId;

    public LivroSpecification(String termo, String titulo, String autor, String isbn, Integer anoPublicacao, Long categoriaId, Long estanteId) {
        this.termo = termo;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.categoriaId = categoriaId;
        this.estanteId = estanteId;
    }

    @Override
    @Nullable
    public Predicate toPredicate(@NonNull Root<Livro> root, @Nullable CriteriaQuery<?> query,
            @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // OTIMIZAÇÃO (N+1): Esta condição aplica o 'fetch' (similar a um JOIN FETCH em JPQL)
        // apenas na query principal, e não na query de contagem (count) que o Spring faz
        // para a paginação. Isso evita erros e melhora a performance, carregando as
        // entidades relacionadas (categoria e estante) em uma única consulta.
        if (query != null && query.getResultType() != Long.class && query.getResultType() != long.class) {
            root.fetch("categoria", JoinType.LEFT);
            root.fetch("estante", JoinType.LEFT);
        }

        // Filtro de busca rápida (termo geral)
        if (StringUtils.hasText(termo)) {
            Predicate p1 = cb.like(cb.lower(root.get("titulo")), "%" + termo.toLowerCase() + "%");
            Predicate p2 = cb.like(cb.lower(root.get("autor")), "%" + termo.toLowerCase() + "%");
            Predicate p3 = cb.like(cb.lower(root.get("isbn")), "%" + termo.toLowerCase() + "%");
            predicates.add(cb.or(p1, p2, p3));
        }

        // Filtros avançados
        if (StringUtils.hasText(titulo)) {
            predicates.add(cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(autor)) {
            predicates.add(cb.like(cb.lower(root.get("autor")), "%" + autor.toLowerCase() + "%"));
        }
        if (StringUtils.hasText(isbn)) {
            predicates.add(cb.like(cb.lower(root.get("isbn")), "%" + isbn.toLowerCase() + "%"));
        }
        if (anoPublicacao != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("anoPublicacao"), anoPublicacao));
        }
        if (categoriaId != null) {
            predicates.add(cb.equal(root.get("categoria").get("id"), categoriaId));
        }
        if (estanteId != null) {
            predicates.add(cb.equal(root.get("estante").get("id"), estanteId));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}