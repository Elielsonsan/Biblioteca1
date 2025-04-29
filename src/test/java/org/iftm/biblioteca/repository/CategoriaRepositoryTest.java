package org.iftm.biblioteca.repository; // Pacote onde o teste está localizado
    // Importa as classes necessárias para o teste

 import org.iftm.biblioteca.entities.Categoria; // A classe Categoria que estamos testando
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.DisplayName;
 import org.junit.jupiter.api.Test;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
 import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

 import java.util.List;
 import java.util.Optional;

 // Importa as classes do AssertJ para fazer asserções mais legíveis
 import static org.assertj.core.api.Assertions.assertThat;

 @DataJpaTest // Anotação que indica que é um teste de repositório JPA
 // Carrega o contexto do Spring apenas para os componentes necessários para o teste
 class CategoriaRepositoryTest {

     @Autowired
     private TestEntityManager entityManager; //Gerencia do JPA para facilitar os testes com o banco de dados

     @Autowired
     private CategoriaRepository categoriaRepository; // repositório que estamos testando
     // Instâncias de Categoria que serão usadas nos testes

     private Categoria cat1;
     private Categoria cat2;

    //-- Método que roda antes de cada teste ---
     // Aqui você pode criar dados comuns a todos os testes
     @BeforeEach
     // metodo setUp() é chamado antes de cada teste
     // Isso garante que os dados estejam sempre no estado inicial antes de cada teste
     void setUp() {
         // Cria duas categorias para usar nos testes
         // Essas categorias serão persistidas no banco de dados antes de cada teste
         // Isso garante que os testes de consulta funcionem corretamente
         // e que os dados estejam sempre no estado inicial
         cat1 = new Categoria(null, "Fantasia");
         cat2 = new Categoria(null, "Ficção Científica");

         // Persiste as categorias usando TestEntityManager para garantir que elas existam
         // no banco de dados ANTES de rodar os métodos do repositório que as consultam.
         entityManager.persist(cat1);
         entityManager.persist(cat2);
         entityManager.flush(); //limpa o cache do entityManager
         // Garante que as operações de persistência sejam executadas no banco de dados
         // antes de continuar com os testes.
     }

     // --- Teste de Inclusão (Salvar) ---
     @Test
     @DisplayName("Deve salvar (incluir) uma nova categoria!")
     void save_QuandoCategoriaValida_DeveRetornarCategoriaComId() {
         // Arrange (Preparar) - Cria uma nova categoria para salvar
         // Essa categoria não existe no banco de dados ainda
         // e não tem ID definido (null).
         // O ID será gerado automaticamente pelo banco de dados
         // quando a categoria for persistida.
         Categoria novaCategoria = new Categoria(null, "Aventura");

         // Act (Executar) - Salva a nova categoria no banco de dados
         // O método save() do repositório irá persistir a categoria
         // e gerar um ID para ela.
         // O retorno do método save() será a categoria persistida
         Categoria categoriaSalva = categoriaRepository.save(novaCategoria);

         // Assert (Verificar) - Verifica se a categoria foi salva corretamente
         // e se o ID foi gerado.
         // O ID deve ser diferente de null, indicando que a categoria foi persistida
         // e que o banco de dados gerou um ID para ela.
         // Verifica se a categoriaSalva não é nula
         assertThat(categoriaSalva).isNotNull();
         assertThat(categoriaSalva.getId()).isNotNull(); // Verifica se o ID é diferente de null
         assertThat(categoriaSalva.getNome()).isEqualTo("Aventura");

         // Verifica se a categoria foi realmente persistida no banco de dados
         // Busca a categoria pelo ID gerado
         // e verifica se ela existe no banco de dados.
         // O método find() do entityManager busca a categoria pelo ID
         // e retorna a entidade correspondente.
         Categoria categoriaDoBanco = entityManager.find(Categoria.class, categoriaSalva.getId());
         assertThat(categoriaDoBanco).isNotNull();
         assertThat(categoriaDoBanco.getNome()).isEqualTo("Aventura");
     }

     // --- Testes de Consulta/Pesquisa ---

     @Test
     @DisplayName("Deve encontrar todas as categorias (findAll)")
     void findAll_DeveRetornarTodasCategoriasPersistidas() {
         // Arrange (cat1 e cat2 foram persistidas no setUp)
         // Aqui não precisamos fazer nada, pois as categorias já estão persistidas
         // no banco de dados pelo método setUp().

         // Act
         // Busca todas as categorias do banco de dados
         // O método findAll() do repositório retorna uma lista com todas as categorias
         // que foram persistidas no banco de dados.
         List<Categoria> categorias = categoriaRepository.findAll();

         // Assert
         // Verifica se a lista de categorias não é nula
         // e se contém exatamente 2 categorias (cat1 e cat2).
         // O método hasSize(2) verifica se a lista tem exatamente 2 elementos.
         // O método extract() extrai os nomes das categorias
         // e verifica se contém exatamente os nomes "Fantasia" e "Ficção Científica".
         // O método containsExactlyInAnyOrder() verifica se a lista contém exatamente
         // os elementos especificados, independentemente da ordem.
         // O método isNotNull() verifica se a lista não é nula.
         // O método hasSize(2) verifica se a lista tem exatamente 2 elementos.
         // O método extract() extrai os nomes das categorias
         // e verifica se contém exatamente os nomes "Fantasia" e "Ficção Científica".
         // O método containsExactlyInAnyOrder() verifica se a lista contém exatamente
         // os elementos especificados, independentemente da ordem.
         // O método isNotNull() verifica se a lista não é nula.
         assertThat(categorias).isNotNull();
         assertThat(categorias).hasSize(2); // Esperamos as 2 do setUp
         assertThat(categorias).extracting(Categoria::getNome).containsExactlyInAnyOrder("Fantasia", "Ficção Científica");
     }

     @Test
     @DisplayName("Deve encontrar categoria por ID existente (findById)")
     void findById_QuandoIdExiste_DeveRetornarOptionalComCategoria() {
         // Arrange (cat1 foi persistida no setUp)
         // Aqui não precisamos fazer nada, pois a categoria já está persistida
         // no banco de dados pelo método setUp().
         Long idExistente = cat1.getId();
         assertThat(idExistente).isNotNull(); // Verifica se o ID não é nulo

         // Act
         Optional<Categoria> optionalCategoria = categoriaRepository.findById(idExistente);

         // Assert
         assertThat(optionalCategoria).isPresent(); // Verifica se o Optional contém um valor
         // O método isPresent() verifica se o Optional contém um valor.
         // O método get() retorna o valor contido no Optional.
         assertThat(optionalCategoria.get().getId()).isEqualTo(idExistente);
         assertThat(optionalCategoria.get().getNome()).isEqualTo("Fantasia");
     }

     @Test
     @DisplayName("Não deve encontrar categoria por ID inexistente (findById)")
     void findById_QuandoIdNaoExiste_DeveRetornarOptionalVazio() {
         // Arrange (cat1 e cat2 foram persistidas no setUp)
         Long idInexistente = 999L;

         // Act
         Optional<Categoria> optionalCategoria = categoriaRepository.findById(idInexistente);

         // Assert
         assertThat(optionalCategoria).isNotPresent(); // Verifica se o Optional não contém um valor
         // O método isNotPresent() verifica se o Optional não contém um valor.
         // O método isEmpty() também pode ser usado para verificar se o Optional está vazio.
         // assertThat(optionalCategoria).isEmpty(); // Outra forma de verificar se o Optional está vazio
         // O método isEmpty() verifica se o Optional está vazio.
         // O método isNotPresent() verifica se o Optional não contém um valor.
         // O método isPresent() verifica se o Optional contém um valor.
         // O método get() retorna o valor contido no Optional.
         // O método orElse() retorna o valor contido no Optional ou um valor padrão se estiver vazio.
         // O método orElseThrow() lança uma exceção se o Optional estiver vazio.
     }

     // --- Teste de Query Method ---
     // Esse teste verifica se o método findByNome() funciona corretamente
     // e se retorna a categoria correta quando o nome existe.
     // O método findByNome() é um método de consulta (query method) que busca
     // uma categoria pelo nome.
     @Test
     @DisplayName("Deve encontrar categoria pelo nome existente (Query Method findByNome)")
     void findByNome_QuandoNomeExiste_DeveRetornarOptionalComCategoria() {
         // Arrange (cat2 foi persistida no setUp)
         String nomeExistente = "Ficção Científica";

         // Act
         Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(nomeExistente);

         // Assert
         assertThat(optionalCategoria).isPresent();
         assertThat(optionalCategoria.get().getNome()).isEqualTo(nomeExistente);
         assertThat(optionalCategoria.get().getId()).isEqualTo(cat2.getId()); // Verifica se é a categoria correta
     }

     @Test
     @DisplayName("Não deve encontrar categoria pelo nome inexistente (Query Method findByNome)")
     void findByNome_QuandoNomeNaoExiste_DeveRetornarOptionalVazio() {
         // Arrange 
         String nomeInexistente = "Suspense";

         // Act - Busca a categoria pelo nome inexistente
         Optional<Categoria> optionalCategoria = categoriaRepository.findByNome(nomeInexistente);

         // Assert - Verifica se a categoria foi encontrada
         assertThat(optionalCategoria).isNotPresent();
     }

     // --- Teste de Exclusão ---

     @Test
     @DisplayName("Deve excluir categoria por ID existente (deleteById)")
     void deleteById_QuandoIdExiste_DeveRemoverCategoriaDoBanco() {
         // Arrange (cat1 foi persistida no setUp)
         Long idParaDeletar = cat1.getId();
         assertThat(idParaDeletar).isNotNull();

         // Verifica se a categoria existe antes de deletar
         // O método existsById() verifica se a categoria existe no banco de dados
         // e retorna true se existir e false se não existir.
         // O método count() conta quantas categorias existem no banco de dados
         // e retorna o número total de categorias.
         // O método findById() busca a categoria pelo ID
         // e retorna um Optional com a categoria encontrada.
         // O método isPresent() verifica se o Optional contém um valor.
         // O método get() retorna o valor contido no Optional.
         // O método isEmpty() verifica se o Optional está vazio.
         // O método isNotPresent() verifica se o Optional não contém um valor.
         // O método orElse() retorna o valor contido no Optional ou um valor padrão se estiver vazio.
         // O método orElseThrow() lança uma exceção se o Optional estiver vazio.
         // O método isPresent() verifica se o Optional contém um valor.
         // O método get() retorna o valor contido no Optional.
         // O método orElse() retorna o valor contido no Optional ou um valor padrão se estiver vazio.
         // O método orElseThrow() lança uma exceção se o Optional estiver vazio.
         assertThat(categoriaRepository.existsById(idParaDeletar)).isTrue();
         long contagemAntes = categoriaRepository.count(); // Verifica a contagem antes da exclusão

         // Act
         categoriaRepository.deleteById(idParaDeletar);
         entityManager.flush(); // Força a exclusão no banco de dados

         // Assert
         assertThat(categoriaRepository.existsById(idParaDeletar)).isFalse(); // Verifica se a categoria foi excluída
         // O método existsById() verifica se a categoria existe no banco de dados
         // e retorna true se existir e false se não existir.
         assertThat(categoriaRepository.count()).isEqualTo(contagemAntes - 1); // Verifica a contagem após a exclusão
         // O método count() conta quantas categorias existem no banco de dados
         // e retorna o número total de categorias.
     }

     // --- Teste de Atualização (é feito usando save) ---
      @Test
     @DisplayName("Deve atualizar o nome de uma categoria existente")
     void save_QuandoAtualizaCategoriaExistente_DevePersistirAlteracao() {
         
         Long idParaAtualizar = cat2.getId();
         assertThat(idParaAtualizar).isNotNull();

         // Busca a categoria existente
         Optional<Categoria> optionalCategoria = categoriaRepository.findById(idParaAtualizar);
         assertThat(optionalCategoria).isPresent();
         Categoria categoriaParaAtualizar = optionalCategoria.get();
         String nomeAntigo = categoriaParaAtualizar.getNome();
         String nomeNovo = "Sci-Fi";

         // Modifica o nome da categoria
         categoriaParaAtualizar.setNome(nomeNovo);

         // Act: Salva a entidade modificada
         Categoria categoriaAtualizada = categoriaRepository.save(categoriaParaAtualizar);
         entityManager.flush(); // Força a atualização no banco
         entityManager.clear(); // Limpa o cache do entityManager
         // Isso garante que a entidade atualizada seja recarregada do banco de dados

         // Assert - Verifica se a categoria foi atualizada
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