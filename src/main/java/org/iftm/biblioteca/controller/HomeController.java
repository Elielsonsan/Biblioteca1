package org.iftm.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Alterado para Controller para servir páginas Thymeleaf
public class HomeController {

    /**
     * Página principal da aplicação.
     *
     * @return o nome do template da página inicial (home.html)
     */
    @GetMapping("/") // Mapeia requisições GET para o caminho raiz "/"
    public String homePage() {
        // O Spring Boot procurará por /src/main/resources/templates/home.html
        return "home";
    }

    // Se você estivesse usando um template engine como Thymeleaf,
    // você removeria @ResponseBody e retornaria o nome do arquivo HTML:
    // public String home() {
    // return "index"; // Procuraria por src/main/resources/templates/index.html
    // }
}