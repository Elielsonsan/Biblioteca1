-- Limpa as tabelas para garantir um estado inicial limpo para os testes.
-- A ordem é importante para respeitar as chaves estrangeiras.
DELETE FROM emprestimo;
DELETE FROM livro;
DELETE FROM usuarios;
DELETE FROM categoria;
DELETE FROM estante;

-- Insere dados de teste para categorias e estantes
INSERT INTO categoria (id, nome) VALUES
(1, 'Fantasia'),
(2, 'Ficção Científica');

INSERT INTO estante (id, nome) VALUES
('E001', 'Corredor A1'),
('E002', 'Corredor B2');

-- Insere dados de teste para livros
INSERT INTO livro (id, titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES
(101, 'O Hobbit', 'J.R.R. Tolkien', '9788578279832', 1937, 1, '/images/hobbit.png', 1, 'E001'),
(102, 'Duna', 'Frank Herbert', '9788576570013', 1965, 1, '/images/duna.png', 2, 'E002'),
(103, 'Neuromancer', 'William Gibson', '9788576572994', 1984, 1, '/images/neuromancer.png', 2, 'E001');

-- Insere um usuário e um empréstimo ativo para o livro "Duna", tornando-o indisponível
INSERT INTO usuarios (id, name, email, cpf, income, birth_date, children_count, address, city, state, zip_code) VALUES
(201, 'Leitor Teste', 'leitor@teste.com', '11122233344', 2000.00, '1990-01-01', 0, 'Rua Teste', 'Cidade Teste', 'TS', '12345-000');

INSERT INTO emprestimo (id, usuario_id, livro_id, data_emprestimo, data_devolucao) VALUES
(301, 201, 102, '2024-05-01T10:00:00Z', NULL);