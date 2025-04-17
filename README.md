
# Assistência Técnica - Backend

Este é o backend do sistema de gestão para assistência técnica, desenvolvido com Java e Spring Boot. Ele oferece funcionalidades para gerenciar clientes, ordens de serviço, dispositivos e estoque, integrando com o banco de dados PostgreSQL.

## Funcionalidades

- **Gestão de Dispositivos:** Criação, leitura, atualização e exclusão de dispositivos.
- **Gestão de Clientes:** Cadastro, atualização e exclusão de clientes.
- **Ordens de Serviço:** Criação e atualização de ordens de serviço.

## Tecnologias

- **Backend:** Java 21, Spring Boot
- **Banco de Dados:** PostgreSQL
- **Modelagem:** ModelMapper para conversões entre entidades e DTOs
- **Docker:** Utilizado para contêinerizar o ambiente, facilitando o uso do banco de dados.

## Frontend

O frontend do sistema de Assistência Técnica pode ser encontrado no seguinte repositório:

[Frontend - Assistência Técnica](https://github.com/LucasFL05/Angular_assistencia-tecnica/tree/main)

## Como Rodar o Backend

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/LucasFL05/assistencia-tecnica.git
   ```

2. **Instale as dependências do Maven:**

   Navegue até o diretório do backend e execute:

   ```bash
   mvn install
   ```

3. **Suba o banco de dados com Docker (opcional):**

   Caso queira rodar o banco de dados via Docker, utilize o comando:

   ```bash
   docker-compose up
   ```

4. **Execute o Backend:**

   Inicie o servidor Spring Boot:

   ```bash
   mvn spring-boot:run
   ```

## Estrutura do Backend

O backend é organizado da seguinte forma:

1. **Controller:** Define os endpoints para interação com o sistema.
2. **Service:** Lógica de negócios, como criação, atualização e exclusão de entidades.
3. **Repository:** Interface para persistência de dados no banco PostgreSQL.
4. **DTO (Data Transfer Object):** Representação de dados a serem transferidos entre as camadas da aplicação.

## Contribuições

Sinta-se à vontade para contribuir para este projeto. Para isso, crie uma nova branch para suas modificações e envie um pull request.
