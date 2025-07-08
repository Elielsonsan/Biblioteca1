package org.iftm.biblioteca.config;

import java.util.List;

import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.impl.CapaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Component;

/**
 * Componente que é executado uma vez na inicialização da aplicação.
 * <p>
 * Implementa a interface {@link CommandLineRunner}, que instrui o Spring a
 * executar o método {@code run()} após o contexto da aplicação ter sido
 * completamente carregado.
 * <p>
 * O objetivo deste inicializador é verificar todos os livros no banco de dados e,
 * para aqueles que têm uma URL de capa externa (HTTP), baixar a imagem,
 * salvá-la localmente e atualizar o registro do livro com o novo caminho local.
 * Isso torna a aplicação mais robusta e rápida, pois não depende mais de
 * recursos externos.
 */
@Component // Ativa este componente para ser executado na inicialização
public class CapaInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CapaInitializer.class);

    // Injeção de dependências necessárias via construtor (melhor prática).
    private final LivroRepository livroRepository;
    private final CapaService capaService;

    public CapaInitializer(LivroRepository livroRepository, CapaService capaService) {
        this.livroRepository = livroRepository;
        this.capaService = capaService;
    }

    /**
     * O método principal que será executado pelo Spring na inicialização.
     * <p>
     * A anotação {@code @Transactional} garante que todas as operações de banco de dados
     * (leitura e salvamento de múltiplos livros) ocorram dentro de uma única transação.
     * Se ocorrer um erro no meio do processo, todas as alterações anteriores são desfeitas (rollback),
     * mantendo a consistência dos dados.
     */
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Iniciando verificação e download de capas de livros...");
        List<Livro> todosOsLivros = livroRepository.findAll();

        for (Livro livro : todosOsLivros) {
            String urlAtual = livro.getCapaUrl();
            // Processa apenas os livros que têm uma URL de capa e que é uma URL externa.
            if (urlAtual != null && urlAtual.startsWith("http")) {
                logger.info("Baixando capa para o livro '{}' (ISBN: {})", livro.getTitulo(), livro.getIsbn());
                String novaUrl = capaService.baixarSalvarCapa(urlAtual, livro.getIsbn());
                livro.setCapaUrl(novaUrl);
                livroRepository.save(livro); // Salva a entidade atualizada de volta no banco.
            }
        }
        logger.info("Verificação de capas concluída.");
    }
}