# APIs e Web Services

O sistema em desenvolvimento tem como onbjetivo registrar e acompanhar treinos de musculação. Faremos isso atraves de um sistema distribido com tres camadas: uma aplicação mobile para registro das series, repetições, cargas, e controle de tempo de cada usuário; uma aplicação frontend web com funcionalidades mais avançadas, como estatisticas e visualização de desempenho; e um backend responsável pelo processamento dos dados e integração com o banco de dados.


## Objetivos da API

A API será desenvolvida para centralizar e gerenciar a comunicação entre os componentes do nosso sistema distribuído. Os principais objetivos dela são: 

* Fornecer, sem expor para clientes externos, os serviços para o aplicativo mobile e frontend.
* Disponibilizar recursos para gerenciar os treinos, registros de series, repetições, cargas e acompanhar o historico de treinos.
* Disponibilizar relatórios e estatísticas de desempenho no frontend.
* Assegurar que a integração entre os componentes do sistema seja eficiente e segura.
* Preservar a integridade das informações.
* Facilitar a escalabilidade do sistema.

## Modelagem da Aplicação
A modelagem da aplicação contempla as seguintes entidades principais: 

1. **Usuário:** 

   ```java
   public class Usuarios {
   
     private Long id;
     private String nome;
     private Integer CPF;
     private String email;
     private String senha;
     private LocalDate dataNascimento;
   }
   ```

2. **Treino:**

   ```java
   public class Treino {
   
     private Long id;
     private String nome;
     private LocalDate dataInicio;
     private LocalDate dataFim;
     private Set<Ficha> fichas;
   }
   
   public class Ficha {
   
     private Long id;
     private DiaSemana diaSemana;
     private Set<ExercicioFicha> exercicios;
   }
   
   public class ExercicioFicha {
   
     private Long exercicioFichaId;
     private Long exercicioId;
     private Long fichaId;
     private Double carga;
     private String observacao;
     private Integer quantidadeDeSeries;
     private Integer minimoRepeticoes;
     private Integer maximoRepeticoes;
     private Integer descansoEmSegundos;
   }
   ```

3. **Serie:**

   ```java
   public class Serie {
   
     private Long serieId;
     private LocalDate data;
     private Double carga;
     private Integer repeticoes;
   }
   ```

4. **Exercicio:**

   ```java
   public class Exercicio {
   
     private Long id;
     private String nome;
     private GrupoMuscular grupoMuscular;
     private Equipamento equipamento;
   }
   ```

#### Modelagem do banco de dados:![Modelo Banco de dados](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/GymFlow%20-%20DB.png)

## Tecnologias Utilizadas

|                    |     FRONTEND     |    MOBILE    |     BACKEND     |
| ------------------ | :--------------: | :----------: | :-------------: |
| **LINGUAGENS**     | JavaScript, HTML |  TypeScript  |      Java       |
| **FRAMEWORKS**     |        -         |    React     |   Spring Boot   |
| **UI/DESIGN**      |       CSS        |      -       |        -        |
| **TESTES**         |        -         |      -       | Junit, Mockito  |
| **CI/CD**          |        -         |      -       | GitHub Actions  |
| **BANCO DE DADOS** |        -         | AsyncStorage |   PostgreSQL    |
| **HOSPEDAGEM**     |      Vercel      |      -       | Render, Railway |
| **CONTAINERS**     |        -         |      -       |     Docker      |
| **AUTENTICACAO**   |        -         |      -       | Spring Security |

## API Endpoints

### Buscar todos os exercicios
- Método: GET

- URL: /api/exercicios

- Resposta:
  - Sucesso (200 OK)
    ```json
    {
      "exercicios": [
        {
          "id": 0,
          "nome": "string",
          "grupoMuscular": "PEITO",
          "equipamento": "BARRA"
        }
      ]
    }
    ```
    
  - Erro (400) - Requisição inválida
  
  - Erro (404) - Recurso não encontrado
  
  - Erro (500) - Erro interno do servidor

### Cadastro de exercicios

* Método: POST

* URL: /api/exercicios

* Requisição: 

  ```json
  {
    "nome": "string",
    "grupoMuscular": "PEITO",
    "equipamento": "BARRA"
  }
  ```

* Resposta: 

  * Sucesso (201 CREATED)

    ```json
    {
      "id": 0,
      "nome": "string",
      "grupoMuscular": "PEITO",
      "equipamento": "BARRA"
    }
    ```

  * Erro (400) - Requisição inválida

  * Erro (404) - Recurso não encontrado

  * Erro (500) - Erro interno do servidor

