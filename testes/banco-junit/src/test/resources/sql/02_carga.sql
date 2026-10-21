USE marketplace_db;

INSERT INTO tb_user(user,password,email)
VALUES
('daniel.santos','senha001','daniel.santos@email.com'),
('fernando.araujo','senha002','fernando.araujo@email.com'),
('larissa.cardoso','senha003','larissa.cardoso@email.com'),
('gustavo.cavalcanti','senha004','gustavo.cavalcanti@email.com'),
('paulo.rodrigues','senha005','paulo.rodrigues@email.com'),
('thiago.gomes','senha006','thiago.gomes@email.com'),
('debora.correia','senha007','debora.correia@email.com'),
('helena.oliveira','senha008','helena.oliveira@email.com'),
('eduardo.lima','senha009','eduardo.lima@email.com'),
('guilherme.barbosa','senha010','guilherme.barbosa@email.com'),
('isabela.campos','senha011','isabela.campos@email.com'),
('marcos.costa','senha012','marcos.costa@email.com'),
('yasmin.carvalho','senha013','yasmin.carvalho@email.com'),
('sergio.moreira','senha014','sergio.moreira@email.com'),
('rodrigo.souza','senha015','rodrigo.souza@email.com'),
('gabriela.nascimento','senha016','gabriela.nascimento@email.com'),
('beatriz.rocha','senha017','beatriz.rocha@email.com'),
('natalia.castro','senha018','natalia.castro@email.com'),
('patricia.pereira','senha019','patricia.pereira@email.com'),
('elaine.ribeiro','senha020','elaine.ribeiro@email.com'),
('vinicius.teixeira','senha021','vinicius.teixeira@email.com'),
('ana.silva','senha022','ana.silva@email.com'),
('alexandre.almeida','senha023','alexandre.almeida@email.com'),
('ricardo.martins','senha024','ricardo.martins@email.com'),
('fernanda.dias','senha025','fernanda.dias@email.com'),
('sabrina.santos','senha026','sabrina.santos@email.com'),
('valter.araujo','senha027','valter.araujo@email.com'),
('renato.cardoso','senha028','renato.cardoso@email.com'),
('andre.cavalcanti','senha029','andre.cavalcanti@email.com'),
('vanessa.rodrigues','senha030','vanessa.rodrigues@email.com'),
('mariana.gomes','senha031','mariana.gomes@email.com'),
('karina.correia','senha032','karina.correia@email.com'),
('tatiane.oliveira','senha033','tatiane.oliveira@email.com'),
('luis.lima','senha034','luis.lima@email.com'),
('camila.barbosa','senha035','camila.barbosa@email.com'),
('bruno.campos','senha036','bruno.campos@email.com'),
('juliana.costa','senha037','juliana.costa@email.com'),
('rafael.carvalho','senha038','rafael.carvalho@email.com'),
('felipe.moreira','senha039','felipe.moreira@email.com'),
('leonardo.souza','senha040','leonardo.souza@email.com'),
('fabio.nascimento','senha041','fabio.nascimento@email.com'),
('carlos.rocha','senha042','carlos.rocha@email.com'),
('ewerthon.castro','senha043','ewerthon.castro@email.com'),
('diego.pereira','senha044','diego.pereira@email.com'),
('raquel.ribeiro','senha045','raquel.ribeiro@email.com');

-- id_seller INT NOT NULL / CNPJ CHAR(14) NOT NULL (RN03: unico)
INSERT INTO tb_seller(id_seller,CNPJ)
VALUES
(1,'11000000000037'),
(2,'11000000000074'),
(3,'11000000000111'),
(4,'11000000000148'),
(5,'11000000000185'),
(6,'11000000000222'),
(7,'11000000000259'),
(8,'11000000000296'),
(9,'11000000000333'),
(10,'11000000000370'),
(11,'11000000000407'),
(12,'11000000000444'),
(13,'11000000000481'),
(14,'11000000000518'),
(15,'11000000000555'),
(41,'11000000001517'),
(42,'11000000001554'),
(43,'11000000001591'),
(44,'11000000001628'),
(45,'11000000001665');

-- phone VARCHAR(20) NULL porque nao e obrigado a colocar o numero (RN20)
INSERT INTO tb_client(id_client,phone)
VALUES
(16,NULL),
(17,'619900177'),
(18,'619900188'),
(19,'619900199'),
(20,NULL),
(21,'619900211'),
(22,'619900222'),
(23,'619900233'),
(24,NULL),
(25,'619900255'),
(26,'619900266'),
(27,'619900277'),
(28,NULL),
(29,'619900299'),
(30,'619900300'),
(31,'619900311'),
(32,NULL),
(33,'619900333'),
(34,'619900344'),
(35,'619900355'),
(36,NULL),
(37,'619900377'),
(38,'619900388'),
(39,'619900399'),
(40,NULL),
(41,'619900411'),
(42,'619900422'),
(43,'619900433'),
(44,NULL),
(45,'619900455');

