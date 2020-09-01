# Reactive implementation of 🐈 Spring PetClinic 🐕 

[![Build Status](https://travis-ci.org/ff4j/ff4j.svg?branch=master)](https://travis-ci.org/clun/spring-petclinic-reactive)
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive) 

Implementation of the Spring Pet Clinic REST backend using the **Spring Weblux** for controllers and **Apache Cassandra** for the backend. This is meant to be used with the **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface, all steps are listed here to run the full app stay with us.

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/logos-reactives.png)

## Table of content

- (A) Run the Application on your laptop *(java + maven required)*
  - Create a DBaas Cassandra free Account (ASTRA)
  - Build and start the user interface
  - Build and start the backend
  - Setup the application to run Apache Cassandra locally (docker)

- (B) Run the Sample on Gitpod
  - Create a DBaas Cassandra free Account (ASTRA)
  - Open Gitpod and follow instructions

- (C) Architecture
 - Understanding the Pet Clinic Application
 - Architecture diagram of the version
 

## Run the Application on your laptop

Talk is cheap, show me the code.


**This read is really still *work in progress***


## Understanding the Spring Petclinic application with a few diagrams

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

You should now have the backedn running at [http://localhost:9966](http://localhost:9966)