### Cadastro de series

* Metodo: POST

* URL: /api/series

* Requisição:

  ```json
  {
    "exercicioFichaId": 0,
    "data": "2025-09-29",
    "carga": 0.1,
    "repeticoes": 0
  }
  ```

* Resposta:

  * Sucesso (201 CREATED)

    ```json
    {
      "serieId": 0,
      "data": "2025-09-29",
      "carga": 0.1,
      "repeticoes": 0
    }
    ```

  * Erro (400) - Requisição inválida

  * Erro (404) - Recurso não encontrado

  * Erro (500) - Erro interno do servidor

### Cadastro de Treinos

* Método: POST

* URL: /api/treinos

* Requisição:

  ```json
  {
    "nome": "string",
    "dataInicio": "2025-09-29",
    "dataFim": "2025-09-29"
  }
  ```

* Resposta:

  * Sucesso (201 - CREATED)

    ```json
    {
      "id": 0,
      "nome": "string",
      "dataInicio": "2025-09-29",
      "dataFim": "2025-09-29"
    }
    ```

  * Erro (400) - Requisição inválida

  * Erro (404) - Recurso não encontrado

  * Erro (500) - Erro interno do servidor

### Busca de treinos cadastrados

* Método: GET

* URL: /api/treinos

* Busca paginada. Parametros de busca:

  * page

  * size

  * sort

  * direction

* Resposta:

  * Sucesso (200 OK)

    ```json
    {
      "total": 0,
      "page": 0,
      "totalPages": 0,
      "size": 0,
      "treinos": [
        {
          "id": 0,
          "nome": "string",
          "dataInicio": "2025-10-02",
          "dataFim": "2025-10-02"
        }
      ]
    }
    ```

  * Sucesso (204) - Sem treinos cadastrados

  * Erro (400) - Requisição inválida

  * Erro (500) - Erro interno do servidor


### Cadastro de ficha

* Metodo: POST

* URL: /api/fichas

* Requisição:

  ```json
  {
    "treinoId": 0,
    "diaSemana": "SEGUNDA"
  }
  ```

* Resposta:

  * Sucesso (201 - CREATED)

    ```json
    {
      "id": 0,
      "diaSemana": "SEGUNDA"
    }
    ```

  * Erro (400) - Requisição inválida

  * Erro (404) - Recurso não encontrado

  * Erro (500) - Erro interno do servidor

### Cadastro de exercicios na ficha

* Metodo: 

* URL:

* Requisição:

  ```json
  {
    "fichaId": 0,
    "exercicioId": 0,
    "carga": 0.1,
    "observacao": "string",
    "quantidadeDeSeries": 0,
    "minimoRepeticoes": 0,
    "maximoRepeticoes": 0,
    "descansoEmSegundos": 0
  }
  ```

* Resposta:

  * Sucesso (201 - CREATED)

    ```json
    {
      "exercicioFichaId": 0,
      "fichaId": 0,
      "exercicioId": 0,
      "carga": 0.1,
      "observacao": "string",
      "quantidadeDeSeries": 0,
      "minimoRepeticoes": 0,
      "maximoRepeticoes": 0,
      "descansoEmSegundos": 0
    }
    ```

  * Erro (400) - Requisição inválida

  * Erro (404) - Recurso não encontrado

  * Erro (500) - Erro interno do servidor

### Busca todas as fichas cadastradas

* Método: GET

* URL: /api/fichas

* Busca paginada. Parametros de busca:

  * page

  * size

  * sort

  * direction

* Resposta:

  * Sucesso (200 OK)

    ```json
    {
      "total": 0,
      "page": 0,
      "totalPages": 0,
      "size": 0,
      "fichas": [
        {
          "id": 0,
          "diaSemana": "SEGUNDA"
        }
      ]
    }
    ```

  * Sucesso (204) - Sem fichas cadastradas

  * Erro (400) - Requisição inválida

  * Erro (500) - Erro interno do servidor


### Busca de exercicios de uma ficha

* Método: GET

* URL: /api/fichas/exercicio

* Parametros de busca:

  * idFicha  

* Resposta:

  * Sucesso (200 OK)

    ```json
    {
      "exerciciosDaFicha": [
        {
          "exercicioFichaId": 0,
          "nome": "string",
          "grupoMuscular": "PEITO",
          "equipamento": "BARRA",
          "carga": 0.1,
          "observacao": "string",
          "quantidadeDeSeries": 0,
          "minimoRepeticoes": 0,
          "maximoRepeticoes": 0,
          "descansoEmSegundos": 0
        }
      ]
    }
    ```

  * Sucesso (204) - Sem exercicios cadastrados na ficha

  * Erro (400) - Requisição inválida

  * Erro (500) - Erro interno do servidor



