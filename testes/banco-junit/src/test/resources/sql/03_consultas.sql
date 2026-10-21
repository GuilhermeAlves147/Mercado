USE marketplace_db;

-- 1. Quais produtos estão cadastrados no marketplace?

SELECT name, value, quantity
FROM tb_product;

-- 2. Quais produtos custam mais de R$ 100, ordenados do mais caro para o mais barato?

SELECT name, value
FROM tb_product
WHERE value > 100
ORDER BY value DESC;

-- Em ordem vai aparecer o celular (1500) e depois a panela (120).

-- 3. Quais produtos possuem a palavra "Celular" no nome?

SELECT name, value
FROM tb_product
WHERE name LIKE '%Celular%';
-- usa o LIKE pra encontra um padrão no nosso texto.
-- LIKE vai procurar na coluna name todos os registros com a palavra celular.

-- 4. Quais produtos custam entre R$ 100 e R$ 2000 e pertencem às categorias 3 ou 4?

SELECT name, value, id_category
FROM tb_product
WHERE value BETWEEN 100 AND 2000
AND id_category IN (3,4);
-- categoria 3 é o celular Samsung e a panela de pressão vai pertencer a categoria 4.

-- 5. Quais fornecedores não possuem telefone cadastrado?

SELECT name, phone
FROM tb_supplier
WHERE phone IS NULL;

-- eu tinha cadastrado Elias sem telefone

-- 6. Qual é a categoria de cada produto?

SELECT tb_product.name, tb_category.category
FROM tb_product
JOIN tb_category
ON tb_product.id_category = tb_category.id_category;

-- 7. Quais produtos estão sendo vendidos e qual usuário é responsável por cada venda?

-- tb_product.name: mostra o nome do produto
-- tb_user.user: mostra o nome do vendedor
-- tb_product.value: mostra o valor do produto
-- JOIN tb_seller: junta o produto com o vendedor responsável por ele
-- JOIN tb_user: pega o nome do usuário que é esse vendedor

SELECT tb_product.name, tb_user.user, tb_product.value
FROM tb_product
JOIN tb_seller
ON tb_product.id_seller = tb_seller.id_seller
JOIN tb_user
ON tb_seller.id_seller = tb_user.id_user;

-- 8. Quais produtos possuem ou não registro em pedidos?

-- tb_orderItem.id_order: mostra em qual pedido o produto apareceu
-- LEFT JOIN: mostra todos os produtos mesmo se não tiverem aparecido em nenhum pedido

SELECT tb_product.name, tb_orderItem.id_order
FROM tb_product
LEFT JOIN tb_orderItem
ON tb_product.id_product = tb_orderItem.id_product;

-- 9. Quais pedidos possuem mais de um registro no histórico de status?

-- GROUP BY: agrupa os registros que possuem o mesmo id_order
-- COUNT: conta quantos status cada pedido possui
-- HAVING: mostra apenas os pedidos que possuem mais de um status

SELECT id_order, COUNT(*) AS quantidade_status
FROM tb_order_status
GROUP BY id_order
HAVING COUNT(*) > 1;

-- são 4 status: Aberto,pago,enviado e entregue.

-- 10. Quantos produtos estão associados a cada fornecedor?

-- tb_supplier.name: mostra o nome do fornecedor
-- COUNT: conta quantos produtos estão ligados a cada fornecedor
-- LEFT JOIN: mostra o fornecedor mesmo se ele não tiver produto associado
-- GROUP BY: agrupa os resultados de cada fornecedor

SELECT tb_supplier.name, COUNT(tb_product_supplier.id_product) AS quantidade_produtos
FROM tb_supplier
LEFT JOIN tb_product_supplier
ON tb_supplier.id_supplier = tb_product_supplier.id_supplier
GROUP BY tb_supplier.id_supplier, tb_supplier.name;

-- 11. Quais produtos possuem valor maior que a média de preço dos produtos?

-- AVG: calcula a média dos valores dos produtos
-- a consulta que está dentro dos parênteses calcula primeiro a média
-- depois mostra apenas os produtos que possuem valor maior que essa média

SELECT name, value
FROM tb_product
WHERE value > (
    SELECT AVG(value)
    FROM tb_product
);
-- no caso só o celular possui um valor maior que a média que é 810,o valor do celular é 1500.

-- 12. Quais clientes já realizaram pelo menos um pedido?

-- JOIN: liga o cliente ao usuário para conseguir mostrar o nome
-- EXISTS: verifica se existe algum pedido feito por aquele cliente
-- SELECT 1: serve apenas para verificar se o registro existe

SELECT tb_user.user
FROM tb_user
JOIN tb_client
ON tb_user.id_user = tb_client.id_client
WHERE EXISTS (
    SELECT 1
    FROM tb_order
    WHERE tb_order.id_client = tb_client.id_client
);

-- 13. Quais produtos ainda não foram comprados em nenhum pedido?

SELECT tb_product.name, tb_product.quantity
FROM tb_product
WHERE NOT EXISTS (
    SELECT 1
    FROM tb_orderItem
    WHERE tb_orderItem.id_product = tb_product.id_product
);
-- não foi comprado em nenhum pedido ainda foi a panela de pressão.

-- 14. Qual é o pedido com o maior valor?

-- MAX: procura o maior valor entre todos os pedidos
-- depois mostra o pedido que possui o valor maior,por busca no parênteses.

SELECT id_order, totalvalue, id_client
FROM tb_order
WHERE totalvalue = (
    SELECT MAX(totalvalue)
    FROM tb_order
);
-- obviamente o celular possui o maior valor.

-- 15. Qual é o status mais recente de cada pedido e quem é o cliente?

-- os JOIN junta as tabelas para conseguir mostrar o cliente e o status do pedido
-- MAX pega a data do status mais recente
-- a consulta de dentro verifica qual foi o ultimo status daquele pedido

SELECT tb_order.id_order, tb_user.user, tb_order_status.status, tb_order_status.date_status
FROM tb_order
JOIN tb_client
ON tb_order.id_client = tb_client.id_client
JOIN tb_user
ON tb_client.id_client = tb_user.id_user
JOIN tb_order_status
ON tb_order.id_order = tb_order_status.id_order
WHERE tb_order_status.date_status = (
    SELECT MAX(status_atual.date_status)
    FROM tb_order_status AS status_atual
    WHERE status_atual.id_order = tb_order.id_order
);


