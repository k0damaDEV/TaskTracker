## Badges:

[![Actions Status](https://github.com/k0damaDEV/java-project-lvl5/workflows/hexlet-check/badge.svg)](https://github.com/k0damaDEV/java-project-lvl5/actions)
<a href="https://codeclimate.com/github/k0damaDEV/java-project-lvl5/maintainability"><img src="https://api.codeclimate.com/v1/badges/9aefc5ec56702b23013b/maintainability" /></a>
<a href="https://codeclimate.com/github/k0damaDEV/java-project-lvl5/test_coverage"><img src="https://api.codeclimate.com/v1/badges/9aefc5ec56702b23013b/test_coverage" /></a>

## Description

Training project written with Spring. Main goal - make task tracker with statuses, labels, users.

## API

[Click here](https://murmuring-lowlands-34103.herokuapp.com/swagger-ui.html)

## Java version

* Java 17

## Configuration:

Located in `src/main/resources/config/application-{env}.properties`, where `{env}` - name of the environment(dev, prod). General configuration located in `src/main/resources/config/application.properties`.

Available to config in application.properties:

* `spring-profiles-active` - active profile(dev/prod)
* `server.port` - port which will be used by application(4000 by default)

Available to config in application-prod.properties:

* `springdoc.swagger-ui.path` - path to page with API(Swagger)
* `spring.datasource.url` - production database address

Available to config in application-dev.properties:

* `spring.datasource.url` - development database address 
* `spring.datasource.username` - username for database
* `spring.datasource.password` - password for database
* `spring.h2.console.path` - path to H2 database(if used). Default - `/h2console/`

## Launch

* To start project in development purposes:

```sh
git clone https://github.com/k0damaDEV/TaskTracker.git project
cd project
make start-dev
```

```sh
git clone https://github.com/k0damaDEV/TaskTracker.git project
cd project
make start-prod
```

Make sure you have configured properties files before you start application.
By default, application will start at 4000 port. You can change it by assigning environment variable PORT.

[Project Example on Heroku](https://murmuring-lowlands-34103.herokuapp.com/) - feel free to play with it!

