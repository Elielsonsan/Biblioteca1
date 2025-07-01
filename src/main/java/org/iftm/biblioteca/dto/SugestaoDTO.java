package org.iftm.biblioteca.dto;

/**
 * DTO para transportar dados de sugestão para o autocomplete.
 * Usar um record é uma forma concisa de criar uma classe de dados imutável.
 *
 * @param valor O texto da sugestão (ex: "Paulo Freire").
 * @param tipo  O tipo da sugestão (ex: "autor", "titulo", "isbn").
 */
public record SugestaoDTO(String valor, String tipo) {
}