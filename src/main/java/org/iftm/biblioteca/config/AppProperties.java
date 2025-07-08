package org.iftm.biblioteca.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Centraliza todas as configurações personalizadas da aplicação de forma segura e organizada.
 * <p>
 * Esta classe é mapeada para as propriedades definidas no arquivo {@code application.properties}
 * que começam com o prefixo "app". Por exemplo, uma propriedade como {@code app.emprestimo.prazo-dias=20}
 * seria automaticamente injetada no campo {@code prazoDias} da classe aninhada {@code Emprestimo}.
 * <p>
 * Esta abordagem é superior a "hardcodar" valores, pois permite mudanças fáceis de configuração
 * sem alterar o código-fonte, além de suportar diferentes configurações para diferentes
 * ambientes (ex: desenvolvimento, produção).
 */
// A anotação @Configuration é removida pois a classe será ativada via @EnableConfigurationProperties
// na classe principal da aplicação, o que é uma abordagem mais explícita e robusta.
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    // Inicializa os objetos de configuração aninhados.
    private final Upload upload = new Upload();
    private final Emprestimo emprestimo = new Emprestimo();

    public Upload getUpload() { return upload; }

    /**
     * Agrupa as configurações relacionadas ao upload de arquivos, como as capas dos livros.
     */
    public static class Upload {
        /**
         * O caminho absoluto no sistema de arquivos onde as capas dos livros serão armazenadas.
         * O valor padrão aponta para um diretório dentro da pasta "home" do usuário,
         * o que é uma prática robusta por manter os dados da aplicação (uploads)
         * separados do código executável (o arquivo JAR).
         */
        private String path = System.getProperty("user.home") + "/biblioteca_files/capas";

        public String getPath() { return path; }

        /**
         * Permite que o Spring injete o valor de {@code app.upload.path} do arquivo de propriedades.
         */
        public void setPath(String path) { this.path = path; }
    }

    public Emprestimo getEmprestimo() { return emprestimo; }

    /**
     * Agrupa as configurações relacionadas às regras de negócio para empréstimos de livros.
     */
    public static class Emprestimo {
        /**
         * O prazo padrão do empréstimo em dias. Este valor é usado para calcular
         * as datas de devolução e identificar empréstimos atrasados.
         */
        private int prazoDias = 15;

        /**
         * O número máximo de empréstimos ativos (não devolvidos) que um único
         * usuário pode ter.
         */
        private int limiteAtivos = 3;

        public int getPrazoDias() { return prazoDias; }

        /**
         * Permite que o Spring injete o valor de {@code app.emprestimo.prazo-dias} do arquivo de propriedades.
         */
        public void setPrazoDias(int prazoDias) { this.prazoDias = prazoDias; }

        public int getLimiteAtivos() { return limiteAtivos; }

        /**
         * Permite que o Spring injete o valor de {@code app.emprestimo.limite-ativos} do arquivo de propriedades.
         */
        public void setLimiteAtivos(int limiteAtivos) { this.limiteAtivos = limiteAtivos; }
    }
}