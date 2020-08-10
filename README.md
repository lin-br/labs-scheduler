# Labs Scheduler API
Um projeto de agendamento para envio de carga de comunicação, desenvolvido em Java.

# Versões:
* 1.0.0 -> Desenvolvido em cima de um monolito.
* 2.0.0 -> Desenvolvido em microserviços, mas eu não subi na branch Master para não sobrescrever
o que foi solicitado, entretanto, caso queira validar o desenvolvimento do mesmo (clique aqui)[https://github.com/lin-br/labs-scheduler/tree/feature/internal-integration].
Nesse branch eu utilizo RabbitMQ e Google Protocol Buffer para realizar a comunicação entre os
serviços internos, dessa maneira a API fica disponível para usuários finais e caso o meu coleguinha
queira utilizar o mesmo serviço, ele pode realizar a comunicação internamente.

## Tecnologias

 - [`Spring boot`](https://spring.io) - Framework de desenvolvimento Java.
 - [`PostgreSQL`](https://www.postgresql.org/) - Banco de dados.
 - [`JPA/Hibernate`](https://hibernate.org/orm/) - Framework para persistência de dados / ORM.
 - [`Docker`](https://www.docker.com) - Executa aplicações dentro de invólucros chamados containers.
 - [`jUnit5 e Mockito`](https://junit.org/junit5/) - Framework para execução de testes.
 - [`H2 Database Engine`](https://mvnrepository.com/artifact/com.h2database/h2) - 
 Banco de dados executado em memória, utilizado nos testes.
 - [Swagger](https://swagger.io/) - Framework para documentação da API.

## Dependências
É necessário ter instalado na sua máquina os seguintes programas:
* O Gerenciador de dependências do Java, [Maven](https://maven.apache.org/),
caso ele não esteja instalado, siga o tutorial do próprio site: 
[Maven install](https://maven.apache.org/install.html).
* Aplicação de containers, estamos utilizando o [Docker](https://www.docker.com), caso ele não
esteja instalado, siga o tutorial do próprio site: [Get Docker](https://docs.docker.com/get-docker/).

## Instalação

### A cópia do repositório
Abra o seu terminal e faça o clone do repositório através do seguinte comando:
```bash
git clone https://github.com/lin-br/labs-scheduler.git
```
Após completar o download dos arquivos, acesso o diretório que foi criado:
```bash
cd ./labs-scheduler/
```
E execute o seguinte comando para realizar a instalação das dependências do aplicativo:
```bash
mvn clean package
```
Agora execute o seguinte comando para realizar a construção do aplicativo:
> Ps: Tenha certeza de que na sua máquina o Docker está ligado. 
```bash
mvn dockerfile:build
```
Por último, mas não menos importante, vamos iniciar o Schedule API:
> Ps: Caso ocorra algum erro nesse momento, verifique se possui algum aplicativo na sua máquina
que esteja a utilizar a porta `8080` ou `5432`. Caso não possua conhecimento para tal
procedimento, tente reiniciar a máquina e então voltar a executar o Schedule API.
```bash
docker-compose up
```

## Endpoints da API
São:
- Para cadastro de agendamento:
    POST http://localhost:8080/api/schedules
- Para consulta de agendamento:
    GET http://localhost:8080/api/schedules/{id}
- Para exclusão de agendamento:
    DELETE http://localhost:8080/api/schedules/{id}

É possível consulta-los pelo endpoint da documentação via [Swagger](http://localhost:8080/api/swagger-ui.html).

## Consumindo API

> É possível enviar requisições através de um aplicativo como o [POSTMAN](https://www.getpostman.com/downloads/) que
pode instalar na máquina ou uma extensão no navegador e realiza requisições para um URL.
> Outra opção é enviar comandos através do famoso **cURL**, neste caso, é o modo que adotarei.
Para testes e usos em casos reais, utilizo e recomendo fortemente o POSTMAN.

### Método POST - cadastro de agendamento

URL: **http://localhost:8080/api/schedules**

* Observação: Foi adicionado uma validação entre o destinatário e o tipo enviado:
    * Para `EMAIL`: o campo destinatário precisa possuir um e-mail válido.
    * Para `WHATSAPP` e `SMS`: o campo destinatário precisa possuir somente números e mais
    do que 9 caracteres. 

Vai cadastrar um `agendamento` com a data de envio em `07 de Dezembro de 2020 às 17:40 horas`,
para o destinatário `abc@email.com`, com a mensagem `teste 1` e o tipo de envio para `email`.
```bash
curl -X POST \
  'http://localhost:8080/api/schedules' \
  -H 'Content-Type: application/json' \
  -d '{"date":"2020-12-07 17:40:00","recipient":"abc@email.com","message":"teste 1","type":"EMAIL"}'
```
> Quando realizamos uma requisição POST para uma API Restful, o método retorna um header chamado
`Location` que possui a URL de consulta para aquela nova entidade que foi criada.
Para pegar o header `Location` envie o seguinte comando:
```bash
curl -i -X POST \
  'http://localhost:8080/api/schedules' \
  -H 'Content-Type: application/json' \
  -d '{"date":"2020-12-07 17:40:00","recipient":"abc@email.com","message":"teste 1","type":"EMAIL"}'
```
Exemplo de resposta:
```text
HTTP/1.1 201
Location: http://localhost:8080/api/schedules/ef51a952-f891-43da-a7f1-87a4c1d2c54b
Content-Length: 0
Date: Sat, 08 Aug 2020 19:08:04 GMT
```

### Método GET - consulta de agendamento

URL: **http://localhost:8080/api/schedules/{id}**
Exemplo de comando:
```bash
curl GET 'http://localhost:8080/api/schedules/6e7876f2-6139-47e2-a618-c927f0819df9'
```

### Método DELETE - exclusão de agendamento

URL: **http://localhost:8080/api/schedules/{id}**
Exemplo de comando:
```bash
curl DELETE 'http://localhost:8080/api/schedules/6e7876f2-6139-47e2-a618-c927f0819df9'
```

## License
[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)