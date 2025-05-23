package org.iftm.biblioteca.config;

// --- Imports Necessários ---

import org.iftm.biblioteca.entities.Categoria;
import org.iftm.biblioteca.entities.Estante;
import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.CategoriaRepository;
import org.iftm.biblioteca.repository.EstanteRepository;
import org.iftm.biblioteca.repository.LivroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional; // Opcional, mas bom para operações múltiplas

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

        // --- Declara os repositórios como membros da classe ---
        @Autowired
        private CategoriaRepository categoriaRepository;
        @Autowired
        private EstanteRepository estanteRepository;
        @Autowired
        private LivroRepository livroRepository;

        // --- Implementação do método run ---
        @Override
        @Transactional // Garante que tudo execute em uma única transação (opcional, mas recomendado)
        public void run(String... args) throws Exception {

                // Limpa o banco antes de inserir (útil para H2 em memória, opcional)
                livroRepository.deleteAll();
                categoriaRepository.deleteAll();
                estanteRepository.deleteAll();

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

                // Se precisar referenciar por nome depois, pode buscar na lista salva ou usar
                // findByNome
                // Exemplo de como pegar a referência salva (mais seguro que buscar de novo)
                Categoria fantasiaSalva = categoriasSalvas.stream().filter(c -> c.getNome().equals("Fantasia"))
                                .findFirst()
                                .orElse(null);
                Categoria romanceSalvo = categoriasSalvas.stream().filter(c -> c.getNome().equals("Romance"))
                                .findFirst()
                                .orElse(null);
                Categoria ficcaoSalva = categoriasSalvas.stream().filter(c -> c.getNome().equals("Ficção")).findFirst()
                                .orElse(null);

                // 2. Criar e Salvar Estantes PRIMEIRO
                Estante est1 = new Estante(null, "Estante 1");
                Estante est2 = new Estante(null, "Estante 2");
                Estante est3 = new Estante(null, "Estante 3");

                List<Estante> estantesSalvas = estanteRepository.saveAll(Arrays.asList(est1, est2, est3));

                // Pega as referências salvas filtrando pelo NOME
                Estante estante1Salva = estantesSalvas.stream()
                                .filter(e -> e.getNome().equals("Estante 1")) 
                                .findFirst()
                                .orElse(null);
                Estante estante2Salva = estantesSalvas.stream()
                                .filter(e -> e.getNome().equals("Estante 2")) 
                                .findFirst()
                                .orElse(null);
                Estante estante3Salva = estantesSalvas.stream()
                                .filter(e -> e.getNome().equals("Estante 3")) 
                                .findFirst()
                                .orElse(null);

                // 3. Criar e Salvar Livros, usando as referências salvas de Categoria e Estante
                // Certifique-se que o construtor da classe Livro aceita os objetos Categoria e
                // Estante.
                // Ajuste os parâmetros do construtor conforme a sua classe Livro.
                // Exemplo de como criar livros com as referências salvas
                // Verifica se as categorias e estantes foram salvas corretamente e não são
                // nulas
                if (fantasiaSalva != null && romanceSalvo != null && ficcaoSalva != null &&
                                estante1Salva != null && estante2Salva != null && estante3Salva != null) { // Verifica
                                                                                                           // se não são
                                                                                                           // nulos

                        Livro l1 = new Livro(null, "O Senhor dos Anéis", "J.R.R. Tolkien", "1234567890", 1954, 5, fantasiaSalva, estante1Salva);
                        Livro l2 = new Livro(null, "Harry Potter e a Pedra Filosofal", "J.K. Rowling", "0987654321", 1997, 3, fantasiaSalva, estante2Salva);
                        Livro l3 = new Livro(null, "O Hobbit", "J.R.R. Tolkien", "1122334455", 1937, 4, fantasiaSalva, estante3Salva);
                        Livro l4 = new Livro(null, "Dom Casmurro", "Machado de Assis", "2233445566", 1899, 2, romanceSalvo, estante1Salva);
                        Livro l5 = new Livro(null, "A Moreninha", "Joaquim Manuel de Macedo", "3344556677", 1844, 1, romanceSalvo, estante2Salva);
                        Livro l6 = new Livro(null, "O Pequeno Príncipe", "Antoine de Saint-Exupéry", "4455667788", 1943, 6, ficcaoSalva, estante3Salva); // Use ficcaoSalva
                        Livro l7 = new Livro(null, "A Revolução dos Bichos", "George Orwell", "5566778899", 1945, 2, ficcaoSalva, estante1Salva); // Use ficcaoSalva
                        Livro l8 = new Livro(null, "1984", "George Orwell", "6677889900", 1949, 3, ficcaoSalva, estante2Salva);

                        livroRepository.saveAll(Arrays.asList(l1, l2, l3, l4, l5, l6, l7, l8));
                        System.out.println(">>> Livros carregados com sucesso!");
                } else {
                        System.out.println(">>> Erro ao carregar categorias ou estantes salvas.");
                }
        }
}