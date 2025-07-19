-- =================================================================================
-- LISTAS 10 CATEGORIAS
-- =================================================================================
-- IDs de 1 a 10
INSERT INTO tb_categoria (nome) VALUES ('Tecnologia e Ciência da Computação'); -- ID 1
INSERT INTO tb_categoria (nome) VALUES ('Ficção Científica'); -- ID 2
INSERT INTO tb_categoria (nome) VALUES ('Fantasia'); -- ID 3
INSERT INTO tb_categoria (nome) VALUES ('História'); -- ID 4
INSERT INTO tb_categoria (nome) VALUES ('Biografia e Autobiografia'); -- ID 5
INSERT INTO tb_categoria (nome) VALUES ('Psicologia'); -- ID 6
INSERT INTO tb_categoria (nome) VALUES ('Filosofia'); -- ID 7
INSERT INTO tb_categoria (nome) VALUES ('Mistério e Suspense'); -- ID 8
INSERT INTO tb_categoria (nome) VALUES ('Romance'); -- ID 9
INSERT INTO tb_categoria (nome) VALUES ('Arte e Fotografia'); -- ID 10

-- =================================================================================
-- LISTA COM 10 ESTANTES
-- =================================================================================
INSERT INTO tb_estante (nome) VALUES ('Corredor T - Tecnologia'); -- ID 1
INSERT INTO tb_estante (nome) VALUES ('Corredor FC - Ficção Científica'); -- ID 2
INSERT INTO tb_estante (nome) VALUES ('Corredor FN - Fantasia'); -- ID 3
INSERT INTO tb_estante (nome) VALUES ('Corredor H - História'); -- ID 4
INSERT INTO tb_estante (nome) VALUES ('Corredor B - Biografias'); -- ID 5
INSERT INTO tb_estante (nome) VALUES ('Corredor P - Psicologia e Filosofia'); -- ID 6
INSERT INTO tb_estante (nome) VALUES ('Corredor M - Mistério'); -- ID 7
INSERT INTO tb_estante (nome) VALUES ('Corredor R - Romance'); -- ID 8
INSERT INTO tb_estante (nome) VALUES ('Corredor A - Arte'); -- ID 9
INSERT INTO tb_estante (nome) VALUES ('Corredor G - Geral'); -- ID 10

-- =================================================================================
-- LISTA COM 100 LIVROS (10 POR CATEGORIA)
-- =================================================================================

-- Categoria 1: Tecnologia e Ciência da Computação
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Código Limpo', 'Robert C. Martin', '9788576082675', 2008, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Programador Pragmático', 'Andrew Hunt, David Thomas', '9788573077147', 1999, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Arquitetura Limpa', 'Robert C. Martin', '9788550804606', 2017, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Padrões de Projetos', 'Erich Gamma, et al.', '9788573076102', 1994, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Refatoração', 'Martin Fowler', '9788575227244', 2018, 2, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Inteligência Artificial: Uma Abordagem Moderna', 'Stuart Russell, Peter Norvig', '9788543004523', 2021, 4, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Mítico Homem-Mês', 'Frederick P. Brooks Jr.', '9788550802381', 1975, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Domain-Driven Design', 'Eric Evans', '9780321125217', 2003, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Trabalho Eficaz com Código Legado', 'Michael Feathers', '9788577805333', 2004, 1, '/images/capa-placeholder.png', 1, 1);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Redes de Computadores', 'Andrew S. Tanenbaum', '9788543004738', 2011, 5, '/images/capa-placeholder.png', 1, 1);

-- Categoria 2: Ficção Científica
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Duna', 'Frank Herbert', '9780441013593', 1965, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Fundação', 'Isaac Asimov', '9780553803716', 1951, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('1984', 'George Orwell', '9780451524935', 1949, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Neuromancer', 'William Gibson', '9780441569595', 1984, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Guia do Mochileiro das Galáxias', 'Douglas Adams', '9780345391803', 1979, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Admirável Mundo Novo', 'Aldous Huxley', '9780060850524', 1932, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Fahrenheit 451', 'Ray Bradbury', '9781451673319', 1953, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Jogo do Exterminador', 'Orson Scott Card', '9780812550702', 1985, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Snow Crash', 'Neal Stephenson', '9780553380958', 1992, 1, '/images/capa-placeholder.png', 2, 2);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Mão Esquerda da Escuridão', 'Ursula K. Le Guin', '9780441478125', 1969, 1, '/images/capa-placeholder.png', 2, 2);

