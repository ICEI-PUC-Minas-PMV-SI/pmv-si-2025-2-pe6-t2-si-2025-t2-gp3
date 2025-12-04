# Front-end Web

O front-end web faz parte da arquitetura distribuída do sistema de acompanhamento de treinos, desenvolvido com o objetivo de oferecer uma experiência completa para os praticantes de musculação.  
A versão web tem foco principal na visualização de estatísticas, relatórios e gráficos de desempenho, além de permitir o gerenciamento detalhado dos treinos e das informações de perfil do usuário\*\*.  
Por meio da interface web, o usuário poderá acompanhar sua evolução de forma clara e objetiva, analisar dados históricos e gerenciar as rotinas de treino de maneira organizada e centralizada.

---

## Projeto da Interface Web

A interface web será projetada com base em usabilidade, clareza e eficiência, conforme os requisitos não funcionais do sistema.  
Seu design seguirá uma linha intuitiva, evitando elementos que possam distrair o usuário, garantindo uma navegação fluida e direta aos recursos essenciais.

O foco principal do projeto da interface web é fornecer uma experiência visual agradável, responsiva e centrada na análise de resultados, em contraste com o aplicativo mobile, que prioriza a praticidade do registro durante o treino.

## Wireframes

Os wireframes a seguir representam o esboço visual das principais telas do GymFlow, aplicativo mobile voltado à organização e acompanhamento de treinos. Esta etapa teve como objetivo definir a estrutura visual e funcional do sistema, facilitando a visualização do fluxo de navegação do usuário entre as telas de login, menu inicial, cronômetro e perfil.

O uso de wireframes permitiu validar antecipadamente a experiência do usuário (UX), garantindo que a disposição dos elementos na interface seja intuitiva, acessível e coerente com a proposta do aplicativo. A seguir, são apresentados os wireframes desenvolvidos para o GymFlow, que serviram de base para o desenvolvimento do protótipo funcional:

##### Tela de Cadastro

![Tela de Cadastro](./img/wiri-cadastro.jpeg)

##### Tela de Login

![Tela de Login](./img/wiri-login.jpeg)

##### Tela Início

![Tela de Gráficos](./img/wiri-inicio.jpeg)

##### Tela de Fichas

![Tela de Fichas](./img/wiri-fichas.jpeg)

##### Tela de Estatísticas

![Tela de Estatísticas](./img/wiri-graficos.jpeg)

##### Tela de Perfil

![Tela de Perfil](./img/wiri-perfil.png)

##### Iconografia

<p align ="center">
  <img src="./img/icon1.jpeg" alt="Icon 1" width="150"><br>
  <img src="./img/icon2.jpeg" alt="Icon 2" width="150"><br>
</p>


### 

## Fluxo de dados
### 1. Página de Cadastro

| Etapa | Origem | Destino | Tipo de Dado | Descrição |
|-------|---------|----------|---------------|------------|
| 1 | Usuário | Front-end | Texto | Inserção de nome, email e senha |
| 2 | Front-end | Front-end | Texto / Booleano | Validação dos campos (não vazios, formato do email, tamanho da senha) |
| 3 | Front-end | API `/api/usuarios` | JSON | Envio dos dados do novo usuário `{ nome, email, senha }` |
| 4 | API | Front-end | JSON | Retorno de confirmação ou mensagem de erro (ex: email já cadastrado) |
| 5 | Front-end | Usuário | Texto (mensagem) | Exibição do popup com o resultado do cadastro |


### 2. Página de Login

| Etapa | Origem | Destino | Tipo de Dado | Descrição |
|-------|---------|----------|---------------|------------|
| 1 | Usuário | Front-end | Texto | Inserção de email e senha |
| 2 | Front-end | Front-end | Texto / Booleano | Verificação de campos e formato de email |
| 3 | Front-end | API `/api/usuarios/login` | JSON | Envio das credenciais de login |
| 4 | API | Front-end | JSON | Retorno de token JWT ou mensagem de erro |
| 5 | Front-end | LocalStorage | String | Armazenamento do token JWT |
| 6 | Front-end | Página Principal | URL | Caminho de redirecionamento após login |

