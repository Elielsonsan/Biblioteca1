package org.iftm.biblioteca.config;

// --- Imports Necessários ---

import java.math.BigDecimal;
import java.time.LocalDate; // Adicionar para BigDecimal
import java.util.Arrays; // Adicionar para LocalDate
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Client;
import org.iftm.biblioteca.entities.Estante; // Adicionar import para Client
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.ClientRepository; // Opcional, mas bom para operações múltiplas
import org.iftm.biblioteca.repository.EstanteRepository; // Adicionar import para ClientRepository
import org.iftm.biblioteca.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DataLoader implements CommandLineRunner {

        // --- Declara os repositórios como membros da classe ---
        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private EstanteRepository estanteRepository;
        @Autowired
        private LivroRepository livroRepository;
        @Autowired // Injetar o ClientRepository
        private ClientRepository clientRepository;

        @PersistenceContext
        private EntityManager entityManager;

        // --- Implementação do método run ---
        @Override
        @Transactional // Garante que tudo execute em uma única transação (opcional, mas recomendado)
        public void run(String... args) throws Exception {

                // Limpa o banco antes de inserir (útil para H2 em memória, opcional)
                livroRepository.deleteAll();
                clientRepository.deleteAll(); // Deletar Clientes antes de Categorias
                categoriaRepository.deleteAll();
                estanteRepository.deleteAll();
                entityManager.flush(); // Força a execução dos deletes no banco de dados

                System.out.println(">>> Carregando dados iniciais...");

                // 1. Criar e Salvar Categorias PRIMEIRO
                // Use os objetos retornados pelo saveAll, pois podem conter IDs gerados.
                Categoria catFiccao = new Categoria(null, "Ficção");
                Categoria catNaoFiccao = new Categoria(null, "Não Ficção");
                Categoria catFantasia = new Categoria(null, "Fantasia");
                Categoria catRomance = new Categoria(null, "Romance");
                Categoria catAventura = new Categoria(null, "Aventura");

                // Salva todos de uma vez e guarda a lista de entidades salvas
                List<Categoria> categoriasSalvas = categoriaRepository.saveAll(Arrays.asList(
                                catFiccao, catNaoFiccao, catFantasia, catRomance, catAventura));

                // Coleta as categorias salvas em um Map para fácil acesso pelo nome
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
                                && estante4Salva != null) {

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
                        // Exemplo de livro usando a nova estante e uma categoria existente
                        // Supondo que categoria "Romance" (ID 4 pelo DataLoader) e "Seção Especial -
                        // TI" (ID 4 pelo DataLoader)
                        // Livro l9 = new Livro(null, "Código Limpo", "Robert C. Martin",
                        // "9788576082675", 2008, 1, mapCategoriasSalvas.get("Romance"), estante4Salva);

                        livroRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8));
                        System.out.println(">>> Livros carregados com sucesso!");
                } else {
                        System.out.println(">>> Erro ao carregar categorias ou estantes salvas.");
                }

                // 4. Criar e Salvar Clientes
                // Certifique-se de que as categorias referenciadas (ficcaoSalva, romanceSalvo)
                // existem
                if (ficcaoSalva != null && romanceSalvo != null) {
                        Client cliente1 = new Client(null, "Carlos Alberto", "carlos.alberto@example.com",
                                        "123.456.789-00",
                                        new BigDecimal("3500.00"), LocalDate.of(1985, 5, 20), 2,
                                        "Rua Teste, 100", "Uberlândia", "MG", "38400-000", ficcaoSalva);

                        Client cliente2 = new Client(null, "Fernanda Lima", "fernanda.lima@example.com",
                                        "987.654.321-00",
                                        new BigDecimal("4200.50"), LocalDate.of(1992, 10, 15), 0,
                                        "Avenida Brasil, 500", "Araguari", "MG", "38440-000", romanceSalvo);

                        Client cliente3 = new Client(null, "Usuario Teste Sem Categoria", "teste.semcat@example.com",
                                        "111.222.333-44",
                                        new BigDecimal("2000.00"), LocalDate.of(1990, 1, 1), 1,
                                        "Rua dos Exemplos, 123", "Ituiutaba", "MG", "38300-000", null); // Cliente sem
                                                                                                        // categoria
                                                                                                        // associada

                        clientRepository.saveAll(Arrays.asList(cliente1, cliente2, cliente3));
                        System.out.println(">>> Clientes carregados com sucesso!");
                } else {
                        System.out.println(">>> Erro: Categorias necessárias para clientes não foram carregadas.");
                }

        }
}