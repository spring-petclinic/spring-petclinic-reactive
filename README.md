# spring-petclinic-reactive
Implementation of the Spring Pet Clinic using Spring Data Cassandra and Spring Weblux

Start the cassandra db

```
docker-compose up -d
```

After a few second create the keyspace
```
docker exec -it `docker ps | grep cassandra:3.11.7 | cut -b 1-12` cqlsh -e "create keyspace petclinic WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 3 };"
```

Start the App
```
mvn spring-boot:run
```

Access application, it should redirect you to documentation
```
http://localhost:8081/
```

