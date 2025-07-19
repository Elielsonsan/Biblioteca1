package org.iftm.biblioteca.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração avançada do Spring MVC para mapear e servir recursos estáticos
 * (como imagens de capas de livros) de um diretório externo ao projeto.
 * <p>
 * Esta classe é fundamental para desacoplar os arquivos de upload (que são dados)
 * do código da aplicação. Em um ambiente de produção, a aplicação geralmente é
 * empacotada em um arquivo JAR, que não deve ser modificado em tempo de execução.
 * Ao salvar as capas em um diretório externo, garantimos que a aplicação possa
 * ser atualizada sem perder os arquivos salvos.
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private AppProperties appProperties;

    /**
     * Configura os "resource handlers", que são os responsáveis por servir
     * arquivos estáticos como CSS, JavaScript e imagens.
     * <p>
     * Neste método, criamos uma regra que diz ao Spring: "Quando uma requisição
     * chegar para uma URL que comece com '/images/capas/', não procure esse
     * arquivo dentro do projeto, mas sim no diretório físico que eu especificar".
     */
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 1. Obtém o caminho físico do diretório de uploads a partir da classe de configurações.
        //    Isso mantém o código desacoplado de caminhos fixos ("hardcoded").
        Path uploadDir = Paths.get(appProperties.getUpload().getPath());
        // 2. Converte o caminho do sistema de arquivos para um formato de URI de recurso,
        //    que é o formato esperado pelo método `addResourceLocations`.
        //    É crucial adicionar "file:" no início e uma "/" no final para que o Spring
        //    o interprete corretamente como um diretório.
        //    Exemplo: "file:/home/ele/biblioteca_files/capas/"
        String uploadPath = uploadDir.toUri().toString();

        // 3. Mapeia o padrão de URL público (o que o navegador vai requisitar) para o local físico dos arquivos.
        registry.addResourceHandler("/images/capas/**")
                .addResourceLocations(uploadPath);
    }
}