package org.iftm.biblioteca.service;

 import org.iftm.biblioteca.entities.Livro; 
 import java.util.List;
 import java.util.Optional;
 

 public interface LivroService {

     // --- CRUD ---
     Livro salvarNovoLivro(Livro livro);             // Inserir um registro
     List<Livro> salvarTodosLivros(List<Livro> livros); // Inserir um conjunto
     Livro atualizarLivro(Long id, Livro livro);    // Modificar um registro
     void apagarLivroPorId(Long id);                 // Apagar um registro
     void apagarTodosLivros();                       // Apagar todos os registros

     // --- CONSULTAS (Mínimo 5) ---
     List<Livro> buscarTodos();
     Optional<Livro> buscarPorId(Long id);
     List<Livro> buscarPorTituloContendo(String trechoTitulo);
     List<Livro> buscarPorAutor(String autor);
     List<Livro> buscarPorCategoria(Long categoriaId); // Consulta usando ID da categoria
     List<Livro> buscarLivrosPublicadosAposAno(Integer ano); // Consulta que pode se relacionar a uma regra
 }