-- Categoria 3: Fantasia
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Senhor dos Anéis: A Sociedade do Anel', 'J.R.R. Tolkien', '9780618640157', 1954, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Hobbit', 'J.R.R. Tolkien', '9780345339683', 1937, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Harry Potter e a Pedra Filosofal', 'J.K. Rowling', '9780747532743', 1997, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Guerra dos Tronos', 'George R.R. Martin', '9780553381689', 1996, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Nome do Vento', 'Patrick Rothfuss', '9780756404741', 2007, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('As Crônicas de Nárnia', 'C.S. Lewis', '9780064471190', 1950, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Mistborn: O Império Final', 'Brandon Sanderson', '9780765311788', 2006, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Bússola de Ouro', 'Philip Pullman', '9780440418320', 1995, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Feiticeiro de Terramar', 'Ursula K. Le Guin', '9780547722023', 1968, 1, '/images/capa-placeholder.png', 3, 3);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('American Gods', 'Neil Gaiman', '9780380789030', 2001, 1, '/images/capa-placeholder.png', 3, 3);

-- Categoria 4: História
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Sapiens: Uma Breve História da Humanidade', 'Yuval Noah Harari', '9780062316097', 2011, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Armas, Germes e Aço', 'Jared Diamond', '9780393317558', 1997, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('1491: Novas Revelações das Américas Antes de Colombo', 'Charles C. Mann', '9781400032051', 2005, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Diário de Anne Frank', 'Anne Frank', '9780553296983', 1947, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Ascensão e Queda do Terceiro Reich', 'William L. Shirer', '9780671728687', 1960, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('SPQR: Uma História da Roma Antiga', 'Mary Beard', '9781631492228', 2015, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('1808', 'Laurentino Gomes', '9788576653202', 2007, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('1822', 'Laurentino Gomes', '9788525049208', 2010, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('1889', 'Laurentino Gomes', '9788525055155', 2013, 1, '/images/capa-placeholder.png', 4, 4);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Homo Deus: Uma Breve História do Amanhã', 'Yuval Noah Harari', '9780062464316', 2015, 1, '/images/capa-placeholder.png', 4, 4);

-- Categoria 5: Biografia e Autobiografia
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Steve Jobs', 'Walter Isaacson', '9781451648539', 2011, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Longa Caminhada Até a Liberdade', 'Nelson Mandela', '9780316320521', 1994, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Eu Sei Por Que o Pássaro Canta na Gaiola', 'Maya Angelou', '9780375507892', 1969, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Minha História', 'Michelle Obama', '9781524763138', 2018, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'A Marca da Vitória', 'Phil Knight', '9781501135910', 2016, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Leonardo da Vinci', 'Walter Isaacson', '9781501139154', 2017, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'Elon Musk', 'Ashlee Vance', '9780062301239', 2015, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'O Diário de um Mago', 'Paulo Coelho', '9788576653332', 1987, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'A Menina do Vale', 'Bel Pesce', '9788578882339', 2012, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES (
    'A Autobiografia de Martin Luther King', 'Martin Luther King Jr., Clayborne Carson', '9780446676502', 1998, 1, '/images/capa-placeholder.png',
    (SELECT id FROM tb_categoria WHERE nome = 'Biografia e Autobiografia'),
    (SELECT id FROM tb_estante WHERE nome = 'Corredor B - Biografias')
);

-- Categoria 6: Psicologia
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Rápido e Devagar: Duas Formas de Pensar', 'Daniel Kahneman', '9780374533557', 2011, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Poder do Hábito', 'Charles Duhigg', '9781400069286', 2012, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Inteligência Emocional', 'Daniel Goleman', '9780553383713', 1995, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Mindset: A Nova Psicologia do Sucesso', 'Carol S. Dweck', '9780345472328', 2006, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Em Busca de Sentido', 'Viktor E. Frankl', '9780807014271', 1946, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Coragem de Ser Imperfeito', 'Brené Brown', '9781592408412', 2012, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Comunicação Não-Violenta', 'Marshall B. Rosenberg', '9781892005038', 1999, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Homem e Seus Símbolos', 'Carl G. Jung', '9780440336352', 1964, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Os 7 Hábitos das Pessoas Altamente Eficazes', 'Stephen R. Covey', '9780743269513', 1989, 1, '/images/capa-placeholder.png', 6, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Interpretação dos Sonhos', 'Sigmund Freud', '9780486419232', 1899, 1, '/images/capa-placeholder.png', 6, 6);

-- Categoria 7: Filosofia
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Mundo de Sofia', 'Jostein Gaarder', '9788535909473', 1991, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A República', 'Platão', '9789723109798', -380, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Assim Falou Zaratustra', 'Friedrich Nietzsche', '9788535911865', 1883, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Príncipe', 'Nicolau Maquiavel', '9788525419875', 1532, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Ética a Nicômaco', 'Aristóteles', '9788572836603', -340, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Banquete', 'Platão', '9788572325548', -385, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Crítica da Razão Pura', 'Immanuel Kant', '9789723106216', 1781, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Mito de Sísifo', 'Albert Camus', '9788501017933', 1942, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Meditações', 'Marco Aurélio', '9788572839321', 180, 1, '/images/capa-placeholder.png', 7, 6);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Ser e o Nada', 'Jean-Paul Sartre', '9788532605030', 1943, 1, '/images/capa-placeholder.png', 7, 6);

-- Categoria 8: Mistério e Suspense
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Silêncio dos Inocentes', 'Thomas Harris', '9788532504149', 1988, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Garota Exemplar', 'Gillian Flynn', '9788580572922', 2012, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Código Da Vinci', 'Dan Brown', '9788575421137', 2003, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('E Não Sobrou Nenhum', 'Agatha Christie', '9788525059634', 1939, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Garota no Trem', 'Paula Hawkins', '9788501104643', 2015, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Paciente Silencioso', 'Alex Michaelides', '9788501116431', 2019, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Mulher na Janela', 'A.J. Finn', '9788580418008', 2018, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Os Homens que Não Amavam as Mulheres', 'Stieg Larsson', '9788535912800', 2005, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Sobre os Ossos dos Mortos', 'Olga Tokarczuk', '9788580333394', 2009, 1, '/images/capa-placeholder.png', 8, 7);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Iluminado', 'Stephen King', '9788532528221', 1977, 1, '/images/capa-placeholder.png', 8, 7);

