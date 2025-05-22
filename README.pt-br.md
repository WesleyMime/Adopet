<div align="center">
<h1> Adopet </h1>

[![en][en-shield]][en-url]
[![pt-br][pt-br-shield]][pt-br-url]
[![project_license][license-shield]][license-url]
[![last-commit][commit-shield]][commit-url]
![workflow][workflow-shield]
![deploy-status]
</div>

![frontend-gif]

## Descrição

Adopet é uma plataforma projetada para conectar pessoas que desejam adotar animais de estimação com abrigos.
A plataforma fornece uma interface intuitiva e um Back-End robusto para otimizar o processo de adoção, facilitando 
os abrigos e os adotantes encontrarem a combinação perfeita.

O projeto consiste em:
- Uma API Spring Boot que armazena pessoas e abrigos de animais com endpoints para acessar e modificar os dados, 
incluindo endpoints para acessar animais de estimação de um abrigo específico, consulta de apenas animais que ainda 
não foram adotados e um endpoint para conectar abrigos com pessoas interessadas em seus animais.
- Uma aplicação Angular para navegar pelos endpoints de uma maneira amigável, cuidando da autenticação com JWT e
enviando alguns parâmetros automaticamente.

### Porquê
O projeto final do CS50x precisava ser alguma coisa do meu interesse, que eu solucionasse um problema real, que eu
impactasse minha comunidade, ou que eu mudasse o mundo. Então eu pensei que essa ideia de projeto fosse perfeita para isso.


## Objetivos do Projeto

O projeto foi desenvolvido em sprints com duração 1 semana cada, que possuiam determinadas atividades a serem implementadas.
Para uma melhor gestão das atividades, foi utilizado o trello como ferramenta.

