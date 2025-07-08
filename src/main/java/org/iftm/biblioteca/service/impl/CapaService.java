package org.iftm.biblioteca.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.iftm.biblioteca.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CapaService {

    private static final Logger logger = LoggerFactory.getLogger(CapaService.class);
    private final Path diretorioUpload;

    public CapaService(AppProperties appProperties) {
        this.diretorioUpload = Paths.get(appProperties.getUpload().getPath());
    }

    /**
     * Baixa uma imagem de capa de uma URL externa e a salva localmente.
     * Se a URL já for um caminho local ou a operação falhar, retorna a URL original.
     *
     * @param urlExterna A URL da imagem a ser baixada.
     * @param isbnNormalizado O ISBN do livro, já normalizado (sem hifens ou espaços), para ser usado como nome do arquivo.
     * @return O caminho local para a imagem salva (ex: /images/capas/9788576082675.jpg) ou a URL original.
     */
    public String baixarSalvarCapa(String urlExterna, String isbnNormalizado) {
        // Se a URL for nula, vazia ou já for um caminho local, não faz nada.
        if (urlExterna == null || urlExterna.trim().isEmpty() || !urlExterna.startsWith("http")) {
            return urlExterna;
        }

        try {
            URL url = new URL(urlExterna);
            
            String extensao = ".jpg"; // Padrão
            int lastDot = urlExterna.lastIndexOf(".");
            if (lastDot > 0 && lastDot > urlExterna.lastIndexOf("/")) {
                String ext = urlExterna.substring(lastDot);
                if (ext.length() <= 5 && !ext.contains("?")) {
                    extensao = ext;
                }
            }

            String novoNomeArquivo = isbnNormalizado + extensao;
            Files.createDirectories(diretorioUpload);
            Path arquivoDestino = diretorioUpload.resolve(novoNomeArquivo);

            try (InputStream in = url.openStream()) {
                Files.copy(in, arquivoDestino, StandardCopyOption.REPLACE_EXISTING);
                logger.info("Capa salva com sucesso para o ISBN {}: {}", isbnNormalizado, arquivoDestino);
            }
            return "/images/capas/" + novoNomeArquivo;
        } catch (IOException e) {
            logger.error("Erro ao baixar ou salvar a capa do livro com ISBN {}. URL: {}. Erro: {}", isbnNormalizado, urlExterna, e.getMessage());
            return urlExterna;
        }
    }
}