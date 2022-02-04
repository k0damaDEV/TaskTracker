### Badges:

[![Actions Status](https://github.com/k0damaDEV/java-project-lvl5/workflows/hexlet-check/badge.svg)](https://github.com/k0damaDEV/java-project-lvl5/actions)
<a href="https://codeclimate.com/github/k0damaDEV/java-project-lvl5/maintainability"><img src="https://api.codeclimate.com/v1/badges/9aefc5ec56702b23013b/maintainability" /></a>
<a href="https://codeclimate.com/github/k0damaDEV/java-project-lvl5/test_coverage"><img src="https://api.codeclimate.com/v1/badges/9aefc5ec56702b23013b/test_coverage" /></a>

<b>Description:</b> training project written with Spring Boot. Main goal - make task tracker with statuses, labels etc.

[API available here](https://murmuring-lowlands-34103.herokuapp.com/swagger-ui.html)

<b>Configuration:</b> located in src/main/resources/config. application-{env}.properties, where {env} - name of the environment(dev, prod). General configuration located in src/main/resources/config/application.properties.

<b>Launch:</b> to start project in development purposes:

```sh
git clone https://github.com/k0damaDEV/TaskTracker.git project
cd project
make start
```

By default, application will start at 4000 port. You can change it by assigning environment variable PORT.

[Project Example on Heroku](https://murmuring-lowlands-34103.herokuapp.com/) - feel free to play with it!

