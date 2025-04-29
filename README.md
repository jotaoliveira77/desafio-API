# 📌 Desafio API Consulta de Filmes

API Spring Boot para gerenciamento de usuários e autenticação com JWT.

## 🚀 Tecnologias
- **Java 17**
- **Spring Boot 3**
  - Spring Security
  - Spring Data JPA
- **JWT** (Autenticação)
- **PostgreSQL** (Banco de dados)
- **Maven** (Gerenciamento de dependências)

## 🔧 Configuração

1. **Banco de Dados**:
   Configure o PostgreSQL em `application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/desafioapi
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

2. **JWT**:
   Defina a chave secreta em `application.properties`:
   ```properties
   jwt.secret=sua_chave_secreta
   ```

## 🔌 Endpoints da API

### Autenticação
| Método | Rota          | Descrição         |
|--------|---------------|-------------------|
| POST   | `/auth/login` | Gera token JWT.   |

### Usuários
| Método | Rota     | Descrição                                              |
|--------|----------|--------------------------------------------------------|
| POST   | `/users` | Cria um novo usuário.                                  |
| GET    | `/users` | Lista todos os usuários (requer autenticação JWT).     |

## 🛠️ Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/jotaoliveira77/desafio-API.git
   cd desafio-API
   ```

2. Configure o banco de dados e JWT no `application.properties`.

3. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em `http://localhost:8080`.

## ✅ Requisitos

- Java 17 instalado
- PostgreSQL configurado e rodando
- Maven instalado
