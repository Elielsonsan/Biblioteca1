-- Inserindo Categorias
INSERT INTO tb_categoria (nome) VALUES ('Ficção Científica');
INSERT INTO tb_categoria (nome) VALUES ('Fantasia');
INSERT INTO tb_categoria (nome) VALUES ('Romance');
INSERT INTO tb_categoria (nome) VALUES ('Técnico');
INSERT INTO tb_categoria (nome) VALUES ('História');
INSERT INTO tb_categoria (nome) VALUES ('Suspense');
INSERT INTO tb_categoria (nome) VALUES ('Biografia');
INSERT INTO tb_categoria (nome) VALUES ('Poesia');
INSERT INTO tb_categoria (nome) VALUES ('Infantojuvenil');
INSERT INTO tb_categoria (nome) VALUES ('Autoajuda');

-- Inserindo Estantes
INSERT INTO tb_estante (nome) VALUES ('Corredor A - Ficção Científica');
INSERT INTO tb_estante (nome) VALUES ('Corredor B - Fantasia');
INSERT INTO tb_estante (nome) VALUES ('Corredor C - Romance');
INSERT INTO tb_estante (nome) VALUES ('Seção Especial - Técnico');
INSERT INTO tb_estante (nome) VALUES ('Corredor D - História');
INSERT INTO tb_estante (nome) VALUES ('Corredor E - Suspense');
INSERT INTO tb_estante (nome) VALUES ('Corredor F - Biografias');
INSERT INTO tb_estante (nome) VALUES ('Seção Poesia');
INSERT INTO tb_estante (nome) VALUES ('Seção Infantojuvenil');
INSERT INTO tb_estante (nome) VALUES ('Seção Autoajuda');

-- Inserindo Clientes
-- A tabela de clientes pode manter os IDs se for necessário para alguma lógica específica, mas o ideal seria remover também.
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (1, 'Ana Silva', 'ana.silva@example.com', '111.111.111-11', 2500.00, '1990-05-15', 0, 'Rua das Flores, 123', 'São Paulo', 'SP', '01000-001', 1);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (2, 'Bruno Costa', 'bruno.costa@example.com', '222.222.222-22', 3200.50, '1985-11-20', 2, 'Avenida Principal, 456', 'Rio de Janeiro', 'RJ', '02000-002', 2);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (3, 'Carla Dias', 'carla.dias@example.com', '333.333.333-33', 1800.75, '1995-02-10', 1, 'Travessa da Paz, 789', 'Belo Horizonte', 'MG', '03000-003', 1);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (4, 'Daniel Faria', 'daniel.faria@example.com', '444.444.444-44', 5000.00, '1980-07-01', 3, 'Praça Central, 101', 'Curitiba', 'PR', '04000-004', 3);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (5, 'Elisa Moreira', 'elisa.moreira@example.com', '555.555.555-55', 2200.00, '2000-01-30', 0, 'Alameda dos Sonhos, 202', 'Porto Alegre', 'RS', '05000-005', 2);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (6, 'Fernando Alves', 'fernando.alves@example.com', '666.666.666-66', 4500.00, '1975-09-05', 1, 'Rua da Ladeira, 303', 'Salvador', 'BA', '06000-006', 1);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (7, 'Gabriela Lima', 'gabriela.lima@example.com', '777.777.777-77', 2900.00, '1992-12-12', 2, 'Estrada Velha, 404', 'Fortaleza', 'CE', '07000-007', 3);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (8, 'Hugo Mendes', 'hugo.mendes@example.com', '888.888.888-88', 6000.00, '1988-03-25', 0, 'Rodovia do Sol, Km 5', 'Recife', 'PE', '08000-008', 1);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (9, 'Isabela Rocha', 'isabela.rocha@example.com', '999.999.999-99', 3500.00, '1998-06-18', 1, 'Caminho das Pedras, 606', 'Manaus', 'AM', '09000-009', 2);
INSERT INTO tb_client (id, name, email, cpf, income, birth_date, children_count, street, city, state, zip_code, category_id) VALUES (10, 'João Pereira', 'joao.pereira@example.com', '000.000.000-00', 2000.00, '1993-08-08', 0, 'Viela da Sorte, 707', 'Brasília', 'DF', '10000-010', 3);

-- Inserindo Livros
-- Removendo a coluna 'id' para deixar o banco de dados gerar automaticamente

