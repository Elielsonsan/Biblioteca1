-- Inserir Categorias
INSERT INTO tb_categoria (nome) VALUES ('Ficção Científica');
INSERT INTO tb_categoria (nome) VALUES ('Fantasia');
INSERT INTO tb_categoria (nome) VALUES ('Mistério');
INSERT INTO tb_categoria (nome) VALUES ('Não-Ficção');
INSERT INTO tb_categoria (nome) VALUES ('Romance');

-- Inserir Estantes (com ID e Nome)
INSERT INTO tb_estante (id, nome) VALUES ('E001', 'Corredor A - Ficção Científica');
INSERT INTO tb_estante (id, nome) VALUES ('E002', 'Corredor A - Fantasia');
INSERT INTO tb_estante (id, nome) VALUES ('E003', 'Corredor B - Mistério e Romance');
INSERT INTO tb_estante (id, nome) VALUES ('E004', 'Corredor C - Não-Ficção');

-- Inserir Livros (IDs de categoria são 1, 2, 3, 4, 5)
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Duna', 'Frank Herbert', '9780441013593', 1965, 1, '/images/capas/9780441013593.jpg', 1, 'E001');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Hobbit', 'J.R.R. Tolkien', '9780345339683', 1937, 1, '/images/capas/9780345339683.jpg', 2, 'E002');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Código Da Vinci', 'Dan Brown', '9780307474278', 2003, 1, '/images/capas/9780307474278.jpg', 3, 'E003');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', '9780062316097', 2011, 1, '/images/capas/9780062316097.jpg', 4, 'E004');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Orgulho e Preconceito', 'Jane Austen', '9780141439518', 1813, 1, '/images/capas/9780141439518.jpg', 5, 'E003');