## Considerações de Segurança

Por se tratar de uma aplicação que envolve armazenamento e troca de dados sensíveis de usuários, foram definidos mecanismos de segurança para proteger a PAI e os serviços integrados. As definições são:

* **Autenticação** com JWT – JSON Web Token.
* **Autorização** com o controle de acesso aos recursos com base nas permissões do usuário autenticado.
* **Expiração de tokens.** Os tokens de autenticação terão tempo de expiração definido.
* **Configuração de CORS** para permitir apenas domínios autorizados acessarem a API.
* **Armazenamento seguro.** Senhas de usuário serão armazenadas utilizando hash seguro, e dados sensíveis não serão expostos em logs ou respostas da API.

## Implantação

Nossa implantação envolve a preparação de três ambientes para nossos principais componentes: backend, frontend e mobile

1. **Requisitos de Hardware e Software**
   - Servidor backend / API:
     - Processador compativel com múltiplas requisições
     - Memória suficiente para executar uma aplicação Spring
     - Suporte para linguagem Java
     - Suporte para conteinerização com Docker

   - Banco de dados:
     - Ser compatível com PostgreSQL

   - Frontend
     - Ambiente com suporte a HTML, CSS e JS

   - Mobile
     - Sem necessidade de infraestrutura para deploy

2. **Plataforma de Hospedagem**
   - Backend / API: Railway 
   - Banco de dados: Railway 
   - Frontend: Vercel
   - Mobile:  Geração do apk e disponibilização para download atraves de um link

3. **Configuração do Ambiente**
   - Backend / API:  instalação de dependencias, configuração de variaveis de ambiente.
   - Banco de dados: gerar url de conexão, executar script inicial do banco.
   - Frontend: configuração de urls de conexão e do servidor.
   - Mobile: Não será necessário

4. **Deploy**
   - Backend / API / Banco de dados: subir serviços e conectar ao banco de dados.
   - Frontend:  realizar o build e subir arquivos estáticos para o servidor de hospedagem.
   - Mobile: gerar APK  e disponibilizar link para download.

5. **Testes em ambiente não produtivo**
   - Verificar autenticação e acesso aos endpoints.
   - Testar integração entre frontend, mobile e backend.
   - Avaliar o desempenho sob múltiplas requisições.
   - Garantir que os dados estão sendo inseridos e consultados no banco corretamente.
   - Monitorar logs e possíveis erros.


## Testes

Para  assegurar a qualidade e a confiabilidade da aplicação desenvolvida vamos traçar uma estrategia de testes para garantir que a aplicação atenta aos requisistos funcionais e não funcionais definidos. Serão aplicados diferentes tipos de testes em diferentes etapas do desenvolvimento do projeto. Em algumas etapas vamos usar ferramentas de automação como apoio.

1. Testes Unitários
    - Objetivo: validar unidades isoladas do código desenvolvido, como funçoes e classes, assegurando que produzam a saída correta para diferentes entradas.
    - Exemplo: funções de calculo de carga total de treino, metodos que calculam estatisticas, metodos de cadastro, etc.

2. Testes de integração
    - Objetivo: verificar se diferentes módulos da aplicação interagem corretamente.
    - Exemplo: verificar se os serviços estão se comunicando corretamente com o banco de dados

3. Testes de Carga
    - Objetivo: validar se a aplicação se comporta com alto volume de acessos simultaneos e garante armazenamento dos dados.
    - Exemplo: 100 usuarios registrando treinos ao mesmo tempo na aplicação

4. Testes Funcionais
    - Objetivo: Validar se os requisitos levantados estão sendo atendidos simulando cenários reais de uso.
    - Exemplo: Registrar treino ou atualizar carga do exercicio na ficha.


### Onde serão aplicados os testes:

| Tipo de Teste        | Backend | Mobile | Frontend |
| -------------------- | :-----: | :----: | :------: |
| Testes Unitários     |   SIM   |  SIM   |   NÃO    |
| Testes de Integração |   SIM   |  NÃO   |   NÃO    |
| Testes de Carga      |   SIM   |  SIM   |   SIM    |
| Testes Funcionais    |   NÃO   |  SIM   |   SIM    |

### Casos de Testes