- [Sprint 1 Trello](https://trello.com/b/gQC25pZg/challenge-back-end-6-semana-1)
- [Sprint 2 Trello](https://trello.com/b/005pszqz/challenge-back-end-6-semana-2)
- [Sprint 3 Trello](https://trello.com/b/7Rcwmzcg/alura-challenge-back-end-6-semana-3-e-4)

## Características principais

- Construído com **Spring Boot** para um backend robusto e escalável.
- Frontend desenvolvido utilizando **Angular** para uma experiência de usuário dinâmica e responsíva.
- Totalmente contêinerzado com **Docker** para deploy simples.
- Armazenamento de dados persistente com **PostgreSQL**.

## Tecnologias

![java] ![spring] ![postgresql] ![docker] ![angular] ![google] ![cloudflare]

### Pensamento Por Trás das Tecnologias Escolhidas

Eu realmente queria tentar construír algo com Angular, depois de muitos tutoriais e documentações na internet, eu
consegui construir o Front-end para a API Spring, e eu estou orgulhoso disso.

O plano no começo era ter a API utilizando autenticação com OAuth e ter o usuário logar com GitHub e Google, mas depois
de muito tempo tentando aprender como implementar, eu decidi utilizar JWT para a autenticação, que foi muito mais fácil.

---

### Breakdown

#### API

- POSTS para `/auth`, `/tutores` e `/abrigos` não precisam de autenticação.
- Requisições para "`/tutores`" requerem o usuário ter o cargo "tutor".
- O endpoint `/pets/adopt` é aberto para qualquer um ver os animais disponíveis.
- O endpoint `/abrigos/{id}` é permitido para o cargo "tutor" pegar as informações de contato.
- Os endpoints `/adocao`, `/pets`, `/abrigos` requerem o usuário ter o cargo "abrigo".
---
- Os controllers usam anotações jakarta.validation para garantir que toda informação guardada é adequada;
- A API usa JPA para conectar ao banco de dados PostgreSQL rodando em um container Docker;
- AbrigoEntity e TutorEntity estendem UserEntity, que contém as propriedades email, password e authorities;
que facilíta o processo de login, tendo que procurar numa tabela para duas entidades possíveis, já que a estratégia de
herança para as entidades é do tipo JOINED.
- Entidades usam UUID como seus ids, o que faz muito improvável que alguém adivinhe o id de outra pessoa, mas também
pode ser inconveniente para os usuários quando querem pegar uma entidade pelo seu id.
---

#### Diagrama de Relacionamento de Entidades
![relationships real large](https://github.com/user-attachments/assets/0d9db12f-ee61-4bc0-90d5-eebddd3da4ee)

---

**GlobalControllerExceptionHandler.java** trata todas as exceções que podem (ou não devem) acontecer;  
**MapStructMapper.java** constrói mappers automaticamente com base nas assinaturas de métodos, economizando muito tempo;  
**EncodeDecorator.java** garante que todas as entidades com senhas tenham-nas codificadas antes de serem salvas;  
**SecurityConfig.java** cuida da segurança da API, com a configuração do CORS para apenas o front-end poder se conectar, 
fornece @Beans relacionados à autenticação e endpoints seguros com base em cargos;  
**TokenService.java** valida os Json Web Tokens e os gera, enviando o token com o subject (email do usuário) e data de validade,
com o token, também vem a data de validade novamente, os cargos e o ID do usuário (para facilitar o Front-end);  
**TokenAuthenticationFilter.java** é um filtro que ve todas as requisições e garante que os usuários que mandam o "Bearer token"
podem acessar os endpoints seguros.  

A API é preparada para produção usando o comando `./gradlew bootBuildImage`.

---

#### Front-end
- O design está baseado no projeto React do GitHub: https://github.com/sucodelarangela/adopet
- O endpoint `/home` exibe todos os animais não adotados, e o usuário não precisa ser autenticado.
- Para obter as informações de contato do abrigo de animais, o usuário precisa ser autenticado e tenha o cargo "tutor".
- Quando o usuário for autenticado e tenha o cargo "abrigo", será redirecionado para o endpoint `/pets`, que exibe 
todos os animais registados pelo abrigo e permite a cadastrar um novo animal.
- Quando o usuário for autenticado e tenha o cargo "tutor", será redirecionado para o endpoint `/home`.

**auth-interceptor.ts** garante que todas as requisições para a API tenham o cabeçalho "Autenticação" com o token Bearer;  
**abrigo-auth.guard.ts** e **tutor-auth.guard.ts** interceptam os usuários que não possuem os cargos de "abrigo" e "tutor";
**login.service.ts** lida com a autenticação; envia as requisições para a API e armazena a resposta na memória local, 
verifica os cargos dos usuários e cancelar a autenticação, removendo todos os itens da memória local;

O Front-end Angular usa um Dockerfile para a build.

---

### Futuro

Desenvolver o Front-end me deu muitas ideias para adicionar ao projeto que não tive tempo para completá-lo propriamente.  
Definitivamente vou continuar o desenvolvimento do projeto para polí-lo e terminá-lo completamente, como adicionando uma 
página para mostrar detalhes do usuário autenticado e a capacidade de fazer logout, além de uma feature para enviar 
mensagens diretamente ao abrigo de animais via API.

## Deploy

A aplicação Angular está rodando no GitHub Pages e a API Spring Boot está rodando numa Máquina Virtual do Google Cloud,
com DNS e HTTPS da Cloudflare.

Os links para acesso são:

- Aplicação Angular https://adopet.marujo.site
- API Spring Boot https://api.marujo.site

![backend-gif]

## Rode localmente


Para rodar é necessário ter Docker instalado.

- Clone o projeto

```
git clone https://github.com/WesleyMime/Adopet.git
```

- Entre na pasta do projeto

```
cd adopet
```

- Inicie os serviços

```
docker compose up
```

### Uso

Os links para acesso são:

- Aplicação Angular http://localhost
- API Spring Boot http://localhost:8080

## Licença

Distribuído sob a licença do MIT. Consulte `LICENSE.txt` para obter mais informações.

[frontend-gif]: /adopetApp/src/assets/frontend.gif
[backend-gif]: /adopetApp/src/assets/backend.gif
[en-shield]: https://img.shields.io/badge/lang-en-green.svg?style=for-the-badge
[en-url]: https://github.com/WesleyMime/adopet/blob/main/README.md
[pt-br-shield]: https://img.shields.io/badge/lang-pt--br-lightdarkgreen.svg?style=for-the-badge
[pt-br-url]: https://github.com/WesleyMime/adopet/blob/main/README.pt-br.md
[commit-shield]: https://img.shields.io/github/last-commit/wesleymime/adopet.svg?style=for-the-badge
[commit-url]: https://github.com/wesleymime/adopet/commit
[license-shield]: https://img.shields.io/github/license/wesleymime/adopet.svg?style=for-the-badge
[license-url]: https://github.com/wesleymime/adopet/blob/master/LICENSE.txt
[workflow-shield]: https://img.shields.io/github/actions/workflow/status/wesleymime/adopet/.github/workflows/main.yml?style=for-the-badge
[workflow-url]: https://img.shields.io/github/actions/workflow/status/wesleymime/adopet/.github/workflows/main.yml
[deploy-status]: http://167.234.233.130:3001/api/badge/1/status?upColor=lightdarkgreen&style=for-the-badge

[java]: https://img.shields.io/badge/Java-000000?logo=openjdk&logoColor=white&style=for-the-badge
[spring]: https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff&style=for-the-badge
[postgresql]: https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[docker]: https://img.shields.io/badge/docker-2496ED?logo=docker&logoColor=white&style=for-the-badge
[angular]: https://img.shields.io/badge/Angular-%23DD0031.svg?logo=angular&logoColor=white&style=for-the-badge
[google]: https://img.shields.io/badge/Google%20Cloud-%234285F4.svg?logo=google-cloud&logoColor=white&style=for-the-badge
[cloudflare]: https://img.shields.io/badge/Cloudflare-F38020?logo=Cloudflare&logoColor=white&style=for-the-badge
