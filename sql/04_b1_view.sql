-- Execute após 01_ddl.sql e 02_carga.sql, na mesma base usada pelo Spring Boot.
CREATE OR REPLACE VIEW vw_b1_resumo_categorias AS
SELECT c.id_category AS categoria_id, c.category AS categoria,
       COUNT(DISTINCT p.id_product) AS total_produtos,
       COALESCE(SUM(p.quantity),0) AS unidades_estoque,
       COALESCE(SUM(p.quantity * p.value),0) AS valor_estoque
FROM tb_category c
LEFT JOIN tb_product p ON p.id_category = c.id_category
GROUP BY c.id_category, c.category;
