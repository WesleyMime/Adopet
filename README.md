<h1 align="center"> Adopet </h1>
[![Video Demo:](https://img.youtube.com/vi/Hrt1_oqMih8/0.jpg)] (https://www.youtube.com/watch?v=Hrt1_oqMih8 "Project CS50x Adopet")

## About the project

A platform to connect animal shelters and people looking to adopt pets.

The final CS50x project needed to be something of my interest, that I solved an actual problem, that I impacted my community, 
or that I changed the world. <br> So I thought that this idea of project was going to be perfect for that.

The project consists of: <br>
A Spring Boot API that stores people and animal shelters with endpoints to access and modify the data, including endpoints 
to access pets from a specific shelter, see only pets that aren't adopted yet and an endpoint to connect 
animal shelters with people interested in their pets. <br>
An Angular application to navigate through the endpoints in a friendly way, taking care of the authentication with jwt and
sending some parameters automatically.

## Project objectives

The project was developed in sprints lasting 1 week each, which had certain activities to be implemented. 
For better management of activities, trello was used.

- [Sprint 1 Trello](https://trello.com/b/gQC25pZg/challenge-back-end-6-semana-1)
- [Sprint 2 Trello](https://trello.com/b/005pszqz/challenge-back-end-6-semana-2)
- [Sprint 3 Trello](https://trello.com/b/7Rcwmzcg/alura-challenge-back-end-6-semana-3-e-4)

## Technologies

The programming language, frameworks and technologies were of free choice. I chose to develop the project with the following technologies:

<img alt="Java" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original-wordmark.svg" width="50" height="50" /> <img alt="Spring" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/spring/spring-original-wordmark.svg" width="50" height="50" /> <img alt="PostgreSQL" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postgresql/postgresql-original-wordmark.svg" width="50" height="50"/> <img alt="Docker" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/docker/docker-plain-wordmark.svg" width="50" height="50" /> <img alt="Angular" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg" width="50" height="50" />

I really wanted to try building something with Angular, after a lot of tutorials and documentations on the internet,
I managed to build the front-end for the Spring API, and I am very proud of it.

The plan at first was going to have the API use authentication with OAuth and have the user login with GitHub and Google, <br>
but after a lot of time trying to learn how to implement it, I decided to use JWT to handle authentication, with has a lot easier.

## Breakdown

<h4> API </h4>
POSTS for "/auth", "/tutores" and "/abrigos" don't need authentication.<br>
Requests for "/tutores" requires the user to have the role "tutor".<br>
The "/pets/adopt" endpoint is open to anyone to see the pets available.<br>
The "/abrigos/{id}" endpoint is allowed for the role "tutor" to get the contact information. <br>
The endpoints "/adocao", "/pets", "/abrigos" require the user to have the role "abrigo".<br>

The controllers use jakarta.validation annotations to make sure that all the information store is adequate; <br>
The API uses JPA to connect to the PostgreSQL database running inside a docker container; <br>
AbrigoEntity and TutorEntity extends UserEntity, that contains the properties email, password and authorities; <br>
that facilitates the login process, having to search in one table for two possible entities, since the inheritance strategy <br>
for the entities is of type JOINED. <br>
Entities use UUID as their ids, with makes very unlikely that someone guesses the id of another person, but can also be <br>
inconvenient for the users when trying to get a entity by the id. <br>

![relationships real large](https://github.com/user-attachments/assets/0d9db12f-ee61-4bc0-90d5-eebddd3da4ee)

<b>GlobalControllerExceptionHandler.java</b> controls treats all the exceptions that can (and shouldn't) happen; <br>
<b>MapStructMapper.java</b> builds mappers automatically just based on method signatures, saving a lot of time; <br>
<b>EncodeDecorator.java</b> makes sure that all the entities with passwords, have them encoded before being saved; <br>
<b>SecurityConfig.java</b> takes care of the security of the API, with CORS configuration for only the Front-End to be able to connect, <br>
provides authentication related @Beans and secure endpoints based on roles; <br>
<b>TokenService.java</b> validates the Json Web Tokens and generates then, sending the token with the subject (user email) and expiration date,
with the token also comes the expiration date again, user roles and the user id (to facilitate on the Front-end); <br>
<b>TokenAuthenticationFilter.java</b>> is a filter that sees every request and makes sure that the users who sent the "Bearer token"<br>
can access the secured endpoints. <br>
The API gets build for production using the command <b>./gradlew bootBuildImage</b>.

<h4> Front-end </h4>
The design is based in this React project from this repository: https://github.com/sucodelarangela/adopet <br>
The "/home" endpoint shows all the pets that are not adopted yet, and the user does not need to be authenticated. <br>
To get the animal shelter's contact information, the user needs to be authenticated and have the role "tutor". <br>
When the user gets authenticated and have the role "abrigo", it gets redirected to the "/pets" endpoint, that shows all the pets <br>
registered from that animal shelter, and allows it to register a new pet. <br>
When the user gets authenticated and have the role "tutor", it gets redirected to "/home" endpoint. <br>


<b>auth-interceptor.ts</b> makes sure that all the requests for the API have the "Authentication" header with the Bearer token;<br>
<b>abrigo-auth.guard.ts</b> and <b>tutor-auth.guard.ts</b> blocks users that doesn't have the "abrigo" and "tutor" roles; <br>
<b>login.service.ts</b> takes care of the authentication; sends the login requests to the API and stores the response in the local storage, <br>
checks users roles and logout the user, removing all the items in the local storage; <br>
The Angular Front-end uses a Dockerfile to build.

## Future

Developing the Front-End gave me a lot of new ideas to add to the project that I ended up with no time left to finish it properly. <br>
I definitely will continue to develop the project to polish it and finish completely, like adding a page to show details of the
logged-in user and the ability to logout, <br> and a feature to send messages directly to the animal shelter via the API.

## Deploy

To run locally you need to have Docker installed.

- Open the terminal and clone the project using the command
  ```git clone https://github.com/WesleyMime/Adopet.git```

- Enter the "adopet" folder and use the command "docker compose up".

If everything is working, the links to access are:
- Angular application http://localhost/
- Spring API http://localhost:8080/