| Caso de Teste | Endpoint             | Dados de Entrada                            | Resultado Esperado                                       | Resultado Obtido  | Status     |
| ------------- | -------------------- | ------------------------------------------- | -------------------------------------------------------- | ----------------- | ---------- |
| CT-001        | `POST /api/exercicios` | `{"nome": "Agachamento Smith","grupoMuscular": "QUADRICEPS","equipamento": "SMITH_MACHINE"}` | Retornar `200 Created` com dados do Exercicio  | `200 Created`     | ✅ Aprovado |
| CT-002        | `POST /api/exercicios` | `{"nome": "Leg Press 45","grupoMuscular": "QUADRICEPS","equipamento": "LEG PRESS"}` | Retornar `400	Requisição inválida`   | `400	Requisição inválida`     | ✅ Aprovado |
| CT-003        | `POST /api/exercicios` | `{"nome": "Agachamento Smith","grupoMuscular": "QUADRICEPES","equipamento": "SMITH_MACHINE"}` | Retornar `400	Requisição inválida`   | `400	Requisição inválida`     | ✅ Aprovado |
| CT-004        | `GET /api/exercicios`  | Buscar a lista de exercícios cadastrados na API  | Retornar `200 Created` | `200 Created` | ✅ Aprovado |
| CT-003        | `POST /api/usuarios`   | CPF inválido                                | Retornar `400 Bad Request`                               | `400 Bad Request` | ✅ Aprovado |
| CT-004        | `POST /api/usuarios`   | `{ "nome": "Maria", "cpf": "12345678900" }` | Retornar `201 Created` com dados do paciente             | `201 Created`     | ✅ Aprovado |
| CT-005        | `POST /api/usuarios`   | CPF já existente                            | Retornar `409 Conflict` com mensagem “CPF já cadastrado” | `409 Conflict`    | ✅ Aprovado |
| CT-006        | `POST /api/usuarios`   | CPF inválido                                | Retornar `400 Bad Request`                               | `400 Bad Request` | ✅ Aprovado |



| ------------- | -------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------| ----------------- | ----------- |
| CT-015        | `POST /api/series`   | `"exercicioFichaId": 1, "data": "2025-15-10", "carga": 45.0, "repeticoes": 12`| Retornar Sucesso (201 CREATED)                | `201 Sucesso`     | ✅ Aprovado |
| CT-016        | `POST /api/series`   | `"exercicioFichaId": 2, "data": "quinze de outubro de 2025", "carga": 45kg, "repeticoes": 10`| Retornar  Erro (400) - Requisição inválida| `400 Requisição inválida`|     ✅ Aprovado |
| CT-017        | `POST /api/series`   | `"exercicioFichaId": 15, "data": "2025-15-10", "carga": 180, "repeticoes": 6`|  Retornar Sucesso (201 CREATED)               | `201 Sucesso`      | ✅ Aprovado |

---
# Referências

Inclua todas as referências (livros, artigos, sites, etc) utilizados no desenvolvimento do trabalho.

# Planejamento

##  Quadro de tarefas

> Apresente a divisão de tarefas entre os membros do grupo e o acompanhamento da execução, conforme o exemplo abaixo.

### Semana 1

Atualizado em: 21/04/2024

| Responsável   | Tarefa/Requisito | Iniciado em    | Prazo      | Status | Terminado em    |
| :----         |    :----         |      :----:    | :----:     | :----: | :----:          |
| AlunaX        | Introdução | 01/02/2024     | 07/02/2024 | ✔️    | 05/02/2024      |
| AlunaZ        | Objetivos    | 03/02/2024     | 10/02/2024 | 📝    |                 |
| AlunoY        | Histórias de usuário  | 01/01/2024     | 07/01/2005 | ⌛     |                 |
| AlunoK        | Personas 1  |    01/01/2024        | 12/02/2005 | ❌    |       |

#### Semana 2

Atualizado em: 21/04/2024

| Responsável   | Tarefa/Requisito | Iniciado em    | Prazo      | Status | Terminado em    |
| :----         |    :----         |      :----:    | :----:     | :----: | :----:          |
| AlunaX        | Página inicial   | 01/02/2024     | 07/03/2024 | ✔️    | 05/02/2024      |
| AlunaZ        | CSS unificado    | 03/02/2024     | 10/03/2024 | 📝    |                 |
| AlunoY        | Página de login  | 01/02/2024     | 07/03/2024 | ⌛     |                 |
| AlunoK        | Script de login  |  01/01/2024    | 12/03/2024 | ❌    |       |

Legenda:
- ✔️: terminado
- 📝: em execução
- ⌛: atrasado
- ❌: não iniciado