### 3. Página do Usuário

| Etapa | Origem | Destino | Tipo de Dado | Descrição |
|-------|---------|----------|---------------|------------|
| 1 | Front-end | LocalStorage | String | Verificação do token JWT armazenado para autenticação |
| 2 | Front-end | API `/api/usuarios` | JSON | Requisição GET para carregar os dados do usuário autenticado |
| 3 | API | Front-end | JSON | Retorno com informações do usuário (nome, email, idade, altura, peso) |
| 4 | Front-end | Interface | Texto / Numérico | Preenchimento dos campos de perfil com os dados recebidos |
| 5 | Usuário | Front-end | Texto / Númerico | Edição de dados pessoais e confirmação de salvamento |
| 6 | Front-end | API `/api/usuarios/{id}` | JSON | Envio dos dados atualizados do perfil |
| 7 | API | Front-end | JSON | Resposta de sucesso ou erro da atualização |
| 8 | Front-end | Interface | Texto (mensagem) | Exibição de popup informando o status do salvamento |
| 9 | Front-end | API `/api/usuarios/{id}/metas` | JSON | Requisição GET para carregar metas cadastradas |
| 10 | API | Front-end | JSON | Retorno com lista de metas existentes |
| 11 | Usuário | Front-end | texto | Inserção de nova meta pelo modal |
| 12 | Front-end | API `/api/usuarios/{id}/metas` | JSON | Envio da nova meta  |
| 13 | API | Front-end | JSON | Retorno da meta criada |
| 14 | Usuário | Front-end | Texto / Númerico | Edição da meta |
| 15 | Front-end | API `/api/usuarios/{id}/metas` | JSON | Envio dos dados atualizados da meta |
| 16 | Front-end | Interface | Texto / Numérico | Atualização automática do IMC com base em peso e altura |
| 17 | Usuário | Front-end | Evento | Clique em “Sair” limpa o estado e redireciona para a página inicial |

### 4. Menu Principal

| Etapa | Origem | Destino | Tipo de Dado | Descrição |
|-------|---------|----------|---------------|------------|
| 1 | Front-end | LocalStorage | Texto | Cadastrar uma ficha teste de treino |
| 2 | Front-end | LocalStorage | Texto | Cadastrar um exercício teste de treino |
| 3 | Front-end | LocalStorage | Texto | Remover Ficha adicionada |
| 4 | Front-end | LocalStorage | Texto | Remover Exercício teste adicionado |

### 5. Estatísticas

| Etapa | Origem | Destino | Tipo de Dado | Descrição |
|-------|---------|----------|---------------|------------|
| 1 | Front-end | LocalStorage | String | Verificação do token JWT armazenado para autenticação |
| 2 |API | Interface | Json| Gráficos| Tabelas criadas para visualização |

---
## Design Visual

**Paleta de cores**

