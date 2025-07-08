package org.iftm.biblioteca.repository;

import java.time.Instant;
import java.util.Optional;

import org.iftm.biblioteca.entities.Emprestimo;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.entities.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {
    // Verifica se existe um empréstimo ativo (sem data de devolução) para um determinado livro.
    Optional<Emprestimo> findFirstByLivroAndDataDevolucaoIsNull(Livro livro);

    // Verifica se um usuário possui empréstimos ativos e vencidos.
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.usuario = :usuario AND e.dataDevolucao IS NULL AND e.dataEmprestimo < :dataLimite")
    boolean existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore(@Param("usuario") Usuarios usuario, @Param("dataLimite") Instant dataLimite);

    // Conta quantos empréstimos ativos (sem data de devolução) um usuário possui.
    long countByUsuarioAndDataDevolucaoIsNull(Usuarios usuario);
}