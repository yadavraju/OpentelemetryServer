# Project Demonstrating Observability For Java Spring Applications

This project demonstrates Observability using:

* [Opentelemetry](https://opentelemetry.io/)
* [Spring Boot Project](https://spring.io/projects/spring-boot)

Note : Change exporter end point in Docker file of tempo-api and tempo-provider1

Also change new relic key if you want inside new-relic-exporter project (this is optional Currently I am using my personal new relic account )

-change this http://192.168.0.102 to your local computer ip 

-Dotel.exporter.otlp.endpoint=http://192.168.0.102:4317 \

# Running

Execute the following on root folder

````bash
mvn clean package docker:build
````

Images

````bash
docker image ls

````

````bash
REPOSITORY                                                      TAG                 IMAGE ID            CREATED              SIZE
mnadeem/boot-otel-tempo-provider1                               0.0.1-SNAPSHOT      7ddceebcc722        About a minute ago   169MB
mnadeem/boot-otel-tempo-api                                     0.0.1-SNAPSHOT      a301242388a1        2 minutes ago        147MB
mnadeem/boot-otel-tempo-docker                                  0.0.1-SNAPSHOT      061a20db744b        4 minutes ago        130MB
````

And then either `docker compose` or `docker stack`

## Docker Compose

````bash
docker-compose -f docker-compose.yaml up
````
Execute the following on root folder


## Basic

Multiple micro-services with **db** and **rest** interactions

# Tracing

[Access the endpoint](http://localhost:8080/flights)

![](docs/img/access-flights.png)

**Basic Trace**

![](docs/img/jaeger-trace.png)

# Connecting To PostgreSQL DB

[Connect](http://localhost:7070/login?next=%2F)

![](docs/img/pgAdminlogin.png)

![](docs/img/pgAdmingServer.png)

![](docs/img/pgAdminDb.png)

# Client Project Android
[Connect](https://github.com/yadavraju/DisneyCodeChallenge)