- **Primária (#1565C0)** — Azul intenso utilizado nos títulos, links e elementos de destaque. Transmite **confiança, estabilidade e foco**.
- **Secundária (#121212)** — Cor **escura** usada em fundos e áreas de contraste.
- **Apoio (#00C853)** — Verde vibrante que destaca **botões de ação** (como “CADASTRE-SE”), indicando **sucesso e positividade**.
- **Neutros (#616161 e #FFFFFF)** — Tons de **cinza médio e branco puro** para equilibrar o contraste e garantir boa legibilidade.
- **Feedback (#FF6D00 e #D32F2F)** — Tons de **laranja** e **vermelho** aplicados para **alertas e erros**.

![PALETA DE CORES](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/PALETA%20DE%20CORES.png)

**Tipografia**

- **Títulos:** _Archivo Black_ — Fonte, que reforça a **força e a presença** da marca.
- **Textos e rótulos:** _Montserrat_ — Moderna e legível, garantindo **clareza e leveza** no corpo do texto e nos campos de formulário.

![TIPOGRAFIA](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/TIPOGRAFIA.png)

**Ícones e Elementos Gráficos**

O logotipo em azul escuro sobre um círculo vermelho-alaranjado, simbolizando energia, determinação e superação — valores centrais da marca. Os ícones secundários, como o **ícone de exibição de senha**, seguindo a consistência visual do sistema.

![pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/docs/img/LOGO.PNG at main · ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/LOGO.PNG)

**Geral**

O design do GymFlow comunica **energia e modernidade**. A combinação de azul e verde reforça **confiança e progresso**, enquanto a tipografia e os ícones tornam a experiência **acessível e agradável**. O resultado é uma interface ideal para um app voltado à organização e acompanhamento de treinos.

**Cadastro**

![CADASTRO](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/CADASTRO.png)

**Login**

![LOGIN](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/LOGIN.png)

**Cadastro de Fichas**

![CADASTRO DE FICHAS DE TREINO](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/CADASTRO%20DE%20FICHAS.png)

**Estatisticas**

![ESTATISTICAS](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/ESTATISTICAS.png)

**Perfil do Usuário**

![PERFIL DO USUARIO](./img/PERFIL-DO-USUARIO.png)

**Home Page**

![HOME PAGE](https://github.com/ICEI-PUC-Minas-PMV-SI/pmv-si-2025-2-pe6-t2-si-2025-t2-gp3/raw/main/docs/img/HOME%20PAGE.png)

## Tecnologias Utilizadas

| Categoria                                  | Tecnologia / Ferramenta                                               |
| ------------------------------------------ | --------------------------------------------------------------------- |
| **Linguagem**                              | JavaScript, HTML                                                      |
| **Estilo e Layout**                        | CSS                                                                   |
| **Hospedagem**                             | Vercel                                                                |
| **Banco de Dados (via Backend)**           | PostgreSQL                                                            |
| **Integração com API**                     | Consumo de endpoints REST fornecidos pelo backend em Java/Spring Boot |
| **Controle de Versão**                     | GitHub (repositório remoto)                                           |
| **Containerização (infraestrutura geral)** | Docker                                                                |

Essas tecnologias garantem compatibilidade, desempenho e fácil integração com os demais módulos (mobile e backend).

## Considerações de Segurança

A aplicação web segue as diretrizes gerais de segurança estabelecidas no projeto, garantindo proteção dos dados e autenticação segura. Essas medidas asseguram a integridade e confidencialidade das informações do usuário durante a navegação e manipulação dos dados no sistema.


## Implantação
A aplicação web será implantada na plataforma Vercel, conforme especificado na arquitetura do projeto. O processo de implantação será realizado com base na integração contínua do repositório GitHub, permitindo deploy automatizado após atualizações no código.  

### Passos gerais de implantação:
1. Configuração do repositório remoto no GitHub.  
2. Conexão do repositório à conta Vercel.  
3. Build automático do front-end via pipeline da Vercel.  
4. Testes de funcionamento e integração com a API hospedada em Render ou Railway.  
5. Liberação para ambiente de produção.  


## Testes

### Casos de Teste

### 1. Página de Cadastro

### Funcionalidade: Cadastrar novo usuário

| Caso de Teste | Descrição                    | Entrada                       | Resultado Esperado                                                            | Print da Execução              | Status  |
| ------------- | ---------------------------- | ----------------------------- | ----------------------------------------------------------------------------- | ------------------------------ | ------- |
| CT-01         | Campos obrigatórios vazios   | Nome, email e senha em branco | Exibir popup “Preencha todos os campos!”                                      | ![print CT-01](img/fwct01.png) | Sucesso |
| CT-02         | Email inválido               | `usuario@teste`               | Exibir popup “Email inválido!”                                                | ![print CT-01](img/fwct02.png) | Sucesso |
| CT-03         | Senha menor que 4 caracteres | `123`                         | Exibir popup “A senha deve ter pelo menos 4 caracteres.”                      | ![print CT-03](img/fwct03.png) | Sucesso |
| CT-04         | Email já cadastrado          | Email existente no banco      | Exibir popup “Este email já está cadastrado!”                                 | ![print CT-04](img/fwct04.png) | Sucesso |
| CT-05         | Cadastro bem-sucedido        | Nome, email e senha válidos   | Exibir popup “Cadastro realizado com sucesso!” e registrar usuário no backend | ![print CT-05](img/fwct05.png) | Sucesso |

### 2. Página de Login

### Funcionalidade: Autenticar usuário

| Caso de Teste | Descrição                  | Entrada                  | Resultado Esperado                                             | Print da Execução              | Status  |
| ------------- | -------------------------- | ------------------------ | -------------------------------------------------------------- | ------------------------------ | ------- |
| CT-06         | Campos obrigatórios vazios | Email e senha em branco  | Exibir popup “Preencha todos os campos!”                       | ![print CT-06](img/fwct06.png) | Sucesso |
| CT-07         | Email inválido             | `usuario@teste`          | Exibir popup “Email inválido!”                                 | ![print CT-07](img/fwct07.png) | Sucesso |
| CT-08         | Credenciais incorretas     | Email ou senha inválidos | Exibir popup “Email ou senha inválidos!”                       | ![print CT-08](img/fwct08.png) | Sucesso |
| CT-09         | Login bem-sucedido         | Email e senha corretos   | Salvar token no localStorage e redirecionar para MenuPrincipal | ![print CT-09](img/fwct09.png) | Sucesso |

### 3. Página do Usuário

### Funcionalidade: Editar e salvar dados pessoais

| Caso de Teste | Descrição                | Entrada                         | Resultado Esperado                                     | Print da Execução | Status                                            |
| ------------- | ------------------------ | ------------------------------- | ------------------------------------------------------ | ----------------- | --------------------------------------------------|
| CT-10         | Editar campos vazios     | Nome em branco         | Exibir popup “Insira um nome.” | ![print CT-10](img/fwct10.png)                | sucesso |
| CT-11         | Valores inválidos        | peso ou altura negativos | Exibir popup “Insira um peso válido.”      | ![print CT-11](img/fwct11.png)                 | sucesso |
| CT-12         | Atualização bem-sucedida | Todos os campos válidos         | Exibir popup “Salvo com sucesso!”               | ![print CT-12](img/fwct12.png)             | sucesso |

### Funcionalidade: Metas do Usuário

| Caso de Teste | Descrição         | Entrada                    | Resultado Esperado                     | Print da Execução              | Status                                             |
| ------------- | ----------------- | -------------------------- | -------------------------------------- | ------------------------------ | -------------------------------------------------- |
| CT-13         | Criar meta vazia  | Campo de textos vazio       | Popup informando para preencher o campo em branco | ![print CT-13](img/fwct13.png) | Sucesso                                            |
| CT-14         | Criar meta válida | campos preenchidos          | Adicionar meta à lista                 | ![print CT-14](img/fwct14.png) | sucesso                                               |
| CT-15         | Editar Meta      | Clicar no ícone do pincel | edita meta no frontend e no backend     | ![print CT-15](img/fwct15.png)                              | sucesso |

### Funcionalidade: IMC do Usuário

| Caso de Teste | Descrição              | Entrada                 | Resultado Esperado          | Print da Execução | Status                                             |
| ------------- | ---------------------- | ----------------------- | --------------------------- | ----------------- | -------------------------------------------------- |
| CT-16         | Cálculo correto do IMC | Peso 70kg, Altura 170cm | Exibir IMC = 24.22 (Normal) | ![print CT-16](img/fwct16.png)                 | sucesso |