-- tb_category: 4 categorias raiz + 10 subcategorias (autorrelacionamento, RN05).
INSERT INTO tb_category(category,description,id_category_parent)
VALUES
('Eletronicos','Produtos eletronicos em geral',NULL),
('Casa e Cozinha','Produtos para casa e cozinha',NULL),
('Alimentos','Produtos alimenticios',NULL),
('Vestuario','Roupas, calcados e acessorios',NULL);
-- subcategorias (id_category_parent aponta para a categoria raiz correspondente)
INSERT INTO tb_category(category,description,id_category_parent)
VALUES
('Celulares','Smartphones e celulares',1),
('Notebooks','Notebooks e laptops',1),
('Acessorios de Informatica','Fones, carregadores, cabos',1),
('Utensilios','Utensilios para cozinha',2),
('Moveis','Moveis para casa',2),
('Graos','Arroz, feijao e graos em geral',3),
('Bebidas','Bebidas em geral',3),
('Roupas Masculinas','Vestuario masculino',4),
('Roupas Femininas','Vestuario feminino',4),
('Calcados','Calcados em geral',4);

INSERT INTO tb_supplier(name,CNPJ,phone)
VALUES
('Felipe Distribuidora','20000000000091','619800011'),
('Elias Comercio','20000000000182','619800022'),
('TechImport Ltda','20000000000273','619800033'),
('Casa Boa Suprimentos','20000000000364','619800044'),
('Alimentos Brasil','20000000000455',NULL),
('Bebidas do Vale','20000000000546','619800066'),
('Moda Sul Confeccoes','20000000000637','619800077'),
('Calcados Nordeste','20000000000728','619800088'),
('InfoParts Componentes','20000000000819','619800099'),
('Utilcasa Distribuicao','20000000000910',NULL),
('Grao Dourado Alimentos','20000000001001','619800111'),
('MoveisBem Fabrica','20000000001092','619800122'),
('Acessorios Tech','20000000001183','619800133'),
('VaredoTextil','20000000001274','619800144'),
('Norte Suprimentos','20000000001365',NULL),
('Sabor Distribuidora','20000000001456','619800166');

-- id_product nao e informado (AUTO_INCREMENT).
INSERT INTO tb_product(name,value,quantity,id_seller,id_category)
VALUES
('Celular Samsung Galaxy A54',1949.27,61,4,5),
('Celular Motorola Edge 40',1950.72,51,7,5),
('iPhone 13 Seminovo',2633.96,40,10,5),
('Celular Xiaomi Redmi Note 12',1280.58,49,13,5),
('Notebook Dell Inspiron 15',3781.50,11,41,6),
('Notebook Lenovo IdeaPad 3',2273.32,32,44,6),
('Notebook Acer Aspire 5',3636.91,13,2,6),
('Fone de Ouvido Bluetooth JBL',310.94,15,5,7),
('Carregador Turbo USB-C',52.81,61,8,7),
('Cabo HDMI 2m',34.07,49,11,7),
('Mouse sem Fio Logitech',53.01,48,14,7),
('Teclado Mecanico Gamer',202.38,37,42,7),
('Panela de Pressao 5L',156.15,0,45,8),
('Jogo de Panelas Antiaderente',370.89,12,3,8),
('Liquidificador 3 Velocidades',169.19,24,6,8),
('Faqueiro Inox 24 Pecas',205.46,34,9,8),
('Sofa 3 Lugares Retratil',1461.44,51,12,9),
('Mesa de Jantar 4 Lugares',942.95,74,15,9),
('Estante para Livros',387.85,44,43,9),
('Guarda-Roupa 6 Portas',1827.14,10,1,9),
('Arroz Branco Tipo 1 5kg',23.44,7,4,10),
('Feijao Carioca 1kg',10.83,54,7,10),
('Feijao Preto 1kg',7.61,30,10,10),
('Lentilha 500g',13.39,75,13,10),
('Farinha de Trigo 1kg',8.38,43,41,10),
('Suco de Uva Integral 1L',12.55,0,44,11),
('Refrigerante Cola 2L',10.00,61,2,11),
('Agua Mineral 1,5L (Fardo)',16.86,20,5,11),
('Cafe Torrado e Moido 500g',14.96,74,8,11),
('Camisa Polo Masculina',103.12,77,11,12),
('Calca Jeans Masculina',137.13,77,14,12),
('Bermuda Tactel Masculina',59.97,31,42,12),
('Jaqueta Corta-Vento Masculina',259.63,20,45,12),
('Vestido Estampado Feminino',141.14,14,3,13),
('Blusa de Trico Feminina',128.02,17,6,13),
('Calca Legging Feminina',54.93,23,9,13),
('Saia Jeans Feminina',114.41,57,12,13),
('Tenis Esportivo Unissex',257.17,52,15,14),
('Sandalia Feminina',84.35,0,43,14),
('Sapato Social Masculino',319.34,70,1,14),
('Chinelo de Dedo',28.80,73,4,14),
('Celular Samsung Galaxy A54 Premium',2019.01,4,7,5),
('Celular Motorola Edge 40 Basico',2152.40,17,10,5),
('iPhone 13 Seminovo Edicao Especial',3658.91,71,13,5),
('Celular Xiaomi Redmi Note 12 Compacto',1225.61,46,41,5),
('Notebook Dell Inspiron 15 Plus',2989.64,58,44,6),
('Notebook Lenovo IdeaPad 3 Slim',2453.05,3,2,6),
('Notebook Acer Aspire 5 Pro',3926.11,36,5,6),
('Fone de Ouvido Bluetooth JBL Lite',342.41,25,8,7),
('Carregador Turbo USB-C Premium',60.46,16,11,7),
('Cabo HDMI 2m Basico',41.12,41,14,7),
('Mouse sem Fio Logitech Edicao Especial',107.33,0,42,7),
('Teclado Mecanico Gamer Compacto',276.92,28,45,7),
('Panela de Pressao 5L Plus',112.23,23,3,8),
('Jogo de Panelas Antiaderente Slim',334.84,70,6,8);

