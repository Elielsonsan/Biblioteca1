package org.iftm.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody; // Use isto para retornar texto simples

@Controller // Ou @RestController se for uma API REST
public class HomeController {

    /**
     * P gina principal da aplica o.
     *
     * @return uma mensagem de boas-vindas
     */
    @GetMapping("/") // Mapeia requisições GET para o caminho raiz "/"
    @ResponseBody  // Retorna o valor diretamente como corpo da resposta (texto)
    public String home() {
        return "Bem-vindo à Biblioteca!"; // Mensagem simples a ser exibida
    }

    // Se você estivesse usando um template engine como Thymeleaf,
    // você removeria @ResponseBody e retornaria o nome do arquivo HTML:
    // public String home() {
    //     return "index"; // Procuraria por src/main/resources/templates/index.html
    // }
}