package org.iftm.biblioteca.repository; // Pacote onde o teste está localizado

// Importa as classes necessárias para o teste

import java.util.List; // A classe Categoria que estamos testando
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.iftm.biblioteca.entities.Categoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest // Anotação que indica que é um teste de repositório JPA
class CategoriaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria cat1;
    private Categoria cat2;

    @SuppressWarnings("unused") // Suprime o aviso da IDE, pois o JUnit usa este método
    @BeforeEach
    // metodo setUp() é chamado antes de cada teste
    // Isso garante que os dados estejam sempre no estado inicial antes de cada
    // teste
    void setUp() {
        // Cria duas categorias para usar nos testes
        cat1 = new Categoria(null, "Fantasia");
        cat2 = new Categoria(null, "Ficção Científica");

        entityManager.persist(cat1);
        entityManager.persist(cat2);
        entityManager.flush();
    }

    // --- Teste de Inclusão (Salvar) ---
    @Test
    @DisplayName("Deve salvar (incluir) uma nova categoria!")
    void save_QuandoCategoriaValida_DeveRetornarCategoriaComId() {
        // Arrange
        Categoria novaCategoria = new Categoria(null, "Aventura");

        // Act
        Categoria categoriaSalva = categoriaRepository.save(novaCategoria);

        // Assert
        assertThat(categoriaSalva).isNotNull();
        assertThat(categoriaSalva.getId()).isNotNull();
        assertThat(categoriaSalva.getNome()).isEqualTo("Aventura");

        // Verifica no banco
        Categoria categoriaDoBanco = entityManager.find(Categoria.class, categoriaSalva.getId());
        assertThat(categoriaDoBanco).isNotNull();
        assertThat(categoriaDoBanco.getNome()).isEqualTo("Aventura");
    }

    // --- Testes de Consulta/Pesquisa ---
    @Test
    @DisplayName("Deve encontrar todas as categorias (findAll)")
    void findAll_DeveRetornarTodasCategoriasPersistidas() {
        // Arrange (Dados do setUp)

        // Act
        List<Categoria> categorias = categoriaRepository.findAll();

        // Assert
        assertThat(categorias).isNotNull().hasSize(2); // Espera 2 do setUp
        assertThat(categorias).extracting(Categoria::getNome)
                .containsExactlyInAnyOrder("Fantasia", "Ficção Científica");
    }

    @Test
    @DisplayName("Deve encontrar categoria por ID existente (findById)")
    void findById_QuandoIdExiste_DeveRetornarOptionalComCategoria() {
        // Arrange
        Long idExistente = cat1.getId();
        assertThat(idExistente).isNotNull();

        // Act
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(idExistente);

        // Assert
        assertThat(optionalCategoria).isPresent();
        assertThat(optionalCategoria.get().getId()).isEqualTo(idExistente);
        assertThat(optionalCategoria.get().getNome()).isEqualTo("Fantasia");
    }

    @Test
    @DisplayName("Não deve encontrar categoria por ID inexistente (findById)")
    void findById_QuandoIdNaoExiste_DeveRetornarOptionalVazio() {
        // Arrange
        Long idInexistente = 999L;

        // Act
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(idInexistente);

        // Assert
        assertThat(optionalCategoria).isNotPresent();
    }

    // --- Teste de Query Method ---
    @Test
    @DisplayName("Deve encontrar categoria pelo nome existente (Query Method findByNome)")
    void findByNome_QuandoNomeExiste_DeveRetornarOptionalComCategoria() {
        // Arrange
        String nomeExistente = "Ficção Científica";

        // Act
        Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(nomeExistente);

        // Assert
        assertThat(optionalCategoria).isPresent();
        assertThat(optionalCategoria.get().getNome()).isEqualTo(nomeExistente);
        assertThat(optionalCategoria.get().getId()).isEqualTo(cat2.getId());
    }

    @Test
    @DisplayName("Não deve encontrar categoria pelo nome inexistente (Query Method findByNome)")
    void findByNome_QuandoNomeNaoExiste_DeveRetornarOptionalVazio() {
        // Arrange
        String nomeInexistente = "Suspense";

        // Act
        Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(nomeInexistente);

        // Assert
        assertThat(optionalCategoria).isNotPresent();
    }

    // --- Teste de Exclusão ---
    @Test
    @DisplayName("Deve excluir categoria por ID existente (deleteById)")
    void deleteById_QuandoIdExiste_DeveRemoverCategoriaDoBanco() {
        // Arrange
        Long idParaDeletar = cat1.getId();
        assertThat(idParaDeletar).isNotNull();
        assertThat(categoriaRepository.existsById(idParaDeletar)).isTrue();
        long contagemAntes = categoriaRepository.count();

        // Act
        categoriaRepository.deleteById(idParaDeletar);
        entityManager.flush();

        // Assert
        assertThat(categoriaRepository.existsById(idParaDeletar)).isFalse();
        assertThat(categoriaRepository.count()).isEqualTo(contagemAntes - 1);
    }

    // --- Teste de Atualização (é feito usando save) ---
    @Test
    @DisplayName("Deve atualizar o nome de uma categoria existente")
    void save_QuandoAtualizaCategoriaExistente_DevePersistirAlteracao() {
        // Arrange
        Long idParaAtualizar = cat2.getId();
        assertThat(idParaAtualizar).isNotNull();

        // Busca a categoria existente
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(idParaAtualizar);
        assertThat(optionalCategoria).isPresent();
        Categoria categoriaParaAtualizar = optionalCategoria.get();
        String nomeAntigo = categoriaParaAtualizar.getNome();
        String nomeNovo = "Sci-Fi";

        categoriaParaAtualizar.setNome(nomeNovo);

        // Act
        Categoria categoriaAtualizada = categoriaRepository.save(categoriaParaAtualizar);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(categoriaAtualizada).isNotNull();
        assertThat(categoriaAtualizada.getId()).isEqualTo(idParaAtualizar);
        assertThat(categoriaAtualizada.getNome()).isEqualTo(nomeNovo);
        assertThat(categoriaAtualizada.getNome()).isNotEqualTo(nomeAntigo);

        // Verifica no banco
        Optional<Categoria> optionalCategoriaDoBanco = categoriaRepository.findById(idParaAtualizar);
        assertThat(optionalCategoriaDoBanco).isPresent();
        assertThat(optionalCategoriaDoBanco.get().getNome()).isEqualTo(nomeNovo);
    }
}