-- tb_product_supplier: vinculos produto-fornecedor (RN18: produto pode ter mais de um
-- fornecedor). Produtos com id multiplo de 11 ficam propositalmente sem fornecedor,
-- e o fornecedor 16 fica propositalmente sem nenhum produto vinculado (casos de contorno).
INSERT INTO tb_product_supplier(id_product,id_supplier,delivery_time)
VALUES
(1,6,2),
(2,11,11),
(3,1,7),
(4,6,9),
(5,11,2),
(6,1,3),
(7,6,7),
(7,9,15),
(8,11,14),
(9,1,6),
(10,6,5),
(12,1,2),
(13,6,5),
(14,11,11),
(14,14,3),
(15,1,3),
(16,6,13),
(17,11,9),
(18,1,15),
(19,6,3),
(20,11,14),
(21,1,10),
(21,4,14),
(23,11,4),
(24,1,4),
(25,6,12),
(26,11,9),
(27,1,10),
(28,6,4),
(28,9,6),
(29,11,10),
(30,1,15),
(31,6,11),
(32,11,8),
(34,6,5),
(35,11,10),
(35,14,14),
(36,1,13),
(37,6,13),
(38,11,5),
(39,1,13),
(40,6,6),
(41,11,8),
(42,1,12),
(42,4,12),
(43,6,7),
(45,1,9),
(46,6,10),
(47,11,9),
(48,1,3),
(49,6,5),
(49,9,5),
(50,11,3),
(51,1,7),
(52,6,2),
(53,11,11),
(54,1,10);

-- tb_cart: 45 carrinhos. Os clientes de indice par (a cada 3) possuem um segundo
-- carrinho (RN07: cliente pode ter mais de um carrinho). O ultimo carrinho fica vazio
-- de proposito (caso de contorno: carrinho em andamento, ainda sem itens).
INSERT INTO tb_cart(totalvalue,itemquantity,id_client)
VALUES
(0.00,0,16),
(0.00,0,17),
(0.00,0,18),
(0.00,0,19),
(0.00,0,20),
(0.00,0,21),
(0.00,0,22),
(0.00,0,23),
(0.00,0,24),
(0.00,0,25),
(0.00,0,26),
(0.00,0,27),
(0.00,0,28),
(0.00,0,29),
(0.00,0,30),
(0.00,0,31),
(0.00,0,32),
(0.00,0,33),
(0.00,0,34),
(0.00,0,35),
(0.00,0,36),
(0.00,0,37),
(0.00,0,38),
(0.00,0,39),
(0.00,0,40),
(0.00,0,41),
(0.00,0,42),
(0.00,0,43),
(0.00,0,44),
(0.00,0,45),
(0.00,0,16),
(0.00,0,17),
(0.00,0,18),
(0.00,0,19),
(0.00,0,20),
(0.00,0,21),
(0.00,0,22),
(0.00,0,23),
(0.00,0,24),
(0.00,0,25),
(0.00,0,26),
(0.00,0,27),
(0.00,0,28),
(0.00,0,29),
(0.00,0,16);

-- tb_buyItem: itens de carrinho. unit_value copiado do preco vigente do produto no
-- momento da inclusao (RN09). O ultimo carrinho (vazio) nao recebe nenhum item de
-- proposito.
INSERT INTO tb_buyItem(id_product,id_cart,quantity,unit_value)
VALUES
(8,1,1,310.94),
(21,1,3,23.44),
(15,2,1,169.19),
(28,2,1,16.86),
(22,3,1,10.83),
(29,4,3,14.96),
(42,4,3,2019.01),
(36,5,1,54.93),
(49,5,1,342.41),
(43,6,1,2152.40),
(50,7,1,60.46),
(8,7,2,310.94),
(2,8,1,1950.72),
(15,8,3,169.19),
(9,9,1,52.81),
(16,10,2,205.46),
(29,10,3,14.96),
(23,11,2,7.61),
(36,11,1,54.93),
(30,12,3,103.12),
(37,13,1,114.41),
(50,13,3,60.46),
(44,14,3,3658.91),
(2,14,3,1950.72),
(51,15,2,41.12),
(3,16,1,2633.96),
(16,16,2,205.46),
(10,17,2,34.07),
(23,17,1,7.61),
(17,18,1,1461.44),
(24,19,1,13.39),
(37,19,3,114.41),
(31,20,2,137.13),
(44,20,2,3658.91),
(38,21,2,257.17),
(45,22,2,1225.61),
(3,22,2,2633.96),
(52,23,3,107.33),
(10,23,1,34.07),
(4,24,3,1280.58),
(11,25,3,53.01),
(24,25,3,13.39),
(18,26,1,942.95),
(31,26,1,137.13),
(25,27,2,8.38),
(32,28,3,59.97),
(45,28,2,1225.61),
(39,29,1,84.35),
(52,29,1,107.33),
(46,30,1,2989.64),
(53,31,1,276.92),
(11,31,3,53.01),
(5,32,2,3781.50),
(18,32,1,942.95),
(12,33,2,202.38),
(19,34,1,387.85),
(32,34,2,59.97),
(26,35,2,12.55),
(39,35,1,84.35),
(33,36,1,259.63),
(40,37,2,319.34),
(53,37,3,276.92),
(47,38,1,2453.05),
(5,38,1,3781.50),
(54,39,3,112.23),
(6,40,3,2273.32),
(19,40,1,387.85),
(13,41,1,156.15),
(26,41,1,12.55),
(20,42,1,1827.14),
(27,43,2,10.00),
(40,43,2,319.34),
(34,44,2,141.14),
(47,44,1,2453.05);

