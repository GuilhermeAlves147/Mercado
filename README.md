# Nexa — Marketplace 

Projeto acadêmico de marketplace de eletrônicos, com interface web em HTML, CSS e JavaScript, API REST em Spring Boot e banco MySQL.

## Funcionalidades
- Catálogo de produtos e categorias carregados da API/banco de dados
- Busca, filtro por categoria, ordenação e filtro de estoque
- Cadastro/login de usuários
- Área para anunciar e gerenciar produtos
- Carrinho e finalização de pedidos
- Produtos associados às categorias e vendedores existentes no modelo do banco

## Executar
1. Importe o banco usando o script `01_ddl.sql` e depois execute `02_carga.sql` do repositório SQL do grupo.
2. Configure as credenciais MySQL em `src/main/resources/application.properties` (não publique senhas no Git).
3. Execute `MarketApplication` no IntelliJ. O backend Spring Boot usa a porta 8080 por padrão.
4. A interface fica disponível na mesma origem em `http://localhost:8080/`. Também existe uma cópia independente em `frontend/`; se abrir por Live Server, confira `frontend/js/config.js` e a origem permitida no CORS.

## Observação
A interface usa os dados de produtos e categorias que a API fornece. Para exibir produtos reais, o banco precisa estar carregado e o backend em execução. Os nomes, preços, estoque e categorias não são inventados pelo frontend.