-- Ficção Científica (Categoria ID: 1)
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Duna', 'Frank Herbert', '9788576574019', 1965, 1, 1, 1, '/images/capas/9788576574019.jpg');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Fundação', 'Isaac Asimov', '978-0000000002', 1951, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Neuromancer', 'William Gibson', '978-0000000003', 1984, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Guia do Mochileiro das Galáxias', 'Douglas Adams', '978-0000000004', 1979, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('1984', 'George Orwell', '9788535914849', 1949, 1, 1, 1, '/images/capas/9788535914849.jpg');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Admirável Mundo Novo', 'Aldous Huxley', '978-0000000006', 1932, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Fahrenheit 451', 'Ray Bradbury', '978-0000000007', 1953, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Homem do Castelo Alto', 'Philip K. Dick', '978-0000000008', 1962, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('A Máquina do Tempo', 'H.G. Wells', '978-0000000009', 1895, 1, 1, 1, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Eu, Robô', 'Isaac Asimov', '978-0000000010', 1950, 1, 1, 1, '/images/capa-placeholder.png');

-- Fantasia (Categoria ID: 2)
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', '978-0000000011', 1954, 50, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', '978-0000000012', 1997, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('A Guerra dos Tronos', 'George R.R. Martin', '978-0000000013', 1996, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Hobbit', 'J.R.R. Tolkien', '978-0000000014', 1937, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('As Crônicas de Nárnia: O Leão, a Feiticeira e o Guarda-Roupa', 'C.S. Lewis', '978-0000000015', 1950, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Nome do Vento', 'Patrick Rothfuss', '978-0000000016', 2007, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Eragon', 'Christopher Paolini', '978-0000000017', 2002, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('A Roda do Tempo: O Olho do Mundo', 'Robert Jordan', '978-0000000018', 1990, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Mistborn: O Império Final', 'Brandon Sanderson', '978-0000000019', 2006, 1, 2, 2, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Feiticeiro de Terramar', 'Ursula K. Le Guin', '978-0000000020', 1968, 1, 2, 2, '/images/capa-placeholder.png');

-- Romance (Categoria ID: 3)
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Romeu e Julieta', 'William Shakespeare', '978-0000000024', 1597, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Amor nos Tempos do Cólera', 'Gabriel García Márquez', '978-0000000025', 1985, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Anna Karenina', 'Liev Tolstói', '978-0000000026', 1877, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('O Grande Gatsby', 'F. Scott Fitzgerald', '978-0000000027', 1925, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('A Culpa é das Estrelas', 'John Green', '978-0000000028', 2012, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Como Eu Era Antes de Você', 'Jojo Moyes', '978-0000000029', 2012, 1, 3, 3, '/images/capa-placeholder.png');
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES ('Diário de uma Paixão', 'Nicholas Sparks', '978-0000000030', 1996, 1, 3, 3, '/images/capa-placeholder.png');

-- Técnico (Categoria ID: 4)