-- Ajuste dos totais derivados de tb_cart a partir dos itens inseridos (mantido por
-- aplicacao/consulta, conforme A4 e A5 - tb_cart.totalvalue/itemquantity sao
-- atributos derivados).
UPDATE tb_cart SET totalvalue=381.26, itemquantity=4 WHERE id_cart=1;
UPDATE tb_cart SET totalvalue=186.05, itemquantity=2 WHERE id_cart=2;
UPDATE tb_cart SET totalvalue=10.83, itemquantity=1 WHERE id_cart=3;
UPDATE tb_cart SET totalvalue=6101.91, itemquantity=6 WHERE id_cart=4;
UPDATE tb_cart SET totalvalue=397.34, itemquantity=2 WHERE id_cart=5;
UPDATE tb_cart SET totalvalue=2152.40, itemquantity=1 WHERE id_cart=6;
UPDATE tb_cart SET totalvalue=682.34, itemquantity=3 WHERE id_cart=7;
UPDATE tb_cart SET totalvalue=2458.29, itemquantity=4 WHERE id_cart=8;
UPDATE tb_cart SET totalvalue=52.81, itemquantity=1 WHERE id_cart=9;
UPDATE tb_cart SET totalvalue=455.80, itemquantity=5 WHERE id_cart=10;
UPDATE tb_cart SET totalvalue=70.15, itemquantity=3 WHERE id_cart=11;
UPDATE tb_cart SET totalvalue=309.36, itemquantity=3 WHERE id_cart=12;
UPDATE tb_cart SET totalvalue=295.79, itemquantity=4 WHERE id_cart=13;
UPDATE tb_cart SET totalvalue=16828.89, itemquantity=6 WHERE id_cart=14;
UPDATE tb_cart SET totalvalue=82.24, itemquantity=2 WHERE id_cart=15;
UPDATE tb_cart SET totalvalue=3044.88, itemquantity=3 WHERE id_cart=16;
UPDATE tb_cart SET totalvalue=75.75, itemquantity=3 WHERE id_cart=17;
UPDATE tb_cart SET totalvalue=1461.44, itemquantity=1 WHERE id_cart=18;
UPDATE tb_cart SET totalvalue=356.62, itemquantity=4 WHERE id_cart=19;
UPDATE tb_cart SET totalvalue=7592.08, itemquantity=4 WHERE id_cart=20;
UPDATE tb_cart SET totalvalue=514.34, itemquantity=2 WHERE id_cart=21;
UPDATE tb_cart SET totalvalue=7719.14, itemquantity=4 WHERE id_cart=22;
UPDATE tb_cart SET totalvalue=356.06, itemquantity=4 WHERE id_cart=23;
UPDATE tb_cart SET totalvalue=3841.74, itemquantity=3 WHERE id_cart=24;
UPDATE tb_cart SET totalvalue=199.20, itemquantity=6 WHERE id_cart=25;
UPDATE tb_cart SET totalvalue=1080.08, itemquantity=2 WHERE id_cart=26;
UPDATE tb_cart SET totalvalue=16.76, itemquantity=2 WHERE id_cart=27;
UPDATE tb_cart SET totalvalue=2631.13, itemquantity=5 WHERE id_cart=28;
UPDATE tb_cart SET totalvalue=191.68, itemquantity=2 WHERE id_cart=29;
UPDATE tb_cart SET totalvalue=2989.64, itemquantity=1 WHERE id_cart=30;
UPDATE tb_cart SET totalvalue=435.95, itemquantity=4 WHERE id_cart=31;
UPDATE tb_cart SET totalvalue=8505.95, itemquantity=3 WHERE id_cart=32;
UPDATE tb_cart SET totalvalue=404.76, itemquantity=2 WHERE id_cart=33;
UPDATE tb_cart SET totalvalue=507.79, itemquantity=3 WHERE id_cart=34;
UPDATE tb_cart SET totalvalue=109.45, itemquantity=3 WHERE id_cart=35;
UPDATE tb_cart SET totalvalue=259.63, itemquantity=1 WHERE id_cart=36;
UPDATE tb_cart SET totalvalue=1469.44, itemquantity=5 WHERE id_cart=37;
UPDATE tb_cart SET totalvalue=6234.55, itemquantity=2 WHERE id_cart=38;
UPDATE tb_cart SET totalvalue=336.69, itemquantity=3 WHERE id_cart=39;
UPDATE tb_cart SET totalvalue=7207.81, itemquantity=4 WHERE id_cart=40;
UPDATE tb_cart SET totalvalue=168.70, itemquantity=2 WHERE id_cart=41;
UPDATE tb_cart SET totalvalue=1827.14, itemquantity=1 WHERE id_cart=42;
UPDATE tb_cart SET totalvalue=658.68, itemquantity=4 WHERE id_cart=43;
UPDATE tb_cart SET totalvalue=2735.33, itemquantity=3 WHERE id_cart=44;

