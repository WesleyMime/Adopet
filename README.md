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

## Description

Adopet is a platform designed to connect people who want to adopt pets with shelters. The platform provides an 
intuitive interface and robust backend to streamline the adoption process, making it easier for shelters and adopters 
to find the perfect match.

The project consists of:
- A Spring Boot API that stores people and animal shelters with endpoints to access and modify the data, including endpoints
to access pets from a specific shelter, see only pets that aren't adopted yet and an endpoint to connect
animal shelters with people interested in their pets.
- An Angular application to navigate through the endpoints in a friendly way, taking care of the authentication with jwt and
sending some parameters automatically.

### Why
The final CS50x project needed to be something of my interest, that I solved an actual problem, that I impacted my community, 
or that I changed the world. So I thought that this idea of project was going to be perfect for that.


## Project objectives

The project was developed in sprints lasting 1 week each, which had certain activities to be implemented. 
For better management of activities, trello was used.

- [Sprint 1 Trello](https://trello.com/b/gQC25pZg/challenge-back-end-6-semana-1)
- [Sprint 2 Trello](https://trello.com/b/005pszqz/challenge-back-end-6-semana-2)
- [Sprint 3 Trello](https://trello.com/b/7Rcwmzcg/alura-challenge-back-end-6-semana-3-e-4)

## Key Features

- Built with **Spring Boot** for a robust and scalable backend.
- Frontend developed using **Angular** for a dynamic and responsive user experience.
- Fully containerized with **Docker** for simple deployment.
- Persistent data management with **PostgreSQL**.

## Technologies

![java]![spring]![postgresql]![docker]![angular]![google]![cloudflare]

### Thought Behind the Technologies Chosen

I really wanted to try building something with Angular, after a lot of tutorials and documentations on the internet,
I managed to build the front-end for the Spring API, and I am very proud of it.

The plan at first was going to have the API use authentication with OAuth and have the user login with GitHub and Google,
but after a lot of time trying to learn how to implement it, I decided to use JWT to handle authentication, with has a lot easier.

---

### Breakdown

#### API

- POSTS for `/auth`, `/tutores` and `/abrigos` don't need authentication.
- Requests for "`/tutores`" requires the user to have the role "tutor".
- The `/pets/adopt` endpoint is open to anyone to see the pets available.
- The `/abrigos/{id}` endpoint is allowed for the role "tutor" to get the contact information.
- The endpoints `/adocao`, `/pets`, `/abrigos` require the user to have the role "abrigo".
---
- The controllers use jakarta.validation annotations to make sure that all the information store is adequate;
- The API uses JPA to connect to the PostgreSQL database running inside a docker container;
- AbrigoEntity and TutorEntity extends UserEntity, that contains the properties email, password and authorities;
that facilitates the login process, having to search in one table for two possible entities, since the inheritance strategy
for the entities is of type JOINED.
- Entities use UUID as their ids, with makes very unlikely that someone guesses the id of another person, but can also be
inconvenient for the users when trying to get a entity by the id.
---

#### Entity Relationship diagram
![relationships real large](https://github.com/user-attachments/assets/0d9db12f-ee61-4bc0-90d5-eebddd3da4ee)

---

**GlobalControllerExceptionHandler.java** treats all the exceptions that can (or shouldn't) happen;  
**MapStructMapper.java** builds mappers automatically just based on method signatures, saving a lot of time;  
**EncodeDecorator.java** makes sure that all the entities with passwords, have them encoded before being saved;  
**SecurityConfig.java** takes care of the security of the API, with CORS configuration for only the Front-End to be able to connect,
provides authentication related @Beans and secure endpoints based on roles;  
**TokenService.java** validates the Json Web Tokens and generates then, sending the token with the subject (user email) and expiration date,
with the token also comes the expiration date again, user roles and the user id (to facilitate on the Front-end);  
**TokenAuthenticationFilter.java** is a filter that sees every request and makes sure that the users who sent the "Bearer token"
can access the secured endpoints.  

The API gets build for production using the command `./gradlew bootBuildImage`.

---

#### Front-end
- The design is based in this React project from this repository: https://github.com/sucodelarangela/adopet  
- The `/home` endpoint shows all the pets that are not adopted yet, and the user does not need to be authenticated.  
- To get the animal shelter's contact information, the user needs to be authenticated and have the role "tutor".  
- When the user gets authenticated and have the role "abrigo", it gets redirected to the `/pets` endpoint, that shows 
all the pets registered from that animal shelter, and allows it to register a new pet.  
- When the user gets authenticated and have the role "tutor", it gets redirected to `/home` endpoint.

**auth-interceptor.ts** makes sure that all the requests for the API have the "Authentication" header with the Bearer token;  
**abrigo-auth.guard.ts** and **tutor-auth.guard.ts** blocks users that doesn't have the "abrigo" and "tutor" roles;  
**login.service.ts** takes care of the authentication; sends the login requests to the API and stores the response in 
the local storage, checks users roles and logout the user, removing all the items in the local storage;

The Angular Front-end uses a Dockerfile to build.

---

### Future

Developing the Front-End gave me a lot of new ideas to add to the project that I ended up with no time left to finish it properly.  
I definitely will continue to develop the project to polish it and finish completely, like adding a page to show details of the
logged-in user and the ability to logout, and a feature to send messages directly to the animal shelter via the API.

## Deploy

The Angular app is running on GitHub Pages and the Spring Boot API is running on a Google Cloud Virtual Machine, 
with DNS and HTTPS from Cloudflare.

The links to access are:

- Angular application https://adopet.marujo.site
- Spring Boot API https://api.marujo.site

![backend-gif]

## Run locally


To run locally you need to have Docker installed.

- Clone project

```
git clone https://github.com/WesleyMime/Adopet.git
```

- Enter the project folder

```
cd adopet
```

- Start services

```
docker compose up
```

### Usage

The links to access are:

- Angular application http://localhost
- Spring Boot API http://localhost:8080

## License

Distributed under the MIT license. See `LICENSE.txt` for more information.

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
[postgresql]: https://img.shields.io/badge/MongoDB-47A248.svg?logo=mongodb&logoColor=white&style=for-the-badge
[docker]: https://img.shields.io/badge/docker-2496ED?logo=docker&logoColor=white&style=for-the-badge
[angular]: https://img.shields.io/badge/Angular-%23DD0031.svg?logo=angular&logoColor=white&style=for-the-badge
[google]: https://img.shields.io/badge/Google%20Cloud-%234285F4.svg?logo=google-cloud&logoColor=white&style=for-the-badge
[cloudflare]: https://img.shields.io/badge/Cloudflare-F38020?logo=Cloudflare&logoColor=white&style=for-the-badge