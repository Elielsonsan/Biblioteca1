package org.iftm.biblioteca.dto;

/**
 * DTO (Data Transfer Object) para transportar dados de sugestão para funcionalidades de autocomplete.
 * <p>
 * A utilização de um 'record' Java é a abordagem mais moderna e ideal para este caso de uso,
 * pois cria uma classe de dados imutável, concisa e segura. O compilador gera
 * automaticamente o construtor, os métodos de acesso (getters), `equals()`, `hashCode()` e `toString()`.
 *
 * @param valor O texto da sugestão (ex: "Paulo Freire").
 * @param tipo  O tipo da sugestão (ex: "autor", "titulo", "isbn").
 *              a categorizar ou exibir as sugestões de forma diferente.
 */
public record SugestaoDTO(String valor, String tipo) {
}