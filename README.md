# spring-petclinic-reactive

Implementation of the Spring Pet Clinic using the DataStax Reactive driver and Spring Weblux

**This read is really still *work in progress***


## Understanding the Spring Petclinic application with a few diagrams

[See the presentation of the Spring Petclinic Framework version](http://fr.slideshare.net/AntoineRey/spring-framework-petclinic-sample-application)


### Petclinic Cassandra Model

![alt petclinic-ermodel](petclinic-ermodel.png)


## Build

```
docker-compose build
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