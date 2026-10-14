
# 🛒 Market — Banca Digital

Sistema de mercado digital desenvolvido como projeto acadêmico.

O projeto permite visualizar produtos, cadastrar usuários, realizar login, cadastrar produtos e realizar compras através de um carrinho.

## 🚀 Funcionalidades

- 👤 Cadastro de usuários
- 🔐 Login
- 🛍️ Visualização de produtos
- 🔎 Busca de produtos
- ➕ Cadastro de produtos
- ✏️ Edição de produtos
- 🗑️ Exclusão de produtos
- 🧺 Carrinho de compras
- 💰 Cálculo do total da compra
- 📦 Finalização de pedidos

## 🛠️ Tecnologias

- Java
- Spring Boot
- MySQL
- HTML5
- CSS3
- JavaScript
- Git
- GitHub

## 📋 Requisitos

Para executar o projeto, é necessário ter instalado:

- Java
- MySQL
- Maven
- Um navegador
- IntelliJ IDEA ou outra IDE Java

## ⚙️ Configuração

Configure o banco de dados MySQL no arquivo:

```text
application.properties

# 🛒 Mercado — Banca Digital

Sistema de mercado desenvolvido como projeto acadêmico, com **frontend web** integrado a uma **API REST em Spring Boot** e banco de dados **MySQL**.

## 📌 Sobre o projeto

O **Mercado — Banca Digital** permite gerenciar produtos e usuários através de uma aplicação web.

O projeto foi desenvolvido com o objetivo de praticar conceitos de:

* Desenvolvimento Web
* Java e Spring Boot
* API REST
* Banco de Dados
* MySQL
* JavaScript
* HTML e CSS
* CRUD
* Integração entre Frontend e Backend

## 🚀 Tecnologias utilizadas

### Frontend

* HTML5
* CSS3
* JavaScript

### Backend

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* API REST

### Banco de dados

* MySQL

### Ferramentas

* IntelliJ IDEA
* MySQL Workbench
* Git
* GitHub

## 📂 Estrutura do projeto

```text
Mercado/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   │
│   └── pom.xml
│
├── frontend/
│   ├── index.html
│   ├── style.css
│   │
│   └── js/
│       ├── config.js
│       ├── api.js
│       └── app.js
│
└── README.md
```

## ⚙️ Funcionalidades

### 👤 Usuários

* Cadastro de usuários
* Login
* Consulta de usuários

### 📦 Produtos

* Cadastro de produtos
* Listagem de produtos
* Atualização de produtos
* Exclusão de produtos
* Produtos associados ao usuário

### 🛒 Carrinho

* Criação de carrinho
* Adição de produtos
* Consulta dos itens do carrinho

## 🔗 API

A API do backend é executada localmente na porta:

```text
http://localhost:8081
```

Principais endpoints:

```text
GET    /api/users
POST   /api/users
POST   /api/login

GET    /api/products
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}

GET    /api/carts
POST   /api/carts
GET    /api/carts/{id}/items
POST   /api/carts/{id}/items
```

## 🗄️ Banco de dados

O projeto utiliza **MySQL**.

Exemplo de configuração:

```properties
spring.datasource.url=jdbc:mysql://localhost:3307/tb_market
spring.datasource.username=root
spring.datasource.password=SUA_SENHA

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> Altere `SUA_SENHA` para a senha do seu MySQL.

## ▶️ Como executar

### 1. Clonar o projeto

```bash
git clone URL_DO_SEU_REPOSITORIO
```

### 2. Abrir o Backend

Abra a pasta do backend no IntelliJ IDEA.

Verifique se o MySQL está executando e se o banco `tb_market` existe.

```sql
CREATE DATABASE tb_market;
```

### 3. Configurar o banco

No arquivo:

```text
src/main/resources/application.properties
```

configure usuário, senha, porta e nome do banco.

### 4. Executar o Spring Boot

Execute a classe principal do projeto.

O backend deverá iniciar em:

```text
http://localhost:8081
```

### 5. Executar o Frontend

Abra o arquivo:

```text
frontend/index.html
```

ou utilize o **Live Server** do VS Code.

## 🔧 Configuração do Frontend

No arquivo:

```text
js/config.js
```

a API deve apontar para:

```javascript
const CONFIG = {
    API_BASE: "http://localhost:8081/api"
};
```

## 🧪 Testando a API

Você pode utilizar:

* Postman
* Insomnia
* Navegador
* Frontend do projeto

Exemplo de cadastro de produto:

```json
{
  "name": "Arroz",
  "price": 10.50,
  "description": "Arroz branco",
  "stock": 20
}
```

## ⚠️ Observações

O projeto foi desenvolvido para execução em ambiente local.

É necessário ter instalado:

* Java JDK 21
* MySQL
* IntelliJ IDEA ou outra IDE compatível
* Git

## 👨‍💻 Desenvolvedores

**Guilherme**
**Valter**
**Werverton**
**Luis**

Projeto desenvolvido para fins acadêmicos.

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais.
