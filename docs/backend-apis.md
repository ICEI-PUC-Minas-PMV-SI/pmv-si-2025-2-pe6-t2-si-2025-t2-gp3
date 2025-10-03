# APIs e Web Services

O planejamento de uma aplicação de APIS Web é uma etapa fundamental para o sucesso do projeto. Ao planejar adequadamente, você pode evitar muitos problemas e garantir que a sua API seja segura, escalável e eficiente.

Aqui estão algumas etapas importantes que devem ser consideradas no planejamento de uma aplicação de APIS Web.

[Inclua uma breve descrição do projeto.]

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

[Instruções para implantar a aplicação distribuída em um ambiente de produção.]

1. Defina os requisitos de hardware e software necessários para implantar a aplicação em um ambiente de produção.
2. Escolha uma plataforma de hospedagem adequada, como um provedor de nuvem ou um servidor dedicado.
3. Configure o ambiente de implantação, incluindo a instalação de dependências e configuração de variáveis de ambiente.
4. Faça o deploy da aplicação no ambiente escolhido, seguindo as instruções específicas da plataforma de hospedagem.
5. Realize testes para garantir que a aplicação esteja funcionando corretamente no ambiente de produção.

## Testes

[Descreva a estratégia de teste, incluindo os tipos de teste a serem realizados (unitários, integração, carga, etc.) e as ferramentas a serem utilizadas.]

1. Crie casos de teste para cobrir todos os requisitos funcionais e não funcionais da aplicação.
2. Implemente testes unitários para testar unidades individuais de código, como funções e classes.
3. Realize testes de integração para verificar a interação correta entre os componentes da aplicação.
4. Execute testes de carga para avaliar o desempenho da aplicação sob carga significativa.
5. Utilize ferramentas de teste adequadas, como frameworks de teste e ferramentas de automação de teste, para agilizar o processo de teste.

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

