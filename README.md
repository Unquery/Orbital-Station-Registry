# Project Title — Spring Boot • H2 • Hibernate • Thymeleaf

Small CRUD web app using Spring Boot, Hibernate (JPA), **H2** database, and Thymeleaf.

## Run

```bash
mvn spring-boot:run
```

* App: [http://localhost:8081](http://localhost:8081)
* H2 Console: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)

  * JDBC URL: `jdbc:h2:file:./db/Spring_Implementation`
  * User: `sa` (no password)
--- 
## H2 config (edit `src/main/resources/application.properties`)

```properties
# -- H2 in-memory (default) --
spring.application.name=Spring_Implementation
spring.datasource.url=jdbc:h2:file:./db/Spring_Implementation
spring.jpa.generate-ddl=true
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

> If you ever change to another database later, remember to update all `spring.datasource.*` entries (and optionally the Hibernate dialect).
--- 
## Maven dep (already in project, for reference)

```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
  <scope>runtime</scope>
</dependency>
```
--- 
## Features 

* Spring MVC + Thymeleaf views
* JPA entities & repositories (Hibernate)
* H2 dev DB (file)

--- 

## Diagrams (paste your images)

* **Diagram analityczny i projektowy** — (Overview of the whole program)
  
* ***System analysis diagram***
<img width="7751" height="4450" alt="DiagramAnalityczny" src="https://github.com/user-attachments/assets/ffc833b6-83a3-439d-9df2-dacfce5c79b1" />

  
* ***Architectural design diagram***
<img width="4425" height="2573" alt="DiagramProjektowy" src="https://github.com/user-attachments/assets/503e71b9-3074-437c-94af-7bd0416b6c85" />


* **Activity diagram** — (Flow of the site built with Thymeleaf + HTML)

<img width="4125" height="7246" alt="ad Stworzenie misji" src="https://github.com/user-attachments/assets/9b829027-7601-4b8f-8a87-4e86991908cd" />

