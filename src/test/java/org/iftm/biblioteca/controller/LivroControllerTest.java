package org.iftm.biblioteca.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Garante que o perfil de teste seja usado (ex: application-test.properties)
@Transactional // Assegura que cada teste rode em sua própria transação, que é revertida ao final
public class LivroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar todos os livros paginados quando nenhum filtro é aplicado")
    @Sql("/test-data.sql") // Carrega os dados de teste antes de executar
    void findPaginated_whenNoFilters_shouldReturnAllBooksWithCorrectStatus() throws Exception {
        mockMvc.perform(get("/api/livros")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "id,asc")) // Ordena por ID para resultados previsíveis
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.totalElements", is(3)))
            .andExpect(jsonPath("$.content", hasSize(3)))
            // Verifica o status de disponibilidade de cada livro
            .andExpect(jsonPath("$.content[0].titulo", is("O Hobbit")))
            .andExpect(jsonPath("$.content[0].statusDisponibilidade", is("Disponível")))
            .andExpect(jsonPath("$.content[1].titulo", is("Duna")))
            .andExpect(jsonPath("$.content[1].statusDisponibilidade", is("Indisponível")))
            .andExpect(jsonPath("$.content[2].titulo", is("Neuromancer")))
            .andExpect(jsonPath("$.content[2].statusDisponibilidade", is("Disponível")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por termo geral")
    @Sql("/test-data.sql")
    void findPaginated_whenSearchingByTerm_shouldReturnMatchingBooks() throws Exception {
        mockMvc.perform(get("/api/livros")
                .param("termo", "Duna"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("Duna")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por título específico")
    @Sql("/test-data.sql")
    void findPaginated_whenSearchingByTitle_shouldReturnMatchingBooks() throws Exception {
        mockMvc.perform(get("/api/livros")
                .param("titulo", "Hobbit"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("O Hobbit")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por autor")
    @Sql("/test-data.sql")
    void findPaginated_whenSearchingByAuthor_shouldReturnMatchingBooks() throws Exception {
        mockMvc.perform(get("/api/livros")
                .param("autor", "Gibson"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("Neuromancer")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por ID da categoria")
    @Sql("/test-data.sql")
    void findPaginated_whenSearchingByCategoryId_shouldReturnMatchingBooks() throws Exception {
        // Categoria 'Fantasia' tem ID 1 no script de teste
        mockMvc.perform(get("/api/livros")
                .param("categoriaId", "1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("O Hobbit")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por ID da estante")
    @Sql("/test-data.sql")
    void findPaginated_whenSearchingByEstanteId_shouldReturnMatchingBooks() throws Exception {
        // Estante 'Corredor B2' tem ID 'E002' no script de teste
        mockMvc.perform(get("/api/livros")
                .param("estanteId", "E002"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("Duna")));
    }

    @Test
    @DisplayName("Deve retornar livros filtrados por uma combinação de autor e categoria")
    @Sql("/test-data.sql")
    void findPaginated_whenUsingMultipleFilters_shouldReturnMatchingBooks() throws Exception {
        // Busca por livros de 'William Gibson' na categoria 'Ficção Científica' (ID 2)
        mockMvc.perform(get("/api/livros")
                .param("autor", "Gibson")
                .param("categoriaId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(1)))
            .andExpect(jsonPath("$.content[0].titulo", is("Neuromancer")));
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando nenhum livro corresponde ao filtro")
    @Sql("/test-data.sql")
    void findPaginated_whenNoResults_shouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/api/livros")
                .param("titulo", "Livro Inexistente"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements", is(0)))
            .andExpect(jsonPath("$.content", hasSize(0)));
    }
}