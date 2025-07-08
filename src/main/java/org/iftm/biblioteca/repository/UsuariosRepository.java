package org.iftm.biblioteca.repository;

import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Usuarios;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
    // Busca um cliente pelo email (deve retornar Optional pois email é único)
    Optional<Usuarios> findByEmail(String email);

    // Busca clientes por nome ou CPF. A busca por ID é tratada no serviço.
    @Query("SELECT u FROM Usuarios u WHERE LOWER(u.name) LIKE LOWER(concat('%', :termo, '%')) OR u.cpf LIKE concat('%', :termo, '%')")
    Page<Usuarios> searchByNameOrCpf(@Param("termo") String termo, Pageable pageable);

    @Query("SELECT DISTINCT u.name FROM Usuarios u WHERE LOWER(u.name) LIKE LOWER(concat('%', :termo, '%')) ORDER BY u.name")
    List<String> findNomesParaSugestao(@Param("termo") String termo, Pageable pageable);
}
