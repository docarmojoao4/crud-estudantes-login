# CRUD de Estudantes
 
Aplicação fullstack para cadastro e gerenciamento de estudantes. O projeto nasceu como uma aplicação de console em Java (JDBC) e foi migrado para **Spring Boot**, com uma API REST e um front-end web servido pela própria aplicação.
 
## Funcionalidades
 
- Criação de conta e login de usuários
- Edição e exclusão da própria conta
- Cadastro, edição, exclusão e listagem de estudantes
- Busca de estudantes por nome
- Registro de quem fez a última alteração em cada estudante, e de quando ela ocorreu
- Cálculo automático da idade a partir da data de nascimento
### Regras de negócio
 
| Regra | Detalhe |
|-------|---------|
| Senha | Mínimo de 8 caracteres, com letra maiúscula, número, letra e caractere especial |
| Data de nascimento | Não pode ser uma data futura |
| Idade mínima | O estudante precisa ter pelo menos 6 anos |
 
## Tecnologias
 
- **Java 25**
- **Spring Boot 4.1** (Spring Web MVC, Spring Data JPA, Validation)
- **MySQL** com driver `mysql-connector-j`
- **Maven**
- **HTML, CSS e JavaScript puro** no front-end
## Pré-requisitos
 
- JDK 25
- MySQL em execução
- Maven (opcional, pois o IntelliJ traz um embutido)
## Como executar
 
1. **Crie o banco de dados:**
```sql
   CREATE DATABASE crud_estudantes;
```
 
   As tabelas `Usuarios` e `Estudantes` são criadas automaticamente na primeira execução (`spring.jpa.hibernate.ddl-auto=update`).
 
2. **Configure a conexão** em `src/main/resources/application.properties`, ou use variáveis de ambiente:
   | Variável | Descrição | Padrão |
   |----------|-----------|--------|
   | `DB_USER` | Usuário do MySQL | `root` |
   | `DB_PASSWORD` | Senha do MySQL | definida no `application.properties` |
3. **Suba a aplicação:**
   - Pela IDE: execute a classe `CrudEstudantesApplication`.
   - Pelo terminal (com Maven instalado): `mvn spring-boot:run`
4. **Acesse** `http://localhost:8080`. O front-end abre direto nessa URL, e a API fica em `/api/...`.
Na primeira vez o banco está vazio. Use a aba **Criar conta** para cadastrar o primeiro usuário.
 
## Estrutura do projeto
 
```
src/main
├── java/br/com/estudantes
│   ├── CrudEstudantesApplication.java   # classe principal
│   ├── controller/                      # endpoints REST
│   ├── service/                         # regras de negócio
│   ├── dao/                             # acesso a dados (Spring Data JPA)
│   ├── model/                           # entidades JPA
│   ├── dto/                             # objetos de entrada da API
│   └── exception/                       # exceções e tratamento global de erros
└── resources
    ├── application.properties
    └── static/                          # front-end
        ├── index.html
        ├── css/style.css
        └── js/
            ├── api.js                   # chamadas à API
            └── app.js                   # telas e eventos
```
 
Fluxo de uma requisição: **front-end → controller → service → DAO → MySQL**.
 
## API
 
| Ação | Método e endpoint |
|------|-------------------|
| Fazer login | `POST /api/auth/login` |
| Cadastrar usuário | `POST /api/usuarios` |
| Buscar usuário por ID | `GET /api/usuarios/{id}` |
| Editar usuário | `PUT /api/usuarios/{id}` |
| Apagar usuário | `DELETE /api/usuarios/{id}` |
| Listar estudantes | `GET /api/estudantes` |
| Buscar estudante por ID | `GET /api/estudantes/{id}` |
| Buscar estudantes por nome | `GET /api/estudantes/busca?nome=ana` |
| Cadastrar estudante | `POST /api/estudantes` |
| Editar estudante | `PUT /api/estudantes/{id}` |
| Apagar estudante | `DELETE /api/estudantes/{id}` |
 
### Exemplos
 
```bash
# criar usuário
curl -X POST localhost:8080/api/usuarios -H "Content-Type: application/json" \
  -d '{"nome":"Maria","login":"maria","senha":"Senha@123"}'
 
# login
curl -X POST localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"login":"maria","senha":"Senha@123"}'
 
# cadastrar estudante (usuarioId = usuário que realiza a operação)
curl -X POST localhost:8080/api/estudantes -H "Content-Type: application/json" \
  -d '{"nome":"João","cpf":"12345678900","dataNascimento":"15/03/2012","usuarioId":1}'
```
 
As datas trafegam no formato `dd/MM/yyyy`. A senha pode ser enviada para a API, mas nunca é devolvida nas respostas.
 
### Respostas de erro
 
Os erros voltam em JSON, com o status e uma mensagem:
 
```json
{ "status": 400, "mensagem": "Senha inválida! Defina uma senha contendo pelo menos 8 caracteres!" }
```
 
| Status | Quando acontece |
|--------|-----------------|
| 400 | Regra de negócio violada, campo obrigatório ausente ou JSON inválido |
| 401 | Login ou senha incorretos |
| 404 | Registro não encontrado |
| 409 | Operação bloqueada por vínculo, como apagar um usuário que ainda tem estudantes |