-- tb_order: 50 pedidos, distribuidos entre os clientes.
INSERT INTO tb_order(date_order,totalvalue,id_client)
VALUES
('2026-06-03 10:00:00',0.00,21),
('2026-06-05 11:00:00',0.00,26),
('2026-06-07 12:00:00',0.00,31),
('2026-06-09 13:00:00',0.00,36),
('2026-06-11 14:00:00',0.00,41),
('2026-06-13 15:00:00',0.00,16),
('2026-06-15 16:00:00',0.00,21),
('2026-06-17 09:00:00',0.00,26),
('2026-06-19 10:00:00',0.00,31),
('2026-06-21 11:00:00',0.00,36),
('2026-06-23 12:00:00',0.00,41),
('2026-06-25 13:00:00',0.00,16),
('2026-06-27 14:00:00',0.00,21),
('2026-06-29 15:00:00',0.00,26),
('2026-07-01 16:00:00',0.00,31),
('2026-07-03 09:00:00',0.00,36),
('2026-07-05 10:00:00',0.00,41),
('2026-07-07 11:00:00',0.00,16),
('2026-07-09 12:00:00',0.00,21),
('2026-07-11 13:00:00',0.00,26),
('2026-07-13 14:00:00',0.00,31),
('2026-07-15 15:00:00',0.00,36),
('2026-07-17 16:00:00',0.00,41),
('2026-07-19 09:00:00',0.00,16),
('2026-07-21 10:00:00',0.00,21),
('2026-07-23 11:00:00',0.00,26),
('2026-07-25 12:00:00',0.00,31),
('2026-07-27 13:00:00',0.00,36),
('2026-07-29 14:00:00',0.00,41),
('2026-07-31 15:00:00',0.00,16),
('2026-08-02 16:00:00',0.00,21),
('2026-08-04 09:00:00',0.00,26),
('2026-08-06 10:00:00',0.00,31),
('2026-08-08 11:00:00',0.00,36),
('2026-08-10 12:00:00',0.00,41),
('2026-08-12 13:00:00',0.00,16),
('2026-08-14 14:00:00',0.00,21),
('2026-08-16 15:00:00',0.00,26),
('2026-08-18 16:00:00',0.00,31),
('2026-08-20 09:00:00',0.00,36),
('2026-08-22 10:00:00',0.00,41),
('2026-08-24 11:00:00',0.00,16),
('2026-08-26 12:00:00',0.00,21),
('2026-08-28 13:00:00',0.00,26),
('2026-08-30 14:00:00',0.00,31),
('2026-09-01 15:00:00',0.00,36),
('2026-09-03 16:00:00',0.00,41),
('2026-09-05 09:00:00',0.00,16),
('2026-09-07 10:00:00',0.00,21),
('2026-09-09 11:00:00',0.00,26);

-- tb_orderItem: itens de pedido. Cada pedido recebe de 1 a 4 itens, totalizando mais
-- de 100 linhas (tabela de maior movimento do projeto). unit_value preservado do
-- momento da compra (RN11).
INSERT INTO tb_orderItem(id_order,id_product,quantity,unit_value)
VALUES
(1,12,4,202.38),
(1,29,1,14.96),
(1,46,2,2989.64),
(2,23,4,7.61),
(2,40,1,319.34),
(2,2,4,1950.72),
(2,19,3,387.85),
(3,34,4,141.14),
(3,51,3,41.12),
(4,45,4,1225.61),
(4,7,4,3636.91),
(4,24,2,13.39),
(5,1,2,1949.27),
(5,18,3,942.95),
(5,35,2,128.02),
(5,52,1,107.33),
(6,12,1,202.38),
(6,29,3,14.96),
(7,23,1,7.61),
(7,40,1,319.34),
(7,2,4,1950.72),
(8,34,2,141.14),
(8,51,1,41.12),
(8,13,1,156.15),
(8,30,2,103.12),
(9,45,1,1225.61),
(9,7,1,3636.91),
(10,1,2,1949.27),
(10,18,4,942.95),
(10,35,1,128.02),
(11,12,2,202.38),
(11,29,1,14.96),
(11,46,1,2989.64),
(11,8,4,310.94),
(12,23,3,7.61),
(12,40,3,319.34),
(13,34,2,141.14),
(13,51,3,41.12),
(13,13,2,156.15),
(14,45,3,1225.61),
(14,7,4,3636.91),
(14,24,2,13.39),
(14,41,3,28.80),
(15,1,4,1949.27),
(15,18,3,942.95),
(16,12,1,202.38),
(16,29,1,14.96),
(16,46,4,2989.64),
(17,23,1,7.61),
(17,40,1,319.34),
(17,2,2,1950.72),
(17,19,3,387.85),
(18,34,2,141.14),
(18,51,3,41.12),
(19,45,1,1225.61),
(19,7,2,3636.91),
(19,24,3,13.39),
(20,1,3,1949.27),
(20,18,2,942.95),
(20,35,4,128.02),
(20,52,3,107.33),
(21,12,1,202.38),
(21,29,3,14.96),
(22,23,1,7.61),
(22,40,2,319.34),
(22,2,3,1950.72),
(23,34,1,141.14),
(23,51,1,41.12),
(23,13,2,156.15),
(23,30,3,103.12),
(24,45,3,1225.61),
(24,7,2,3636.91),
(25,1,3,1949.27),
(25,18,2,942.95),
(25,35,3,128.02),
(26,12,4,202.38),
(26,29,3,14.96),
(26,46,1,2989.64),
(26,8,1,310.94),
(27,23,4,7.61),
(27,40,3,319.34),
(28,34,1,141.14),
(28,51,1,41.12),
(28,13,3,156.15),
(29,45,2,1225.61),
(29,7,3,3636.91),
(29,24,2,13.39),
(29,41,4,28.80),
(30,1,4,1949.27),
(30,18,1,942.95),
(31,12,1,202.38),
(31,29,1,14.96),
(31,46,2,2989.64),
(32,23,1,7.61),
(32,40,3,319.34),
(32,2,2,1950.72),
(32,19,4,387.85),
(33,34,2,141.14),
(33,51,1,41.12),
(34,45,3,1225.61),
(34,7,3,3636.91),
(34,24,1,13.39),
(35,1,3,1949.27),
(35,18,2,942.95),
(35,35,2,128.02),
(35,52,1,107.33),
(36,12,3,202.38),
(36,29,4,14.96),
(37,23,2,7.61),
(37,40,2,319.34),
(37,2,2,1950.72),
(38,34,2,141.14),
(38,51,4,41.12),
(38,13,1,156.15),
(38,30,2,103.12),
(39,45,3,1225.61),
(39,7,4,3636.91),
(40,1,2,1949.27),
(40,18,3,942.95),
(40,35,2,128.02),
(41,12,1,202.38),
(41,29,4,14.96),
(41,46,1,2989.64),
(41,8,4,310.94),
(42,23,2,7.61),
(42,40,2,319.34),
(43,34,4,141.14),
(43,51,3,41.12),
(43,13,3,156.15),
(44,45,2,1225.61),
(44,7,2,3636.91),
(44,24,1,13.39),
(44,41,2,28.80),
(45,1,4,1949.27),
(45,18,3,942.95),
(46,12,3,202.38),
(46,29,1,14.96),
(46,46,3,2989.64),
(47,23,3,7.61),
(47,40,4,319.34),
(47,2,3,1950.72),
(47,19,1,387.85),
(48,34,1,141.14),
(48,51,3,41.12),
(49,45,2,1225.61),
(49,7,3,3636.91),
(49,24,1,13.39),
(50,1,1,1949.27),
(50,18,4,942.95),
(50,35,3,128.02),
(50,52,3,107.33);
-- Total de linhas em tb_orderItem nesta carga: 151

