package org.iftm.biblioteca.config;

import java.util.List;

import org.iftm.biblioteca.entities.Livro;
import org.iftm.biblioteca.repository.LivroRepository;
import org.iftm.biblioteca.service.impl.LivroServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;

// @Component // Desativado temporariamente para usar o data.sql
public class CapaInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CapaInitializer.class);

    private final LivroRepository livroRepository;
    private final LivroServiceImpl livroService;

    public CapaInitializer(LivroRepository livroRepository, LivroServiceImpl livroService) {
        this.livroRepository = livroRepository;
        this.livroService = livroService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("Iniciando verificação e download de capas de livros...");
        List<Livro> todosOsLivros = livroRepository.findAll();
        
        for (Livro livro : todosOsLivros) {
            String urlAtual = livro.getCapaUrl();
            if (urlAtual != null && urlAtual.startsWith("http")) {
                logger.info("Baixando capa para o livro '{}' (ISBN: {})", livro.getTitulo(), livro.getIsbn());
                String novaUrl = livroService.baixarSalvarCapa(urlAtual, livro.getIsbn());
                livro.setCapaUrl(novaUrl);
                livroRepository.save(livro);
            }
        }
        logger.info("Verificação de capas concluída.");
    }
}