package org.iftm.biblioteca.repository; // Pacote de teste

import java.util.List; // Importa a entidade Estante
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.iftm.biblioteca.entities.Estante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest // Configura ambiente para teste JPA
class EstanteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager; // Para preparar dados no teste

    @Autowired
    private EstanteRepository estanteRepository; // O repositório que vamos testar

    private Estante estante1;
    private Estante estante2;

    @BeforeEach // Roda antes de cada @Test
    protected void setUp() {
        // Cria e persiste algumas estantes com IDs de String para os testes
        estante1 = new Estante("E001", "Corredor A - Prateleira 1");
        estante2 = new Estante("E002", "Corredor B - Prateleira 2");

        entityManager.persist(estante1);
        entityManager.persist(estante2);
        entityManager.flush(); // Garante que foram para o banco (na transação do teste)
    }

    // --- Teste de Inclusão (Salvar) ---
    @Test
    @DisplayName("Deve salvar (incluir) uma nova estante com sucesso")
    void save_QuandoEstanteValida_DeveRetornarEstanteComId() {
        // Arrange
        Estante novaEstante = new Estante("E003", "Corredor C - Prateleira 3");

        // Act
        Estante estanteSalva = estanteRepository.save(novaEstante);

        // Assert
        assertThat(estanteSalva).isNotNull();
        assertThat(estanteSalva.getId()).isEqualTo("E003");
        assertThat(estanteSalva.getNome()).isEqualTo("Corredor C - Prateleira 3");

        // Verifica no banco
        Estante estanteDoBanco = entityManager.find(Estante.class, "E003");
        assertThat(estanteDoBanco).isNotNull();
        assertThat(estanteDoBanco.getNome()).isEqualTo("Corredor C - Prateleira 3");
    }

    // --- Testes de Consulta/Pesquisa ---

    @Test
    @DisplayName("Deve encontrar todas as estantes (findAll)")
    void findAll_DeveRetornarTodasEstantesPersistidas() {
        // Arrange (Dados do setUp)

        // Act
        List<Estante> estantes = estanteRepository.findAll();

        // Assert
        assertThat(estantes).isNotNull().hasSize(2); // Espera 2 do setUp
        assertThat(estantes).extracting(Estante::getNome)
                .containsExactlyInAnyOrder("Corredor A - Prateleira 1", "Corredor B - Prateleira 2");
    }

    @Test
    @DisplayName("Deve encontrar estante por ID existente (findById)")
    void findById_QuandoIdExiste_DeveRetornarOptionalComEstante() {
        // Arrange
        String idExistente = estante1.getId(); // Pega ID do setUp (agora é String)
        assertThat(idExistente).isNotNull();

        // Act
        Optional<Estante> optionalEstante = estanteRepository.findById(idExistente); // findById agora espera String

        // Assert
        assertThat(optionalEstante).isPresent();
        assertThat(optionalEstante.get().getId()).isEqualTo(idExistente);
        assertThat(optionalEstante.get().getNome()).isEqualTo("Corredor A - Prateleira 1");
    }

    @Test
    @DisplayName("Não deve encontrar estante por ID inexistente (findById)")
    void findById_QuandoIdNaoExiste_DeveRetornarOptionalVazio() {
        // Arrange
        String idInexistente = "E999";

        // Act
        Optional<Estante> optionalEstante = estanteRepository.findById(idInexistente); // findById agora espera String

        // Assert
        assertThat(optionalEstante).isNotPresent();
    }

    // --- Testes de Query Methods ---
    // !! IMPORTANTE !! Adapte ou adicione testes para os Query Methods
    // que VOCÊ definiu na sua interface EstanteRepository.
    // Abaixo estão exemplos:

    @Test
    @DisplayName("Deve encontrar estante pelo nome existente (Query Method findByNomeIgnoreCase)")
    void findByNomeIgnoreCase_QuandoNomeExiste_DeveRetornarOptionalComEstante() {
        // Arrange
        String nomeExistente = "Corredor B - Prateleira 2"; // Do setUp
        // Act
        Optional<Estante> optionalEstante = estanteRepository.findByNomeIgnoreCase(nomeExistente);

        // Assert
        assertThat(optionalEstante).isPresent();
        assertThat(optionalEstante.get().getNome()).isEqualTo(nomeExistente);
        assertThat(optionalEstante.get().getId()).isEqualTo(estante2.getId());
    }

    @Test
    @DisplayName("Não deve encontrar estante pelo nome inexistente (Query Method findByNomeIgnoreCase)")
    void findByNomeIgnoreCase_QuandoNomeNaoExiste_DeveRetornarOptionalVazio() {
        // Arrange
        String nomeInexistente = "Corredor Z";

        // Act
        Optional<Estante> optionalEstante = estanteRepository.findByNomeIgnoreCase(nomeInexistente);

        // Assert
        assertThat(optionalEstante).isNotPresent();
    }

    // --- Teste de Exclusão ---
    @Test
    @DisplayName("Deve excluir estante por ID existente (deleteById)")
    void deleteById_QuandoIdExiste_DeveRemoverEstante() {
        // Arrange
        String idParaDeletar = estante2.getId(); // ID agora é String
        assertThat(idParaDeletar).isNotNull();
        assertThat(estanteRepository.existsById(idParaDeletar)).isTrue();
        long contagemAntes = estanteRepository.count();

        // Act
        estanteRepository.deleteById(idParaDeletar);
        entityManager.flush(); // Garante execução

        // Assert
        assertThat(estanteRepository.existsById(idParaDeletar)).isFalse();
        assertThat(estanteRepository.count()).isEqualTo(contagemAntes - 1);
    }

    // --- Teste de Atualização ---
    @Test
    @DisplayName("Deve atualizar o nome de uma estante existente")
    void save_QuandoAtualizaEstanteExistente_DevePersistirAlteracao() {
        // Arrange
        String idParaAtualizar = estante1.getId(); // ID agora é String
        assertThat(idParaAtualizar).isNotNull();

        Optional<Estante> optionalEstante = estanteRepository.findById(idParaAtualizar); // findById agora espera String
        assertThat(optionalEstante).isPresent();
        Estante estanteParaAtualizar = optionalEstante.get();
        String nomeNovo = "Corredor A - Prateleira 1 (Reformada)";

        estanteParaAtualizar.setNome(nomeNovo); // Altera o nome

        // Act
        Estante estanteAtualizada = estanteRepository.save(estanteParaAtualizar);
        entityManager.flush(); // Garante atualização
        entityManager.clear(); // Limpa cache

        // Assert
        assertThat(estanteAtualizada).isNotNull();
        assertThat(estanteAtualizada.getId()).isEqualTo(idParaAtualizar);
        assertThat(estanteAtualizada.getNome()).isEqualTo(nomeNovo);

        // Verifica no banco
        Optional<Estante> optionalEstanteDoBanco = estanteRepository.findById(idParaAtualizar);
        assertThat(optionalEstanteDoBanco).isPresent();
        assertThat(optionalEstanteDoBanco.get().getNome()).isEqualTo(nomeNovo);
    }
}