-- Ajuste do total derivado de tb_order a partir dos itens inseridos (RN13: totalvalue
-- deve bater com a soma de quantity*unit_value de tb_orderItem).
UPDATE tb_order SET totalvalue=6803.76 WHERE id_order=1;
UPDATE tb_order SET totalvalue=9316.21 WHERE id_order=2;
UPDATE tb_order SET totalvalue=687.92 WHERE id_order=3;
UPDATE tb_order SET totalvalue=19476.86 WHERE id_order=4;
UPDATE tb_order SET totalvalue=7090.76 WHERE id_order=5;
UPDATE tb_order SET totalvalue=247.26 WHERE id_order=6;
UPDATE tb_order SET totalvalue=8129.83 WHERE id_order=7;
UPDATE tb_order SET totalvalue=685.79 WHERE id_order=8;
UPDATE tb_order SET totalvalue=4862.52 WHERE id_order=9;
UPDATE tb_order SET totalvalue=7798.36 WHERE id_order=10;
UPDATE tb_order SET totalvalue=4653.12 WHERE id_order=11;
UPDATE tb_order SET totalvalue=980.85 WHERE id_order=12;
UPDATE tb_order SET totalvalue=717.94 WHERE id_order=13;
UPDATE tb_order SET totalvalue=18337.65 WHERE id_order=14;
UPDATE tb_order SET totalvalue=10625.93 WHERE id_order=15;
UPDATE tb_order SET totalvalue=12175.90 WHERE id_order=16;
UPDATE tb_order SET totalvalue=5391.94 WHERE id_order=17;
UPDATE tb_order SET totalvalue=405.64 WHERE id_order=18;
UPDATE tb_order SET totalvalue=8539.60 WHERE id_order=19;
UPDATE tb_order SET totalvalue=8567.78 WHERE id_order=20;
UPDATE tb_order SET totalvalue=247.26 WHERE id_order=21;
UPDATE tb_order SET totalvalue=6498.45 WHERE id_order=22;
UPDATE tb_order SET totalvalue=803.92 WHERE id_order=23;
UPDATE tb_order SET totalvalue=10950.65 WHERE id_order=24;
UPDATE tb_order SET totalvalue=8117.77 WHERE id_order=25;
UPDATE tb_order SET totalvalue=4154.98 WHERE id_order=26;
UPDATE tb_order SET totalvalue=988.46 WHERE id_order=27;
UPDATE tb_order SET totalvalue=650.71 WHERE id_order=28;
UPDATE tb_order SET totalvalue=13503.93 WHERE id_order=29;
UPDATE tb_order SET totalvalue=8740.03 WHERE id_order=30;
UPDATE tb_order SET totalvalue=6196.62 WHERE id_order=31;
UPDATE tb_order SET totalvalue=6418.47 WHERE id_order=32;
UPDATE tb_order SET totalvalue=323.40 WHERE id_order=33;
UPDATE tb_order SET totalvalue=14600.95 WHERE id_order=34;
UPDATE tb_order SET totalvalue=8097.08 WHERE id_order=35;
UPDATE tb_order SET totalvalue=666.98 WHERE id_order=36;
UPDATE tb_order SET totalvalue=4555.34 WHERE id_order=37;
UPDATE tb_order SET totalvalue=809.15 WHERE id_order=38;
UPDATE tb_order SET totalvalue=18224.47 WHERE id_order=39;
UPDATE tb_order SET totalvalue=6983.43 WHERE id_order=40;
UPDATE tb_order SET totalvalue=4495.62 WHERE id_order=41;
UPDATE tb_order SET totalvalue=653.90 WHERE id_order=42;
UPDATE tb_order SET totalvalue=1156.37 WHERE id_order=43;
UPDATE tb_order SET totalvalue=9796.03 WHERE id_order=44;
UPDATE tb_order SET totalvalue=10625.93 WHERE id_order=45;
UPDATE tb_order SET totalvalue=9591.02 WHERE id_order=46;
UPDATE tb_order SET totalvalue=7540.20 WHERE id_order=47;
UPDATE tb_order SET totalvalue=264.50 WHERE id_order=48;
UPDATE tb_order SET totalvalue=13375.34 WHERE id_order=49;
UPDATE tb_order SET totalvalue=6427.12 WHERE id_order=50;

