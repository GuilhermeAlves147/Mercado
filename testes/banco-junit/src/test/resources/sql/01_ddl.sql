
-- 01_ddl.sql
-- Dominio: Marketplace (usuarios, vendedores, clientes, produtos,
--          categorias, fornecedores, carrinho e pedidos)
--
-- Este script cria o banco e todas as tabelas do zero (executavel em
-- base limpa). Convencoes de nomes de constraint utilizadas:
--   pk_  -> chave primaria
--   uq_  -> chave alternativa (unique)
--   fk_  -> chave estrangeira
--   ck_  -> check constraint
--   idx_ -> indice auxiliar (nao coberto por PK/UNIQUE)
--
-- Cada bloco traz um comentario indicando a(s) regra(s) de negocio
-- (RN01..RN20 que a constraint implementa.

DROP DATABASE IF EXISTS marketplace_db;
CREATE DATABASE marketplace_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE marketplace_db;

-- Garante que nao ha resquicio de execucao anterior, respeitando a
-- ordem inversa de dependencia entre as tabelas.
DROP TABLE IF EXISTS tb_product_supplier;
DROP TABLE IF EXISTS tb_order_status;
DROP TABLE IF EXISTS tb_orderItem;
DROP TABLE IF EXISTS tb_order;
DROP TABLE IF EXISTS tb_buyItem;
DROP TABLE IF EXISTS tb_cart;
DROP TABLE IF EXISTS tb_supplier;
DROP TABLE IF EXISTS tb_product;
DROP TABLE IF EXISTS tb_category;
DROP TABLE IF EXISTS tb_client;
DROP TABLE IF EXISTS tb_seller;
DROP TABLE IF EXISTS tb_user;

-- =====================================================================
-- tb_user  (entidade forte / supertipo da especializacao)
-- =====================================================================
CREATE TABLE tb_user (
    id_user     INT AUTO_INCREMENT,
    user        VARCHAR(50)  NOT NULL,
    password    VARCHAR(255) NOT NULL,  -- hash + sal, nunca texto claro (B5, Etapa 2)
    email       VARCHAR(100) NOT NULL,

    CONSTRAINT pk_user PRIMARY KEY (id_user),
    -- O login e o e-mail não podem se repetir.
    CONSTRAINT uq_user_login UNIQUE (user),
    CONSTRAINT uq_user_email UNIQUE (email)
) ENGINE=InnoDB;

-- =====================================================================
-- tb_seller  (especializacao de tb_user - tabela por subclasse)
-- =====================================================================
CREATE TABLE tb_seller (
    id_seller   INT NOT NULL,
    CNPJ        CHAR(14) NOT NULL,

    CONSTRAINT pk_seller PRIMARY KEY (id_seller),
    -- RN03: o CNPJ de um vendedor e unico no sistema.
    CONSTRAINT uq_seller_cnpj UNIQUE (CNPJ),
    -- O id_seller também é uma chave estrangeira ligada ao usuário.
    --  Se o usuário for excluído, o vendedor relacionado também será excluído.
    CONSTRAINT fk_seller_user FOREIGN KEY (id_seller)
        REFERENCES tb_user (id_user)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- tb_client  (especializacao de tb_user - tabela por subclasse)
-- =====================================================================
CREATE TABLE tb_client (
    id_client   INT NOT NULL,
    phone       VARCHAR(20) NULL,  -- RN20: telefone e opcional

    CONSTRAINT pk_client PRIMARY KEY (id_client),
    CONSTRAINT fk_client_user FOREIGN KEY (id_client)
        REFERENCES tb_user (id_user)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- tb_category  (autorrelacionamento - hierarquia de categorias)
-- =====================================================================
CREATE TABLE tb_category (
    id_category         INT AUTO_INCREMENT,
    category             VARCHAR(100) NOT NULL,
    description           VARCHAR(255) NULL,
    id_category_parent   INT NULL,  -- NULL = categoria raiz

    CONSTRAINT pk_category PRIMARY KEY (id_category),
    -- RN05: uma categoria pode possuir subcategorias.
-- Não é possível excluir uma categoria que ainda possui subcategorias ligadas a ela.
    CONSTRAINT fk_category_parent FOREIGN KEY (id_category_parent)
        REFERENCES tb_category (id_category)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- A categoria não pode ser ligada a ela mesma.
-- Essa regra será controlada pela aplicação.

CREATE INDEX idx_category_parent ON tb_category (id_category_parent);

-- =====================================================================
-- tb_product
-- =====================================================================
CREATE TABLE tb_product (
    id_product    INT AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    value           DECIMAL(15,2) NOT NULL,
    quantity        INT NOT NULL,
    id_seller     INT NOT NULL,
    id_category   INT NOT NULL,

    CONSTRAINT pk_product PRIMARY KEY (id_product),
    CONSTRAINT ck_product_value_positive CHECK (value > 0),
    CONSTRAINT ck_product_quantity_nonneg CHECK (quantity >= 0),
   -- RN02: todo produto deve estar ligado a um vendedor existente.
-- Não é possível excluir um vendedor que ainda possui produtos cadastrados.
    CONSTRAINT fk_product_seller FOREIGN KEY (id_seller)
        REFERENCES tb_seller (id_seller)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    -- RN04: todo produto pertence a uma categoria (NOT NULL + FK).
    CONSTRAINT fk_product_category FOREIGN KEY (id_category)
        REFERENCES tb_category (id_category)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_product_seller ON tb_product (id_seller);
CREATE INDEX idx_product_category ON tb_product (id_category);

-- =====================================================================
-- tb_supplier
-- =====================================================================
CREATE TABLE tb_supplier (
    id_supplier   INT AUTO_INCREMENT,
    name           VARCHAR(100) NOT NULL,
    CNPJ            CHAR(14) NOT NULL,
    phone           VARCHAR(20) NULL,

    CONSTRAINT pk_supplier PRIMARY KEY (id_supplier),
    CONSTRAINT uq_supplier_cnpj UNIQUE (CNPJ)
) ENGINE=InnoDB;

-- =====================================================================
-- tb_cart
-- =====================================================================
CREATE TABLE tb_cart (
    id_cart         INT AUTO_INCREMENT,
    totalvalue       DECIMAL(15,2) NOT NULL DEFAULT 0,   -- derivado (soma de tb_buyItem)
    itemquantity     INT NOT NULL DEFAULT 0,             -- derivado (soma de tb_buyItem)
    id_client      INT NOT NULL,

    CONSTRAINT pk_cart PRIMARY KEY (id_cart),
    CONSTRAINT ck_cart_totalvalue_nonneg CHECK (totalvalue >= 0),
    CONSTRAINT ck_cart_itemquantity_nonneg CHECK (itemquantity >= 0),
  -- RN06: cada carrinho pertence a um cliente.
-- RN07: um cliente pode possuir mais de um carrinho.
-- ON DELETE CASCADE: ao excluir um cliente, seus carrinhos também são excluídos.
    CONSTRAINT fk_cart_client FOREIGN KEY (id_client)
        REFERENCES tb_client (id_client)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_cart_client ON tb_cart (id_client);

-- =====================================================================
-- tb_buyItem  (associativa N:N - carrinho <-> produto)
-- =====================================================================
CREATE TABLE tb_buyItem (
    id_product   INT NOT NULL,
    id_cart      INT NOT NULL,
    quantity      INT NOT NULL,
    unit_value     DECIMAL(15,2) NOT NULL,

    CONSTRAINT pk_buyitem PRIMARY KEY (id_product, id_cart),
    -- RN08: quantidade deve ser maior que zero.
    CONSTRAINT ck_buyitem_quantity_positive CHECK (quantity > 0),
    CONSTRAINT ck_buyitem_unitvalue_positive CHECK (unit_value > 0),
    -- ON DELETE RESTRICT: não permite excluir um produto que está em um carrinho.
    CONSTRAINT fk_buyitem_product FOREIGN KEY (id_product)
        REFERENCES tb_product (id_product)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    -- ON DELETE CASCADE: ao excluir o carrinho, seus itens também são excluídos.
    CONSTRAINT fk_buyitem_cart FOREIGN KEY (id_cart)
        REFERENCES tb_cart (id_cart)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- tb_order
-- =====================================================================
CREATE TABLE tb_order (
    id_order      INT AUTO_INCREMENT,
    date_order     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    totalvalue      DECIMAL(15,2) NOT NULL,
    id_client     INT NOT NULL,

    CONSTRAINT pk_order PRIMARY KEY (id_order),
    CONSTRAINT ck_order_totalvalue_nonneg CHECK (totalvalue >= 0),
    -- ON DELETE RESTRICT: não permite excluir um cliente que possui pedidos registrados.
    CONSTRAINT fk_order_client FOREIGN KEY (id_client)
        REFERENCES tb_client (id_client)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_order_client ON tb_order (id_client);

-- RN17: a data do pedido não pode ser futura.
-- Essa regra será controlada pela aplicação.

-- =====================================================================
-- tb_orderItem  (associativa N:N - pedido <-> produto)
-- =====================================================================
CREATE TABLE tb_orderItem (
    id_order     INT NOT NULL,
    id_product   INT NOT NULL,
    quantity      INT NOT NULL,
    unit_value     DECIMAL(15,2) NOT NULL,

    CONSTRAINT pk_orderitem PRIMARY KEY (id_order, id_product),
    CONSTRAINT ck_orderitem_quantity_positive CHECK (quantity > 0),
    CONSTRAINT ck_orderitem_unitvalue_positive CHECK (unit_value > 0),
    -- ON DELETE CASCADE no pedido: apagar o pedido remove seus itens.
    CONSTRAINT fk_orderitem_order FOREIGN KEY (id_order)
        REFERENCES tb_order (id_order)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    -- ON DELETE RESTRICT: não permite excluir um produto que possui pedidos registrados.
    CONSTRAINT fk_orderitem_product FOREIGN KEY (id_product)
        REFERENCES tb_product (id_product)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- RN11, RN12 e RN13 são controladas pela aplicação
-- e verificadas por consultas.

-- =====================================================================
-- tb_order_status  (entidade fraca - identificacao por dependencia)
-- =====================================================================
CREATE TABLE tb_order_status (
    id_order      INT NOT NULL,
    date_status    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status          VARCHAR(20) NOT NULL,

  -- RN14: a chave primária é formada pelo id do pedido e pela data do status.
-- Isso permite registrar o histórico de status de cada pedido.
    CONSTRAINT pk_orderstatus PRIMARY KEY (id_order, date_status),
   -- RN15: define os valores permitidos para o status do pedido.
    CONSTRAINT ck_orderstatus_domain CHECK (
        status IN ('aberto', 'pago', 'enviado', 'entregue', 'cancelado')
    ),
    CONSTRAINT fk_orderstatus_order FOREIGN KEY (id_order)
        REFERENCES tb_order (id_order)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- tb_product_supplier  (associativa N:N - produto <-> fornecedor)
-- =====================================================================
CREATE TABLE tb_product_supplier (
    id_product      INT NOT NULL,
    id_supplier     INT NOT NULL,
    delivery_time     INT NOT NULL,

  -- RN19: o mesmo produto e fornecedor não podem aparecer juntos mais de uma vez.
    CONSTRAINT pk_productsupplier PRIMARY KEY (id_product, id_supplier),
    CONSTRAINT ck_productsupplier_deliverytime_positive CHECK (delivery_time > 0),
   -- RN18: um produto pode possuir mais de um fornecedor.
    -- ON DELETE CASCADE: ao excluir um produto, seus vínculos com fornecedores também são excluídos.
    CONSTRAINT fk_productsupplier_product FOREIGN KEY (id_product)
        REFERENCES tb_product (id_product)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
   -- ON DELETE RESTRICT: não permite excluir um fornecedor que ainda está vinculado a um produto.
    CONSTRAINT fk_productsupplier_supplier FOREIGN KEY (id_supplier)
        REFERENCES tb_supplier (id_supplier)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB;
