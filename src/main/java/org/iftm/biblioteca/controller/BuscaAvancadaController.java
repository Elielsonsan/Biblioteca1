package org.iftm.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuscaAvancadaController {

    /**
     * Mapeia a requisição GET para /busca-avancada e retorna o template Thymeleaf.
     *
     * @return o nome do template da página de busca avançada (busca-avancada.html)
     */
    @GetMapping("/busca-avancada")
    public String buscaAvancadaPage() {
        // O Spring Boot procurará por /src/main/resources/templates/busca-avancada.html
        return "busca-avancada";
    }
}