-- Categoria 9: Romance
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Orgulho e Preconceito', 'Jane Austen', '9788535914859', 1813, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Morro dos Ventos Uivantes', 'Emily Brontë', '9788535914866', 1847, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Cem Anos de Solidão', 'Gabriel García Márquez', '9788501012624', 1967, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Amor nos Tempos do Cólera', 'Gabriel García Márquez', '9788501028557', 1985, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Culpa é das Estrelas', 'John Green', '9788580573462', 2012, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Como Eu Era Antes de Você', 'Jojo Moyes', '9788580573271', 2012, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Outlander: A Viajante do Tempo', 'Diana Gabaldon', '9788580412532', 1991, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Eleanor & Park', 'Rainbow Rowell', '9788542801469', 2012, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Grande Gatsby', 'F. Scott Fitzgerald', '9788563560058', 1925, 1, '/images/capa-placeholder.png', 9, 8);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Dom Casmurro', 'Machado de Assis', '9788535914873', 1899, 1, '/images/capa-placeholder.png', 9, 8);

-- Categoria 10: Arte e Fotografia
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A História da Arte', 'E.H. Gombrich', '9788521611858', 1950, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Isto é Arte?', 'Will Gompertz', '9788537810447', 2012, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Sobre Fotografia', 'Susan Sontag', '9788535911735', 1977, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Olho do Fotógrafo', 'Michael Freeman', '9788577805913', 2007, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Roube como um Artista', 'Austin Kleon', '9788579801442', 2012, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Psicologia das Cores', 'Eva Heller', '9788584520273', 2012, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('O Livro da Arte', 'Vários Autores', '9788525063419', 2016, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Da Vinci', 'Walter Isaacson', '9788551002575', 2017, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('A Câmera de Pandora', 'Joan Fontcuberta', '9788584520099', 2010, 1, '/images/capa-placeholder.png', 10, 9);
INSERT INTO tb_livro (titulo, autor, isbn, ano_publicacao, edicao, capa_url, categoria_id, estante_id) VALUES ('Obras-Primas do MASP', 'Vários Autores', '9788537801315', 2009, 1, '/images/capa-placeholder.png', 10, 9);

-- =================================================================================
-- LISTA DE 20 USUÁRIOS
-- =================================================================================
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Ana Clara Souza', 'ana.souza@example.com', '111.222.333-01', 3200.50, '1990-03-15', 1, 'Rua das Flores, 123', 'Uberlândia', 'MG', '38400-100');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Bruno Costa', 'bruno.costa@example.com', '222.333.444-02', 4500.00, '1985-07-22', 2, 'Avenida Brasil, 456', 'Araguari', 'MG', '38440-200');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Carla Dias', 'carla.dias@example.com', '333.444.555-03', 2800.75, '1995-11-30', 0, 'Praça da Matriz, 789', 'Ituiutaba', 'MG', '38300-300');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Daniel Martins', 'daniel.martins@example.com', '444.555.666-04', 5500.00, '1982-01-10', 3, 'Rua dos Coqueiros, 101', 'Uberaba', 'MG', '38010-400');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Eduarda Ferreira', 'eduarda.ferreira@example.com', '555.666.777-05', 6100.20, '1998-09-05', 0, 'Avenida Getúlio Vargas, 202', 'Patos de Minas', 'MG', '38700-500');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Fábio Gonçalves', 'fabio.goncalves@example.com', '666.777.888-06', 3900.00, '1993-06-18', 1, 'Rua Sete de Setembro, 303', 'Monte Carmelo', 'MG', '38500-600');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Gabriela Lima', 'gabriela.lima@example.com', '777.888.999-07', 7200.80, '1980-04-25', 2, 'Travessa da Liberdade, 404', 'Patrocínio', 'MG', '38740-700');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Heitor Pereira', 'heitor.pereira@example.com', '888.999.000-08', 4100.00, '2000-02-20', 0, 'Rua das Orquídeas, 505', 'Coromandel', 'MG', '38550-800');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Isabela Ribeiro', 'isabela.ribeiro@example.com', '999.000.111-09', 3500.90, '1997-08-12', 1, 'Avenida dos Bandeirantes, 606', 'Tupaciguara', 'MG', '38480-900');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('João Santos', 'joao.santos@example.com', '000.111.222-10', 8000.00, '1975-12-01', 2, 'Rua do Comércio, 707', 'Centralina', 'MG', '38390-110');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Larissa Almeida', 'larissa.almeida@example.com', '112.223.334-11', 2950.00, '2002-05-28', 0, 'Rua da Paz, 808', 'Canápolis', 'MG', '38380-120');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Marcos Oliveira', 'marcos.oliveira@example.com', '223.334.445-12', 5100.50, '1988-10-03', 1, 'Avenida das Acácias, 909', 'Capinópolis', 'MG', '38360-130');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Natália Rodrigues', 'natalia.rodrigues@example.com', '334.445.556-13', 4800.00, '1991-07-14', 2, 'Rua dos Pinheiros, 111', 'Goiânia', 'GO', '74000-140');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Otávio Barbosa', 'otavio.barbosa@example.com', '445.556.667-14', 3300.25, '1994-04-09', 0, 'Avenida Anhanguera, 222', 'Anápolis', 'GO', '75000-150');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Patrícia Azevedo', 'patricia.azevedo@example.com', '556.667.778-15', 6500.00, '1986-02-17', 1, 'Rua 24 de Outubro, 333', 'Rio Verde', 'GO', '75900-160');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Rafael Castro', 'rafael.castro@example.com', '667.778.889-16', 4200.60, '1999-01-21', 0, 'Avenida Castelo Branco, 444', 'Jataí', 'GO', '75800-170');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Sofia Mendes', 'sofia.mendes@example.com', '778.889.990-17', 5800.00, '1983-08-08', 2, 'Rua das Gaivotas, 555', 'Catalão', 'GO', '75700-180');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Thiago Nogueira', 'thiago.nogueira@example.com', '889.990.001-18', 3750.00, '1996-06-26', 1, 'Avenida Goiás, 666', 'Itumbiara', 'GO', '75500-190');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('Vanessa Rocha', 'vanessa.rocha@example.com', '990.001.112-19', 7500.40, '1989-09-19', 0, 'Rua da Consolação, 777', 'São Paulo', 'SP', '01301-200');
INSERT INTO tb_usuario (name, email, cpf, income, birth_date, children_count, street, city, state, zip_code) VALUES ('William Teixeira', 'william.teixeira@example.com', '001.112.223-20', 9200.00, '1981-05-05', 3, 'Avenida Paulista, 888', 'São Paulo', 'SP', '01310-300');