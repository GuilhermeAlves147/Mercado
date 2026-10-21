# Testes de software - Marketplace (Grupo 9)

| Pasta | O que testa | Ferramenta | Como rodar |
| --- | --- | --- | --- |
| banco-junit | DDL, carga e consultas SQL (marketplace_db) | JUnit 5 + JDBC (projeto marketplace-testes) | IntelliJ: executar cada classe de teste |
| unitario-login | UserService.login do backend | JUnit 5 + Mockito | Copiar para `src/test/java/com/ucb/market/services/` do projeto Mercado e executar a classe |
| api-postman | 22 requisicoes da API | Postman | Importar `Mercado.postman_collection.json` e executar a colecao (Run collection) |
| e2e-playwright | Interface (9 casos) | Playwright | `npm install`, `npx playwright install chromium`, `npx playwright test` |

## Antes de rodar
- Banco carregado (CargaTest) e backend Mercado rodando. Os testes de banco apagam e recriam o marketplace_db: depois deles, rodar o CargaTest de novo antes da API e do E2E.
- E2E: a porta padrao e 8081. Para outra porta, `set BASE_URL=http://localhost:8080` (Windows CMD).
- Nao publique senhas: usuario e senha do MySQL ficam no seu computador (DbHelper.java e application.properties).
