# spring-petclinic-reactive

Implementation of the Spring Pet Clinic using the DataStax Reactive driver and Spring Weblux

**This read is really still *work in progress***


## Understanding the Spring Petclinic application with a few diagrams

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)


## BUILD AND RUN THE UID LOCALLY

```
git clone https://github.com/spring-petclinic/spring-petclinic-angular.git
cd spring-petclinic-angular
npm uninstall -g angular-cli @angular/cli
npm cache clean
npm install -g @angular/cli@8.0.3
npm install --save-dev @angular/cli@8.0.3
```

You should now have the UI running at [http://localhost:4200](http://localhost:4200)


## BUILD AND RUN THE BACKEND

- Create Astra account + DB
- Download the cloudSecureBundle
- Setup `application.yaml` with `keyspace-name`, `username`, `password` and `secure-connect-bundle`

```yaml
spring.data.cassandra:
  keyspace-name: spring_petclinic
  username: petclinic
  password: petclinic
  schema-action: CREATE_IF_NOT_EXISTS
  request:
    timeout: 10s
  connection:
    connect-timeout: 10s
    init-query-timeout: 10s

datastax.astra:
  enabled: true
  secure-connect-bundle: /Users/cedricklunven/Downloads/secure-connect-demos.zip

```

## Run with local Cassandra

```
docker-compose up -d
```

Access application, it should redirect you to documentation
```
http://localhost:8081/
```

## Run with ASTRA

TBD