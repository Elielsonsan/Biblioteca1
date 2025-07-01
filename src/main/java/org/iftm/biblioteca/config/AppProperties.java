package org.iftm.biblioteca.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe para agrupar e gerenciar as propriedades de configuração da aplicação
 * de forma centralizada e type-safe.
 * Mapeia as propriedades que começam com o prefixo "app".
 */
// A anotação @Configuration é removida pois a classe será ativada via @EnableConfigurationProperties
// na classe principal da aplicação, o que é uma abordagem mais explícita e robusta.
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private final Upload upload = new Upload();

    public Upload getUpload() {
        return upload;
    }

    public static class Upload {
        // Define um valor padrão diretamente na classe, tornando a aplicação mais resiliente.
        private String path = "src/main/resources/static/images/capas";

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }
}