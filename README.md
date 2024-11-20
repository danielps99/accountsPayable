# accountsPayable
Projeto de estudo para gerenciar contas a pagar

## Tópicos do documento
- [Execute com Docker Compose](#execute-com-docker-compose)
    - [Requisitos](#requisitos)
- [Para subir o projeto](#para-subir-o-projeto)
    - [Observação](#observação)
- [Configuração do arquivo docker-compose.yaml inclui](#configuração-do-arquivo-docker-composeyaml-inclui-)
- [Configuração do arquivo Dockerfile inclui](#configuração-do-arquivo-dockerfile-inclui)
- [Documentação dos endpoints da aplicação](#documentação-dos-endpoints-da-aplicação)
- [Como utilizar a aplicação sem frontend](#como-utilizar-a-aplicação-sem-frontend)
    - [Utilizando Postman](#utilizando-postman-httpswwwpostmancom)
    - [Utilizando o Swagger do projeto](#utilizando-o-swagger-do-projeto)
- [Fazer login na aplicação](#fazer-login-na-aplicação)
- [Requisitar os endpoints da aplicação](#requisitar-os-endpoints-da-aplicação)
    - [Utilizando Postman](#utilizando-postman)
    - [Utilizando o Swagger do projeto](#utilizando-o-swagger-do-projeto-1)
- [Implementações realizadas](#implementações-realizadas)
- [Detalhes de conexão do banco de dados](#detalhes-de-conexão-do-banco-de-dados)

## Execute com Docker Compose
### Requisitos:
- Docker Compose version v2.20.3+

## Para subir o projeto
Na raiz do projeto, onde está o arquivo docker-compose.yaml, execute:
```
docker-compose up
```
Ao alterar o código do projeto, o comando abaixo pode ser util
```
docker-compose up --build --force-recreate accounts-payable-api
```
### Observação:
Após a inicialização dos containers, a aplicação ainda pode demorar alguns segundos para responder.

## Configuração do arquivo docker-compose.yaml inclui: 
- Container postgres 15
- Container da aplicação construído a partir do Dockerfile

## Configuração do arquivo Dockerfile inclui:
- Carregamento da imagem maven:3-openjdk-17-slim para compilar o projeto com "mvn clean install"
- Carregamento da imagem openjdk:17 que servirá de base para criar o container da aplicação.

## Documentação dos endpoints da aplicação
- A aplicação tem o Swagger configurado no endereço: http://localhost:8080/swagger-ui/index.html
- O arquivo em src/main/resources/accountPayableExamples.csv é um exemplo de arquivo csv esperado no endpoint: /api/account-payable/upload-csv


## Como utilizar a aplicação sem frontend
### Utilizando Postman https://www.postman.com
- Se utilizar o Postman instalado, pode importar o arquivo AccountPayable.postman_collection.json presente na raiz do projeto.

### Utilizando o Swagger do projeto
- Apos subir o projeto, o Swagger fica disponível localmente em http://localhost:8080/swagger-ui/index.html

## Fazer login na aplicação
Aplicação possui usuário cadastrado em memória.
- No endpoint de login, utilizar:
  - Usuário: admin
  - Senha: admin

## Requisitar os endpoints da aplicação
Os endpoints são protegidos por mecanismo de autenticação. Caso não esteja autenticado, é retornado status 403.
### Utilizando Postman
- Após importar o arquivo AccountPayable.postman_collection.json no Postman, é só requisitar o endpoint de login, com usuário e senha corretos, que o token de autorização é setado automaticamente nas outras requisições.
### Utilizando o Swagger do projeto
- É necessário copiar o token vindo no Header Authorization da requisição de login e colar nas outras requisições.

## Implementações realizadas:
1. Cadastrar conta;
2. Atualizar conta;
3. Alterar a situação da conta;
4. Obter a lista de contas a pagar, com filtro de data de vencimento e descrição;
5. Obter conta filtrando o id;
6. Obter valor total pago por período.
7. Implementar mecanismo para importação de contas a pagar via arquivo csv. O arquivo será consumido via API.

## Detalhes de conexão do banco de dados
Banco de dados Postgres. Nome do banco, nome de usuário e senha podem ser encontrados no arquivo .env presente na raiz do projeto

**Hostname :** localhost

**Porta :** 5432

**Nome Banco :** accountsPayable

**JDBC Url :** jdbc:postgresql://localhost:5432/accountsPayable

**Nome Usuário :** developer

**Senha :** freeaccess
