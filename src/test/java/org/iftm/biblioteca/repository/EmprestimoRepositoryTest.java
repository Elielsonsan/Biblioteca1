package org.iftm.biblioteca.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Emprestimo;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.entities.Usuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class EmprestimoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    private Usuarios usuario;
    private Livro livro;
    private Emprestimo emprestimoAtivo;

    @BeforeEach
    void setUp() {
        // 1. Criar dependências
        Categoria catLivro = new Categoria(null, "Ficção Científica");
        entityManager.persist(catLivro);

        Estante estante = new Estante(1L, "Corredor Principal");
        entityManager.persist(estante);

        // 2. Criar entidades principais
        usuario = new Usuarios(null, "João da Silva", "joao.silva@email.com", "111.222.333-44",
                new BigDecimal("2500.00"), LocalDate.of(1990, 1, 15), 0,
                "Rua dos Livros, 123", "Leitura", "SP", "12345-678");
        entityManager.persist(usuario);

        livro = new Livro(null, "Duna", "Frank Herbert", "978-8576570013", 1965, 1, "/images/duna.png", catLivro, estante);
        entityManager.persist(livro);

        // 3. Criar o empréstimo
        emprestimoAtivo = new Emprestimo(null, usuario, livro, Instant.now(), null); // Empréstimo sem data de devolução
        entityManager.persist(emprestimoAtivo);

        entityManager.flush();
    }

    @Test
    @DisplayName("Deve salvar (criar) um novo empréstimo com sucesso")
    void save_QuandoEmprestimoValido_DeveRetornarEmprestimoComId() {
        // Arrange
        Emprestimo novoEmprestimo = new Emprestimo(null, usuario, livro, Instant.now(), null);

        // Act
        Emprestimo emprestimoSalvo = emprestimoRepository.save(novoEmprestimo);

        // Assert
        assertThat(emprestimoSalvo).isNotNull();
        assertThat(emprestimoSalvo.getId()).isNotNull();
        assertThat(emprestimoSalvo.getUsuario().getId()).isEqualTo(usuario.getId());
        assertThat(emprestimoSalvo.getLivro().getId()).isEqualTo(livro.getId());
        assertThat(emprestimoSalvo.getDataDevolucao()).isNull();
    }

    @Test
    @DisplayName("Deve encontrar um empréstimo por ID existente")
    void findById_QuandoIdExiste_DeveRetornarOptionalComEmprestimo() {
        // Arrange
        Long idExistente = emprestimoAtivo.getId();

        // Act
        Optional<Emprestimo> optionalEmprestimo = emprestimoRepository.findById(idExistente);

        // Assert
        assertThat(optionalEmprestimo).isPresent();
        assertThat(optionalEmprestimo.get().getId()).isEqualTo(idExistente);
        assertThat(optionalEmprestimo.get().getUsuario().getName()).isEqualTo("João da Silva");
    }

    @Test
    @DisplayName("Deve retornar todos os empréstimos existentes")
    void findAll_DeveRetornarListaDeEmprestimos() {
        // Arrange (dados do setUp)

        // Act
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();

        // Assert
        assertThat(emprestimos).isNotNull().hasSize(1);
        assertThat(emprestimos.get(0).getLivro().getTitulo()).isEqualTo("Duna");
    }

    @Test
    @DisplayName("Deve atualizar um empréstimo (ex: registrar devolução)")
    void save_QuandoAtualizaEmprestimo_DevePersistirAlteracao() {
        // Arrange
        Long idParaAtualizar = emprestimoAtivo.getId();
        Emprestimo emprestimoParaDevolver = entityManager.find(Emprestimo.class, idParaAtualizar);
        assertThat(emprestimoParaDevolver.getDataDevolucao()).isNull();

        Instant dataDevolucao = Instant.now();
        emprestimoParaDevolver.setDataDevolucao(dataDevolucao);

        // Act
        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimoParaDevolver);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Emprestimo emprestimoDoBanco = entityManager.find(Emprestimo.class, idParaAtualizar);
        assertThat(emprestimoAtualizado.getDataDevolucao()).isEqualTo(dataDevolucao);
        assertThat(emprestimoDoBanco.getDataDevolucao()).isNotNull();
    }

    @Test
    @DisplayName("Deve excluir um empréstimo por ID")
    void deleteById_QuandoIdExiste_DeveRemoverEmprestimo() {
        // Arrange
        Long idParaDeletar = emprestimoAtivo.getId();
        long contagemAntes = emprestimoRepository.count();

        // Act
        emprestimoRepository.deleteById(idParaDeletar);

        // Assert
        assertThat(emprestimoRepository.count()).isEqualTo(contagemAntes - 1);
        assertThat(emprestimoRepository.findById(idParaDeletar)).isNotPresent();
    }

    @Test
    @DisplayName("Deve encontrar empréstimo ativo para um livro que não foi devolvido")
    void findFirstByLivroAndDataDevolucaoIsNull_QuandoLivroEmprestado_DeveRetornarEmprestimo() {
        // Arrange: O livro e o emprestimoAtivo (sem data de devolução) já existem do setUp

        // Act
        Optional<Emprestimo> resultado = emprestimoRepository.findFirstByLivroAndDataDevolucaoIsNull(livro);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(emprestimoAtivo.getId());
        assertThat(resultado.get().getDataDevolucao()).isNull();
    }

    @Test
    @DisplayName("Não deve encontrar empréstimo ativo para um livro que já foi devolvido")
    void findFirstByLivroAndDataDevolucaoIsNull_QuandoLivroDevolvido_DeveRetornarOptionalVazio() {
        // Arrange
        // Pega o empréstimo existente e registra uma devolução
        emprestimoAtivo.setDataDevolucao(Instant.now());
        entityManager.persistAndFlush(emprestimoAtivo);

        // Garante que a alteração foi persistida
        Emprestimo emprestimoDoBanco = entityManager.find(Emprestimo.class, emprestimoAtivo.getId());
        assertThat(emprestimoDoBanco.getDataDevolucao()).isNotNull();

        // Act
        Optional<Emprestimo> resultado = emprestimoRepository.findFirstByLivroAndDataDevolucaoIsNull(livro);

        // Assert
        assertThat(resultado).isNotPresent();
    }

    @Test
    @DisplayName("Deve retornar true para usuário com empréstimo atrasado")
    void existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore_QuandoUsuarioTemAtraso_DeveRetornarTrue() {
        // Arrange
        // O empréstimo do setUp foi feito "agora". Vamos criar um empréstimo antigo.
        Livro outroLivro = new Livro(null, "Outro Livro", "Outro Autor", "111", 2000, 1, null, emprestimoAtivo.getLivro().getCategoria(), emprestimoAtivo.getLivro().getEstante());
        entityManager.persist(outroLivro);

        Instant dataEmprestimoAntigo = Instant.now().minus(30, ChronoUnit.DAYS); // Empréstimo de 30 dias atrás
        Emprestimo emprestimoAntigo = new Emprestimo(null, usuario, outroLivro, dataEmprestimoAntigo, null);
        entityManager.persistAndFlush(emprestimoAntigo);

        Instant dataLimite = Instant.now().minus(15, ChronoUnit.DAYS); // Limite de 15 dias

        // Act
        boolean temAtraso = emprestimoRepository.existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore(usuario, dataLimite);

        // Assert
        assertThat(temAtraso).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para usuário sem empréstimos atrasados")
    void existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore_QuandoUsuarioNaoTemAtraso_DeveRetornarFalse() {
        // Arrange
        // O empréstimo do setUp foi feito "agora", então não está atrasado.
        Instant dataLimite = Instant.now().minus(15, ChronoUnit.DAYS); // Limite de 15 dias

        // Act
        boolean temAtraso = emprestimoRepository.existsByUsuarioAndDataDevolucaoIsNullAndDataEmprestimoBefore(usuario, dataLimite);

        // Assert
        assertThat(temAtraso).isFalse();
    }

    @Test
    @DisplayName("Deve retornar true se livro tem empréstimo ativo")
    void existsByLivroAndDataDevolucaoIsNull_QuandoLivroEmprestado_DeveRetornarTrue() {
        // Arrange: O livro e o emprestimoAtivo (sem data de devolução) já existem do setUp

        // Act
        boolean existe = emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro);

        // Assert
        assertThat(existe).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se livro não tem empréstimo ativo")
    void existsByLivroAndDataDevolucaoIsNull_QuandoLivroDevolvido_DeveRetornarFalse() {
        // Arrange
        // Pega o empréstimo existente e registra uma devolução
        emprestimoAtivo.setDataDevolucao(Instant.now());
        entityManager.persistAndFlush(emprestimoAtivo);

        // Act
        boolean existe = emprestimoRepository.existsByLivroAndDataDevolucaoIsNull(livro);

        // Assert
        assertThat(existe).isFalse();
    }
}