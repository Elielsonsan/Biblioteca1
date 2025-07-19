package org.iftm.biblioteca.config;

// --- Imports Necessários ---
import java.math.BigDecimal; 
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Usuarios;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.UsuariosRepository;
import org.iftm.biblioteca.repository.EmprestimoRepository;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Componente para carregar dados iniciais no banco de dados na inicialização da aplicação.
 * <p>
 * Implementa {@link CommandLineRunner}, o que faz com que o método {@code run()} seja
 * executado automaticamente pelo Spring Boot após a aplicação iniciar.
 * <p>
 * Esta classe é útil para ambientes de desenvolvimento e teste, garantindo que o banco
 * de dados sempre comece com um conjunto de dados consistente.
 * <p>
 * <strong>Atenção:</strong> A anotação {@code @Component} está comentada. Para ativar
 * este carregador de dados, descomente a linha `//@Component`. Atualmente, a
 * carga de dados pode estar sendo feita pelo arquivo `data.sql`.
 */
//@Component // Descomente esta linha para ativar o carregamento de dados via Java.
public class DataLoader implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

        // --- Injeção de Dependências dos Repositórios ---
        // O Spring injeta automaticamente as instâncias dos repositórios necessários.
        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private EstanteRepository estanteRepository;
        @Autowired
        private LivroRepository livroRepository;
        @Autowired
        private UsuariosRepository usuarioRepository;
        @Autowired
        private EmprestimoRepository emprestimoRepository;

        // O EntityManager é injetado para operações de baixo nível, como o flush.
        @PersistenceContext
        private EntityManager entityManager;

        /**
         * Método executado na inicialização da aplicação.
         * A anotação {@code @Transactional} garante que todas as operações de banco de dados
         * dentro deste método ocorram em uma única transação. Se um erro ocorrer,
         * todas as alterações são desfeitas (rollback), mantendo a consistência.
         */
        @Override
        @Transactional
        public void run(String... args) throws Exception {

                // --- Limpeza do Banco de Dados ---
                // Garante que o banco de dados esteja em um estado limpo antes de inserir novos dados.
                // A ordem é importante para respeitar as chaves estrangeiras:
                // 1. Empréstimos (depende de Livros e Usuários)
                // 2. Livros (depende de Categorias e Estantes)
                // 3. Usuários
                // 4. Categorias e Estantes (não dependem de outras entidades)
                emprestimoRepository.deleteAll();
                livroRepository.deleteAll();
                usuarioRepository.deleteAll();
                categoriaRepository.deleteAll();
                estanteRepository.deleteAll();
                entityManager.flush(); // Força a execução dos deletes no banco de dados

                logger.info(">>> Carregando dados iniciais via DataLoader...");

                // --- 1. Criação e Persistência de Categorias ---
                // É crucial salvar as entidades que não têm dependências primeiro.
                Categoria catFiccao = new Categoria(null, "Ficção");
                Categoria catNaoFiccao = new Categoria(null, "Não Ficção");
                Categoria catFantasia = new Categoria(null, "Fantasia");
                Categoria catRomance = new Categoria(null, "Romance");
                Categoria catAventura = new Categoria(null, "Aventura");

                // Salva todas as categorias em uma única operação de lote para melhor performance.
                List<Categoria> categoriasSalvas = categoriaRepository.saveAll(Arrays.asList(
                                catFiccao, catNaoFiccao, catFantasia, catRomance, catAventura));

                // Cria um Map para facilitar a busca das categorias salvas pelo nome.
                // Isso evita múltiplas consultas ao banco de dados para obter as referências.
                Map<String, Categoria> mapCategoriasSalvas = categoriasSalvas.stream()
                                .collect(Collectors.toMap(Categoria::getNome, Function.identity()));

                Categoria fantasiaSalva = mapCategoriasSalvas.get("Fantasia");
                Categoria romanceSalvo = mapCategoriasSalvas.get("Romance");
                Categoria ficcaoSalva = mapCategoriasSalvas.get("Ficção");

                // 2. Criar e Salvar Estantes PRIMEIRO
                Estante est1 = new Estante(null, "Estante 1");
                Estante est2 = new Estante(null, "Estante 2");
                Estante est3 = new Estante(null, "Estante 3");
                Estante est4 = new Estante(null, "Seção Especial - TI"); // Adiciona a estante que estava no data.sql
                List<Estante> estantesSalvas = estanteRepository.saveAll(Arrays.asList(est1, est2, est3, est4));

                // Coleta as estantes salvas em um Map para fácil acesso pelo nome
                Map<String, Estante> mapEstantesSalvas = estantesSalvas.stream()
                                .collect(Collectors.toMap(Estante::getNome, Function.identity()));
                Estante estante1Salva = mapEstantesSalvas.get("Estante 1");
                Estante estante2Salva = mapEstantesSalvas.get("Estante 2");
                Estante estante3Salva = mapEstantesSalvas.get("Estante 3");
                Estante estante4Salva = mapEstantesSalvas.get("Seção Especial - TI");

                // 3. Criar e Salvar Livros, usando as referências salvas de Categoria e Estante
                // Certifique-se que o construtor da classe Livro aceita os objetos Categoria e
                // Estante.
                // Ajuste os parâmetros do construtor conforme a sua classe Livro.
                // Exemplo de como criar livros com as referências salvas
                // Verifica se as categorias e estantes foram salvas corretamente e não são
                // nulas
                if (fantasiaSalva != null && romanceSalvo != null && ficcaoSalva != null &&
                                estante1Salva != null && estante2Salva != null && estante3Salva != null
                                && estante4Salva != null) { // estante4Salva não estava sendo usada, mas a verificação é boa

                        Livro l1 = new Livro(null, "O Senhor dos Anéis", "J.R.R. Tolkien", "1234567890", 1954, 5,
                                        "/images/capa-placeholder.png", fantasiaSalva, estante1Salva);
                        Livro l2 = new Livro(null, "Harry Potter e a Pedra Filosofal", "J.K. Rowling", "0987654321",
                                        1997, 3, "/images/capa-placeholder.png", fantasiaSalva, estante2Salva);
                        Livro l3 = new Livro(null, "O Hobbit", "J.R.R. Tolkien", "1122334455", 1937, 4,
                                        "/images/capa-placeholder.png", fantasiaSalva, estante3Salva);
                        Livro l4 = new Livro(null, "Dom Casmurro", "Machado de Assis", "2233445566", 1899, 2,
                                        "/images/capa-placeholder.png", romanceSalvo, estante1Salva);
                        Livro l5 = new Livro(null, "A Moreninha", "Joaquim Manuel de Macedo", "3344556677", 1844, 1,
                                        "/images/capa-placeholder.png", romanceSalvo, estante2Salva);
                        Livro l6 = new Livro(null, "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "4455667788", 1943,
                                        6, "/images/capa-placeholder.png", ficcaoSalva, estante3Salva); // Use ficcaoSalva
                        Livro l7 = new Livro(null, "A Revolução dos Bichos", "George Orwell", "5566778899", 1945, 2,
                                        "/images/capa-placeholder.png", ficcaoSalva, estante1Salva); // Use ficcaoSalva
                        Livro l8 = new Livro(null, "1984", "George Orwell", "6677889900", 1949, 3,
                                        "/images/capa-placeholder.png", ficcaoSalva, estante2Salva);

                        livroRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8));
                        logger.info(">>> Livros carregados com sucesso!");
                } else {
                        logger.error(">>> Erro: Não foi possível encontrar todas as categorias ou estantes necessárias para criar os livros.");
                }

                // 4. Criar e Salvar Usuários
                logger.info(">>> Carregando usuários...");
                Usuarios usuario1 = new Usuarios(null, "Carlos Alberto", "carlos.alberto@example.com",
                                "123.456.789-00",
                                new BigDecimal("3500.00"), LocalDate.of(1985, 5, 20), 2,
                                "Rua das Flores, 100", "Uberlândia", "MG", "38400-123");

                Usuarios usuario2 = new Usuarios(null, "Fernanda Lima", "fernanda.lima@example.com",
                                "987.654.321-00",
                                new BigDecimal("4200.50"), LocalDate.of(1992, 10, 15), 0,
                                "Avenida Brasil, 500", "Araguari", "MG", "38440-000");

                Usuarios usuario3 = new Usuarios(null, "Bruno Marques", "bruno.marques@example.com",
                                "111.222.333-44",
                                new BigDecimal("2800.00"), LocalDate.of(1990, 1, 1), 1,
                                "Rua dos Exemplos, 123", "Ituiutaba", "MG", "38300-000");

                Usuarios usuario4 = new Usuarios(null, "Juliana Paes", "juliana.paes@example.com",
                                "222.333.444-55",
                                new BigDecimal("6000.00"), LocalDate.of(1988, 3, 25), 0,
                                "Rua das Palmeiras, 45", "Uberaba", "MG", "38010-010");

                Usuarios usuario5 = new Usuarios(null, "Ricardo Souza", "ricardo.souza@example.com",
                                "333.444.555-66",
                                new BigDecimal("1950.75"), LocalDate.of(2001, 8, 10), 0,
                                "Avenida Afonso Pena, 2000", "Belo Horizonte", "MG", "30130-005");

                usuarioRepository.saveAll(Arrays.asList(usuario1, usuario2, usuario3, usuario4, usuario5));
                logger.info(">>> Usuários carregados com sucesso!");
        }
}