-- História (Categoria ID: 5)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (41, 'Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', '978-0000000041', 2011, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (42, '1808', 'Laurentino Gomes', '978-0000000042', 2007, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (43, 'Uma Breve História do Tempo', 'Stephen Hawking', '978-0000000043', 1988, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (44, 'Armas, Germes e Aço', 'Jared Diamond', '978-0000000044', 1997, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (45, 'O Diário de Anne Frank', 'Anne Frank', '978-0000000045', 1947, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (46, 'A Ascensão e Queda do Terceiro Reich', 'William L. Shirer', '978-0000000046', 1960, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (47, 'História Concisa do Brasil', 'Boris Fausto', '978-0000000047', 2001, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (48, 'Os Romanos', 'Indro Montanelli', '978-0000000048', 1957, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (49, 'A Era dos Extremos: O Breve Século XX', 'Eric Hobsbawm', '978-0000000049', 1994, 1, 5, 5, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (50, 'Cosmos', 'Carl Sagan', '978-0000000050', 1980, 1, 5, 5, '/images/capa-placeholder.png');

-- Suspense (Categoria ID: 6)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (51, 'O Código Da Vinci', 'Dan Brown', '978-0000000051', 2003, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (52, 'Garota Exemplar', 'Gillian Flynn', '978-0000000052', 2012, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (53, 'O Silêncio dos Inocentes', 'Thomas Harris', '978-0000000053', 1988, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (54, 'A Garota no Trem', 'Paula Hawkins', '978-0000000054', 2015, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (55, 'Os Homens que Não Amavam as Mulheres', 'Stieg Larsson', '978-0000000055', 2005, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (56, 'O Iluminado', 'Stephen King', '978-0000000056', 1977, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (57, 'Psicose', 'Robert Bloch', '978-0000000057', 1959, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (58, 'O Colecionador de Ossos', 'Jeffery Deaver', '978-0000000058', 1997, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (59, 'Não Conte a Ninguém', 'Harlan Coben', '978-0000000059', 2001, 1, 6, 6, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (60, 'A Paciente Silenciosa', 'Alex Michaelides', '978-0000000060', 2019, 1, 6, 6, '/images/capa-placeholder.png');

-- Biografia (Categoria ID: 7)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (61, 'Steve Jobs', 'Walter Isaacson', '978-0000000061', 2011, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (62, 'Minha História', 'Michelle Obama', '978-0000000062', 2018, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (63, 'Elon Musk', 'Ashlee Vance', '978-0000000063', 2015, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (64, 'A Menina da Montanha', 'Tara Westover', '978-0000000064', 2018, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (65, 'Leonardo da Vinci', 'Walter Isaacson', '978-0000000065', 2017, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (66, 'Eu Sou Malala', 'Malala Yousafzai', '978-0000000066', 2013, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (67, 'Churchill: Uma Vida', 'Martin Gilbert', '978-0000000067', 1991, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (68, 'Frida: A Biografia', 'Hayden Herrera', '978-0000000068', 1983, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (69, 'Nelson Mandela: Longa Caminhada Até a Liberdade', 'Nelson Mandela', '978-0000000069', 1994, 1, 7, 7, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (70, 'A Bailarina de Auschwitz', 'Edith Eger', '978-0000000070', 2017, 1, 7, 7, '/images/capa-placeholder.png');

-- Poesia (Categoria ID: 8)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (71, 'A Rosa do Povo', 'Carlos Drummond de Andrade', '978-0000000071', 1945, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (72, 'Ou Isto ou Aquilo', 'Cecília Meireles', '978-0000000072', 1964, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (73, 'Sonetos', 'Vinicius de Moraes', '978-0000000073', 1957, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (74, 'Folhas de Relva', 'Walt Whitman', '978-0000000074', 1855, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (75, 'O Corvo e Outros Poemas', 'Edgar Allan Poe', '978-0000000075', 1845, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (76, 'Poemas Escolhidos', 'Fernando Pessoa', '978-0000000076', 1942, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (77, 'Vinte Poemas de Amor e Uma Canção Desesperada', 'Pablo Neruda', '978-0000000077', 1924, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (78, 'As Flores do Mal', 'Charles Baudelaire', '978-0000000078', 1857, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (79, 'O Guardador de Rebanhos', 'Alberto Caeiro (Fernando Pessoa)', '978-0000000079', 1925, 1, 8, 8, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (80, 'Outros Jeitos de Usar a Boca', 'Rupi Kaur', '978-0000000080', 2014, 1, 8, 8, '/images/capa-placeholder.png');

-- Infantojuvenil (Categoria ID: 9)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (81, 'O Pequeno Príncipe', 'Antoine de Saint-Exupéry', '978-0000000081', 1943, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (82, 'Alice no País das Maravilhas', 'Lewis Carroll', '978-0000000082', 1865, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (83, 'Matilda', 'Roald Dahl', '978-0000000083', 1988, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (84, 'O Menino Maluquinho', 'Ziraldo', '978-0000000084', 1980, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (85, 'A Bolsa Amarela', 'Lygia Bojunga', '978-0000000085', 1976, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (86, 'Percy Jackson e o Ladrão de Raios', 'Rick Riordan', '978-0000000086', 2005, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (87, 'O Diário de um Banana', 'Jeff Kinney', '978-0000000087', 2007, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (88, 'Extraordinário', 'R.J. Palacio', '978-0000000088', 2012, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (89, 'A Droga da Obediência', 'Pedro Bandeira', '978-0000000089', 1984, 1, 9, 9, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (90, 'Onde Vivem os Monstros', 'Maurice Sendak', '978-0000000090', 1963, 1, 9, 9, '/images/capa-placeholder.png');

-- Autoajuda (Categoria ID: 10)
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (91, 'O Poder do Hábito', 'Charles Duhigg', '978-0000000091', 2012, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (92, 'Mindset: A Nova Psicologia do Sucesso', 'Carol S. Dweck', '978-0000000092', 2006, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (93, 'Os 7 Hábitos das Pessoas Altamente Eficazes', 'Stephen R. Covey', '978-0000000093', 1989, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (94, 'Como Fazer Amigos e Influenciar Pessoas', 'Dale Carnegie', '978-0000000094', 1936, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (95, 'A Coragem de Ser Imperfeito', 'Brené Brown', '978-0000000095', 2012, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (96, 'Pai Rico, Pai Pobre', 'Robert T. Kiyosaki', '978-0000000096', 1997, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (97, 'O Milagre da Manhã', 'Hal Elrod', '978-0000000097', 2012, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (98, 'A Sutil Arte de Ligar o F*da-se', 'Mark Manson', '978-0000000098', 2016, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (99, 'Mais Esperto que o Diabo', 'Napoleon Hill', '978-0000000099', 2011, 1, 10, 10, '/images/capa-placeholder.png');
INSERT INTO tb_livro (id, titulo, autor, isbn, ano_publicacao, edicao, categoria_id, estante_id, capa_url) VALUES (100, 'Essencialismo', 'Greg McKeown', '978-0000000100', 2014, 1, 10, 10, '/images/capa-placeholder.png');