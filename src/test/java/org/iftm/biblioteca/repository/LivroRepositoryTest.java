package org.iftm.biblioteca.repository;

// Imports do Spring Data JPA
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest // Carrega o contexto do Spring apenas para testes de repositório
class LivroRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // O gerenciador de entidades do JPA para testes

    @Autowired
    private LivroRepository livroRepository; // O repositório que estamos testando
    private Categoria catFantasia;
    private Categoria catFiccao;
    private Estante estA1;
    private Estante estB2;
    private Livro livroHobbit;
    private Livro livroDuna;

    // O JUnit usa este método via reflexão, o aviso da IDE pode ser ignorado ou configurado.
    @BeforeEach // metodo setUp() é chamado antes de cada teste
    protected void setUp() {
        // Criar e persistir categorias e estantes antes de cada teste usando
        // TestEntityManager
        catFantasia = new Categoria(null, "Fantasia");
        catFiccao = new Categoria(null, "Ficção Científica");
        entityManager.persist(catFantasia);
        entityManager.persist(catFiccao);

        // Cirar e persistir as categorias usando TestEntityManager
        estA1 = new Estante(null, "A1");
        estB2 = new Estante(null, "B2");
        entityManager.persist(estA1);
        entityManager.persist(estB2);

        // Criar e persistir Livros usando as entidades relacionadas ja criadas
        // Usa categorias e estantes criadas no setUp
        livroHobbit = new Livro(null, "O Hobbit", "J.R.R. Tolkien", "1122334455", 1937, 1, "/images/hobbit.png", catFantasia, estA1);
        livroDuna = new Livro(null, "Duna", "Frank Herbert", "1234567890", 1965, 1, "/images/duna.png", catFiccao, estB2);
        entityManager.persist(livroHobbit);
        entityManager.persist(livroDuna);

        // garante que as entidades sejam persistidas no banco de dados
        entityManager.flush();
    }

    // --- Teste de Inclusão (Salvar) ---
    @Test
    @DisplayName("Deve salvar (incluir) um novo livro associado a categoria/estante existentes")
    void save_LivrValido_RetornarId() {
        // arrange cria um novo livro associado a categorias e estantes
        // Usa categorias e estantes criadas no setUp
        Livro novoLivro = new Livro(null, "Neuromancer", "William Gibson", "9988776655", 1984, 1, "/images/neuromancer.png", catFiccao, estA1);

        // Act
        Livro livroSalvo = livroRepository.save(novoLivro);

        // Assert
        assertThat(livroSalvo).isNotNull().satisfies(livro -> {
            assertThat(livro.getId()).isNotNull();
            assertThat(livro.getTitulo()).isEqualTo("Neuromancer");
            assertThat(livro.getAutor()).isEqualTo("William Gibson");
            assertThat(livro.getCapaUrl()).isEqualTo("/images/neuromancer.png");
            assertThat(livro.getCategoria().getId()).isEqualTo(catFiccao.getId());
            assertThat(livro.getEstante().getId()).isEqualTo(estA1.getId());
        });

        // Verifica no banco
        Livro livroDoBanco = entityManager.find(Livro.class, livroSalvo.getId());
        assertThat(livroDoBanco).isNotNull();
        assertThat(livroDoBanco.getTitulo()).isEqualTo("Neuromancer");
        assertThat(livroDoBanco.getCategoria().getNome()).isEqualTo("Ficção Científica"); // Checa nome da categoria
                                                                                          // associada
        assertThat(livroDoBanco.getEstante().getNome()).isEqualTo("A1"); // Checa nome/código da estante associada
    }

    // --- Testes de Consulta/Pesquisa ---

    @Test
    @DisplayName("Deve encontrar todos os livros (findAll)")
    void findAll_DeveRetornarTodosLivrosPersistidos() {
        // Arrange (Dados do setUp)

        // Act
        List<Livro> livros = livroRepository.findAll();

        // Assert
        assertThat(livros).isNotNull().hasSize(2); // Esperamos 2 livros do setUp
        assertThat(livros).extracting(Livro::getTitulo).containsExactlyInAnyOrder("O Hobbit", "Duna");
    }

    @Test
    @DisplayName("Deve encontrar livro por ID existente (findById)")
    void findById_IdExiste_RetornarLivroCompleto() {
        // Arrange
        Long idExistente = livroDuna.getId(); // Pega ID do livro persistido no setUp
        assertThat(idExistente).isNotNull();

        // Act
        Optional<Livro> optionalLivro = livroRepository.findById(idExistente);

        // Assert
        assertThat(optionalLivro).isPresent();
        assertThat(optionalLivro.get()).satisfies(livroEncontrado -> {
            assertThat(livroEncontrado.getId()).isEqualTo(idExistente);
            assertThat(livroEncontrado.getTitulo()).isEqualTo("Duna");
            assertThat(livroEncontrado.getAutor()).isEqualTo("Frank Herbert");
            assertThat(livroEncontrado.getCategoria().getNome()).isEqualTo("Ficção Científica");
            assertThat(livroEncontrado.getEstante().getNome()).isEqualTo("B2");
        });
    }

    @Test
    @DisplayName("Não deve encontrar livro por ID inexistente (findById)")
    void findById_IdNaoExiste_RetornarVazio() {
        // Arrange
        Long idInexistente = 999L;

        // Act
        Optional<Livro> optionalLivro = livroRepository.findById(idInexistente);

        // Assert
        assertThat(optionalLivro).isNotPresent();
    }

    // Exemplo:
    @Test
    @DisplayName("Deve encontrar livros por título contendo 'Hobbit'")
    void findByTituloContainingIgnoreCase_ComKeywordExistente_RetornarLivros() {
        // Arrange
        String keyword = "Hobbit";

        // Act
        List<Livro> livros = livroRepository.findByTituloContainingIgnoreCase(keyword);

        // Assert
        assertThat(livros).isNotNull().hasSize(1);
        assertThat(livros.get(0).getTitulo()).isEqualTo("O Hobbit");
    }

    // Exemplo: Supondo que você tenha `List<Livro> findByAutor(String autor);` no repositório
    @Test
    @DisplayName("Deve encontrar livros pelo autor 'Frank Herbert'")
    void findByAutor_QuandoAutorExiste_DeveRetornarLivros() {
        // Arrange
        String autor = "Frank Herbert";

        // Act - Lembre-se de renomear o método no LivroRepository para findByAutor
        List<Livro> livros = livroRepository.findByAutor(autor); // O método agora é findByAutor

        // Assert
        assertThat(livros).isNotNull().hasSize(1);
        assertThat(livros.get(0).getAutor()).isEqualTo(autor);
        assertThat(livros.get(0).getTitulo()).isEqualTo("Duna");
    }

    // Exemplo: Supondo que você tenha `List<Livro> findByCategoriaNome(String
    // nome);`
    @Test
    @DisplayName("Deve encontrar livros pela categoria 'Fantasia' (findByCategoriaNome)")
    void findByCategoriaNome_QuandoCategoriaExiste_DeveRetornarLivros() {
        // Arrange
        String nomeCategoria = "Fantasia";

        // Act
        List<Livro> livros = livroRepository.findByCategoriaNome(nomeCategoria); // Use o nome exato do seu método

        // Assert
        assertThat(livros).isNotNull().hasSize(1);
        assertThat(livros.get(0).getTitulo()).isEqualTo("O Hobbit");
        assertThat(livros.get(0).getCategoria().getNome()).isEqualTo(nomeCategoria);
    }

    // --- Teste de Exclusão ---
    @Test
    @DisplayName("Deve excluir livro por ID existente (deleteById)")
    void deleteById_QuandoIdExiste_DeveRemoverLivro() {
        // Arrange
        Long idParaDeletar = livroHobbit.getId();
        assertThat(idParaDeletar).isNotNull();
        assertThat(livroRepository.existsById(idParaDeletar)).isTrue();
        long contagemAntes = livroRepository.count();

        // Act
        livroRepository.deleteById(idParaDeletar);
        entityManager.flush(); // Garante execução do delete

        // Assert
        assertThat(livroRepository.existsById(idParaDeletar)).isFalse();
        assertThat(livroRepository.count()).isEqualTo(contagemAntes - 1);
    }

    // --- Teste de Atualização ---
    @Test
    @DisplayName("Deve atualizar o ano de publicação de um livro existente")
    void save_QuandoAtualizaLivroExistente_DevePersistirAlteracao() {
        // Arrange
        Long idParaAtualizar = livroDuna.getId();
        assertThat(idParaAtualizar).isNotNull();

        Optional<Livro> optionalLivro = livroRepository.findById(idParaAtualizar);
        assertThat(optionalLivro).isPresent();
        Livro livroParaAtualizar = optionalLivro.get();
        Integer anoAntigo = livroParaAtualizar.getAnoPublicacao();
        Integer anoNovo = 1966; // Novo ano

        livroParaAtualizar.setAnoPublicacao(anoNovo); // Modifica o ano

        // Act
        Livro livroAtualizado = livroRepository.save(livroParaAtualizar);
        entityManager.flush(); // Garante update no banco
        entityManager.clear(); // Limpa cache para ler do banco

        // Assert
        assertThat(livroAtualizado).isNotNull();
        assertThat(livroAtualizado.getId()).isEqualTo(idParaAtualizar);
        assertThat(livroAtualizado.getAnoPublicacao()).isEqualTo(anoNovo);
        assertThat(livroAtualizado.getAnoPublicacao()).isNotEqualTo(anoAntigo);

        // Verifica direto no banco
        Optional<Livro> optionalLivroDoBanco = livroRepository.findById(idParaAtualizar);
        assertThat(optionalLivroDoBanco).isPresent();
        assertThat(optionalLivroDoBanco.get().getAnoPublicacao()).isEqualTo(anoNovo);
    }
}