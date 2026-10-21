# CRUD Estudantes (Spring Boot)

## Como rodar
1. Garanta que o MySQL esteja rodando com o banco `crud_estudantes` criado.
2. Ajuste usuário/senha em `src/main/resources/application.properties`
   (ou use as variáveis de ambiente `DB_USER` e `DB_PASSWORD`).
3. `mvn spring-boot:run` (ou rode `CrudEstudantesApplication` pela IDE). A API sobe em http://localhost:8080.

## Endpoints
| Antigo menu do console        | Endpoint                                |
|-------------------------------|-----------------------------------------|
| Fazer login                   | `POST   /api/auth/login`                |
| Registrar / Cadastrar usuário | `POST   /api/usuarios`                  |
| Editar usuário                | `PUT    /api/usuarios/{id}`             |
| Apagar usuário                | `DELETE /api/usuarios/{id}`             |
| (buscar usuário)              | `GET    /api/usuarios/{id}`             |
| Cadastrar estudante           | `POST   /api/estudantes`                |
| Editar estudante              | `PUT    /api/estudantes/{id}`           |
| Apagar estudante              | `DELETE /api/estudantes/{id}`           |
| Buscar estudante por ID       | `GET    /api/estudantes/{id}`           |
| Buscar estudante por nome     | `GET    /api/estudantes/busca?nome=ana` |
| (listar todos)                | `GET    /api/estudantes`                |

## Exemplos
```bash
curl -X POST localhost:8080/api/usuarios -H "Content-Type: application/json" \
  -d '{"nome":"Maria","login":"maria","senha":"Senha@123"}'

curl -X POST localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"login":"maria","senha":"Senha@123"}'

curl -X POST localhost:8080/api/estudantes -H "Content-Type: application/json" \
  -d '{"nome":"João","cpf":"12345678900","dataNascimento":"15/03/2012","usuarioId":1}'
```
