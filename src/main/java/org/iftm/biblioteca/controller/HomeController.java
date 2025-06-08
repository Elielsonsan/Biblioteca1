package org.iftm.biblioteca.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // @RestController combina @Controller e @ResponseBody
public class HomeController {

    /**
     * Página principal da aplicação.
     *
     * @return uma mensagem de boas-vindas
     */
    @GetMapping("/") // Mapeia requisições GET para o caminho raiz "/"
    public String home() {
        return "Bem-vindo à Biblioteca!"; // Mensagem simples a ser exibida
    }

    // Se você estivesse usando um template engine como Thymeleaf,
    // você removeria @ResponseBody e retornaria o nome do arquivo HTML:
    // public String home() {
    // return "index"; // Procuraria por src/main/resources/templates/index.html
    // }
}