-- Inserindo Categorias
INSERT INTO tb_categoria (id, nome) VALUES (1, 'Ficção Científica');
INSERT INTO tb_categoria (id, nome) VALUES (2, 'Fantasia');
INSERT INTO tb_categoria (id, nome) VALUES (3, 'Romance');
INSERT INTO tb_categoria (id, nome) VALUES (4, 'Técnico');
INSERT INTO tb_categoria (id, nome) VALUES (5, 'História');

-- Inserindo Estantes
INSERT INTO tb_estante (id, nome) VALUES (1, 'Corredor A - Ficção');
INSERT INTO tb_estante (id, nome) VALUES (2, 'Corredor B - Fantasia e Romance');
INSERT INTO tb_estante (id, nome) VALUES (3, 'Corredor C - Não-Ficção');
INSERT INTO tb_estante (id, nome) VALUES (4, 'Seção Especial - TI');

-- Inserindo Clientes
INSERT INTO tb_client (id, name, email) VALUES (1, 'Ana Silva', 'ana.silva@example.com');
INSERT INTO tb_client (id, name, email) VALUES (2, 'Bruno Costa', 'bruno.costa@example.com');
INSERT INTO tb_client (id, name, email) VALUES (3, 'Carla Dias', 'carla.dias@example.com');
INSERT INTO tb_client (id, name, email) VALUES (4, 'Daniel Faria', 'daniel.faria@example.com');
INSERT INTO tb_client (id, name, email) VALUES (5, 'Elisa Moreira', 'elisa.moreira@example.com');

-- Inserindo Livros
-- (ano_publicacao, autor, categoria_id, edicao, estante_id, isbn, titulo, id)

-- Ficção Científica (Categoria ID: 1)
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (1, 'Duna', 'Frank Herbert', '978-8576570010', 1965, 1, 1, 1);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (2, 'Fundação', 'Isaac Asimov', '978-8576570386', 1951, 1, 1, 1);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (3, 'Neuromancer', 'William Gibson', '978-8576572007', 1984, 1, 1, 1);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (4, 'O Guia do Mochileiro das Galáxias', 'Douglas Adams', '978-8599296227', 1979, 1, 1, 1);

-- Fantasia (Categoria ID: 2)
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (5, 'O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', '978-8533613379', 1954, 50, 2, 2);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (6, 'Harry Potter e a Pedra Filosofal', 'J.K. Rowling', '978-8532511010', 1997, 1, 2, 2);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (7, 'A Guerra dos Tronos', 'George R.R. Martin', '978-8580442789', 1996, 1, 2, 2);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (8, 'O Hobbit', 'J.R.R. Tolkien', '978-8533608429', 1937, 1, 2, 2);

-- Romance (Categoria ID: 3)
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (9, 'Orgulho e Preconceito', 'Jane Austen', '978-8525416400', 1813, 1, 3, 2);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (10, 'O Morro dos Ventos Uivantes', 'Emily Brontë', '978-8525416394', 1847, 1, 3, 2);

-- Técnico (Categoria ID: 4)
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (11, 'Código Limpo: Habilidades Práticas do Agile Software', 'Robert C. Martin', '978-8576082675', 2008, 1, 4, 4);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (12, 'Arquitetura Limpa: O Guia do Artesão para Estrutura e Design de Software', 'Robert C. Martin', '978-8550804606', 2017, 1, 4, 4);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (13, 'Java Efetivo', 'Joshua Bloch', '978-8576085324', 2018, 3, 4, 4);

-- História (Categoria ID: 5)
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (14, 'Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', '978-8535925248', 2011, 1, 5, 3);
INSERT INTO tb_livro (id, titulo, author, isbn, ano_publicacao, edicao, categoria_id, estante_id) VALUES (15, '1808', 'Laurentino Gomes', '978-8525056708', 2007, 1, 5, 3);