-- tb_order_status: historico de status por pedido (entidade fraca, RN14/RN15).
-- A maioria dos pedidos percorre o fluxo completo (aberto->pago->enviado->entregue).
-- Casos de contorno propositais: alguns pedidos ficam 'em aberto' (nunca avancam) e
-- outros sao 'cancelado' logo apos o registro inicial.
INSERT INTO tb_order_status(id_order,date_status,status)
VALUES
(1,'2026-06-03 10:00:00','aberto'),
(1,'2026-06-03 13:00:00','pago'),
(1,'2026-06-04 06:00:00','enviado'),
(1,'2026-06-05 10:00:00','entregue'),
(2,'2026-06-05 11:00:00','aberto'),
(2,'2026-06-05 14:00:00','pago'),
(2,'2026-06-06 07:00:00','enviado'),
(2,'2026-06-07 11:00:00','entregue'),
(3,'2026-06-07 12:00:00','aberto'),
(3,'2026-06-07 15:00:00','pago'),
(3,'2026-06-08 08:00:00','enviado'),
(3,'2026-06-09 12:00:00','entregue'),
(4,'2026-06-09 13:00:00','aberto'),
(4,'2026-06-09 16:00:00','pago'),
(4,'2026-06-10 09:00:00','enviado'),
(4,'2026-06-11 13:00:00','entregue'),
(5,'2026-06-11 14:00:00','aberto'),
(5,'2026-06-11 17:00:00','pago'),
(6,'2026-06-13 15:00:00','aberto'),
(6,'2026-06-13 18:00:00','pago'),
(6,'2026-06-14 11:00:00','enviado'),
(6,'2026-06-15 15:00:00','entregue'),
(7,'2026-06-15 16:00:00','aberto'),
(7,'2026-06-15 19:00:00','pago'),
(7,'2026-06-16 12:00:00','enviado'),
(7,'2026-06-17 16:00:00','entregue'),
(8,'2026-06-17 09:00:00','aberto'),
(8,'2026-06-17 12:00:00','pago'),
(8,'2026-06-18 05:00:00','enviado'),
(8,'2026-06-19 09:00:00','entregue'),
(9,'2026-06-19 10:00:00','aberto'),
(10,'2026-06-21 11:00:00','aberto'),
(10,'2026-06-21 14:00:00','pago'),
(11,'2026-06-23 12:00:00','aberto'),
(11,'2026-06-23 15:00:00','pago'),
(11,'2026-06-24 08:00:00','enviado'),
(11,'2026-06-25 12:00:00','entregue'),
(12,'2026-06-25 13:00:00','aberto'),
(12,'2026-06-25 16:00:00','pago'),
(12,'2026-06-26 09:00:00','enviado'),
(12,'2026-06-27 13:00:00','entregue'),
(13,'2026-06-27 14:00:00','aberto'),
(13,'2026-06-27 17:00:00','pago'),
(13,'2026-06-28 10:00:00','enviado'),
(13,'2026-06-29 14:00:00','entregue'),
(14,'2026-06-29 15:00:00','aberto'),
(14,'2026-06-29 18:00:00','pago'),
(14,'2026-06-30 11:00:00','enviado'),
(14,'2026-07-01 15:00:00','entregue'),
(15,'2026-07-01 16:00:00','aberto'),
(15,'2026-07-01 19:00:00','pago'),
(16,'2026-07-03 09:00:00','aberto'),
(16,'2026-07-03 12:00:00','pago'),
(16,'2026-07-04 05:00:00','enviado'),
(16,'2026-07-05 09:00:00','entregue'),
(17,'2026-07-05 10:00:00','aberto'),
(17,'2026-07-05 12:00:00','cancelado'),
(18,'2026-07-07 11:00:00','aberto'),
(19,'2026-07-09 12:00:00','aberto'),
(19,'2026-07-09 15:00:00','pago'),
(19,'2026-07-10 08:00:00','enviado'),
(19,'2026-07-11 12:00:00','entregue'),
(20,'2026-07-11 13:00:00','aberto'),
(20,'2026-07-11 16:00:00','pago'),
(21,'2026-07-13 14:00:00','aberto'),
(21,'2026-07-13 17:00:00','pago'),
(21,'2026-07-14 10:00:00','enviado'),
(21,'2026-07-15 14:00:00','entregue'),
(22,'2026-07-15 15:00:00','aberto'),
(22,'2026-07-15 18:00:00','pago'),
(22,'2026-07-16 11:00:00','enviado'),
(22,'2026-07-17 15:00:00','entregue'),
(23,'2026-07-17 16:00:00','aberto'),
(23,'2026-07-17 19:00:00','pago'),
(23,'2026-07-18 12:00:00','enviado'),
(23,'2026-07-19 16:00:00','entregue'),
(24,'2026-07-19 09:00:00','aberto'),
(24,'2026-07-19 12:00:00','pago'),
(24,'2026-07-20 05:00:00','enviado'),
(24,'2026-07-21 09:00:00','entregue'),
(25,'2026-07-21 10:00:00','aberto'),
(25,'2026-07-21 13:00:00','pago'),
(26,'2026-07-23 11:00:00','aberto'),
(26,'2026-07-23 14:00:00','pago'),
(26,'2026-07-24 07:00:00','enviado'),
(26,'2026-07-25 11:00:00','entregue'),
(27,'2026-07-25 12:00:00','aberto'),
(28,'2026-07-27 13:00:00','aberto'),
(28,'2026-07-27 16:00:00','pago'),
(28,'2026-07-28 09:00:00','enviado'),
(28,'2026-07-29 13:00:00','entregue'),
(29,'2026-07-29 14:00:00','aberto'),
(29,'2026-07-29 17:00:00','pago'),
(29,'2026-07-30 10:00:00','enviado'),
(29,'2026-07-31 14:00:00','entregue'),
(30,'2026-07-31 15:00:00','aberto'),
(30,'2026-07-31 18:00:00','pago'),
(31,'2026-08-02 16:00:00','aberto'),
(31,'2026-08-02 19:00:00','pago'),
(31,'2026-08-03 12:00:00','enviado'),
(31,'2026-08-04 16:00:00','entregue'),
(32,'2026-08-04 09:00:00','aberto'),
(32,'2026-08-04 12:00:00','pago'),
(32,'2026-08-05 05:00:00','enviado'),
(32,'2026-08-06 09:00:00','entregue'),
(33,'2026-08-06 10:00:00','aberto'),
(33,'2026-08-06 13:00:00','pago'),
(33,'2026-08-07 06:00:00','enviado'),
(33,'2026-08-08 10:00:00','entregue'),
(34,'2026-08-08 11:00:00','aberto'),
(34,'2026-08-08 13:00:00','cancelado'),
(35,'2026-08-10 12:00:00','aberto'),
(35,'2026-08-10 15:00:00','pago'),
(36,'2026-08-12 13:00:00','aberto'),
(37,'2026-08-14 14:00:00','aberto'),
(37,'2026-08-14 17:00:00','pago'),
(37,'2026-08-15 10:00:00','enviado'),
(37,'2026-08-16 14:00:00','entregue'),
(38,'2026-08-16 15:00:00','aberto'),
(38,'2026-08-16 18:00:00','pago'),
(38,'2026-08-17 11:00:00','enviado'),
(38,'2026-08-18 15:00:00','entregue'),
(39,'2026-08-18 16:00:00','aberto'),
(39,'2026-08-18 19:00:00','pago'),
(39,'2026-08-19 12:00:00','enviado'),
(39,'2026-08-20 16:00:00','entregue'),
(40,'2026-08-20 09:00:00','aberto'),
(40,'2026-08-20 12:00:00','pago'),
(41,'2026-08-22 10:00:00','aberto'),
(41,'2026-08-22 13:00:00','pago'),
(41,'2026-08-23 06:00:00','enviado'),
(41,'2026-08-24 10:00:00','entregue'),
(42,'2026-08-24 11:00:00','aberto'),
(42,'2026-08-24 14:00:00','pago'),
(42,'2026-08-25 07:00:00','enviado'),
(42,'2026-08-26 11:00:00','entregue'),
(43,'2026-08-26 12:00:00','aberto'),
(43,'2026-08-26 15:00:00','pago'),
(43,'2026-08-27 08:00:00','enviado'),
(43,'2026-08-28 12:00:00','entregue'),
(44,'2026-08-28 13:00:00','aberto'),
(44,'2026-08-28 16:00:00','pago'),
(44,'2026-08-29 09:00:00','enviado'),
(44,'2026-08-30 13:00:00','entregue'),
(45,'2026-08-30 14:00:00','aberto'),
(46,'2026-09-01 15:00:00','aberto'),
(46,'2026-09-01 18:00:00','pago'),
(46,'2026-09-02 11:00:00','enviado'),
(46,'2026-09-03 15:00:00','entregue'),
(47,'2026-09-03 16:00:00','aberto'),
(47,'2026-09-03 19:00:00','pago'),
(47,'2026-09-04 12:00:00','enviado'),
(47,'2026-09-05 16:00:00','entregue'),
(48,'2026-09-05 09:00:00','aberto'),
(48,'2026-09-05 12:00:00','pago'),
(48,'2026-09-06 05:00:00','enviado'),
(48,'2026-09-07 09:00:00','entregue'),
(49,'2026-09-07 10:00:00','aberto'),
(49,'2026-09-07 13:00:00','pago'),
(49,'2026-09-08 06:00:00','enviado'),
(49,'2026-09-09 10:00:00','entregue'),
(50,'2026-09-09 11:00:00','aberto'),
(50,'2026-09-09 14:00:00','pago');
-- Total de linhas em tb_order_status nesta carga: 163
