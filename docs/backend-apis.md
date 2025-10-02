# APIs e Web Services

O planejamento de uma aplicação de APIS Web é uma etapa fundamental para o sucesso do projeto. Ao planejar adequadamente, você pode evitar muitos problemas e garantir que a sua API seja segura, escalável e eficiente.

Aqui estão algumas etapas importantes que devem ser consideradas no planejamento de uma aplicação de APIS Web.

[Inclua uma breve descrição do projeto.]

## Objetivos da API

O primeiro passo é definir os objetivos da sua API. O que você espera alcançar com ela? Você quer que ela seja usada por clientes externos ou apenas por aplicações internas? Quais são os recursos que a API deve fornecer?

[Inclua os objetivos da sua api.]


## Modelagem da Aplicação
[Descreva a modelagem da aplicação, incluindo a estrutura de dados, diagramas de classes ou entidades, e outras representações visuais relevantes.]


## Tecnologias Utilizadas

Existem muitas tecnologias diferentes que podem ser usadas para desenvolver APIs Web. A tecnologia certa para o seu projeto dependerá dos seus objetivos, dos seus clientes e dos recursos que a API deve fornecer.

[Lista das tecnologias principais que serão utilizadas no projeto.]

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

[Discuta as considerações de segurança relevantes para a aplicação distribuída, como autenticação, autorização, proteção contra ataques, etc.]

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

