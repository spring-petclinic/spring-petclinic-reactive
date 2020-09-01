# Reactive implementation of 🐈 Spring PetClinic 🐕 

[![Build Status](https://travis-ci.org/ff4j/ff4j.svg?branch=master)](https://travis-ci.org/clun/spring-petclinic-reactive)
![Fun](https://img.shields.io/badge/Build_with-Fun-blacke.svg?style=flat)
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive) 
![Java](https://img.shields.io/badge/-Java-black.svg?style=falt&logo=java)
![Cassandra](https://img.shields.io/badge/-Cassandra-black.svg?style=flat&logo=apache-cassandra)

Implementation of the [Spring Pet Clinic]  backend using a REST backend implented with **Spring Weblux** and **Apache Cassandra** for the backend. This is meant to be used with the **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface, all steps are listed here to run the full app stay with us.

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/banner.png)

## Table of content

- **(A) Run the Application on your laptop *(java + maven required)***
  - [Create a DBaas Cassandra free Account (ASTRA)](#a1---create-a-dbaas-cassandra-free-account-astra)
  - [Build and start the backend](#a2)
  - [Build and start the user interface](##a3---build-and-start-the-angular--user-interface)
  - [Setup the application to run Apache Cassandra locally (docker)](#)

- **(B) Run the Sample on Gitpod**
  - [Create a DBaas Cassandra free Account (ASTRA)]()
  - [Open Gitpod and follow instructions](#)

- **(C) Architecture**
  - [Understanding the Pet Clinic Application](#)
  - [Architecture diagram of the version](#)
  - [Data Model from Relational to Cassandra data Model](#)

- **(D) Contribute**
  - [Blog post and communications](#)
  - [Architecture diagram of the version](#)
  - [Data Model from Relational to Cassandra data Model](#)

## Run the Application on your laptop

### A1 - Create a DBaas Cassandra free Account (ASTRA)

`DataStax ASTRA` service is available at url [https://astra.datastax.com](https://astra.datastax.com/)

**✅ Step 1a.Register (if needed) and Sign In to Astra** : You can use your `Github`, `Google` accounts or register with an `email`

- [Registration Page](https://astra.datastax.com/register)

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-register.png?raw=true)

- [Authentication Page](https://astra.datastax.com/)

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-login.png?raw=true)


**✅ Step 1b. Fill the Create New Database Form** : As you don't have have any instances the login will route through the instance creation form. You will find below which values to enter for each field.

- *Initialization Form*
![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-2.png?raw=true)

- **Set the Compute Size**: For the work we are doing please use `Free tier`. You instance will be there forever, free of charge. 

- **Select region**: This is the region where your database will reside physically (choose one close to you or your users). For people in EMEA please use `europe-west-1` idea here is to reduce latency.

- **Fill in the database name** - Proposed value `petclinicdb`. You can use any alphanumeric value it is not part of the connection fields. Now it will be part of a file downloaded later and you should avoid capital letters.

With the 3 fields below you can pick any name

- **Fill in the keyspace name** - Proposed value `spring_petclinic` (no spaces, alpha numeric)

- **Fill in the user name** - `petclinic`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the user password** - `petclinic`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

- **Launch the database**. Review all the fields to make sure they are as shown, and click the Launch Database button.

**👁️ Expected output**

![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-3.png?raw=true)

**✅ Step 1c. View your Database and connect** : View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point.

**👁️ Expected output**

*Initializing*
![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-4.png?raw=true)

*Database is ready*
![TodoBackendClient](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-5.png?raw=true)


### A2 - Build and start the Spring Boot backend

**✅ Download the cloud secure bundle** : UI

**✅ Setup the configuration file** : In the folder `src/main/resources` locate the file `application.yaml` and edit the following key

- `keyspace-name`: Name of the keyspace as stated in the user interface
- `username`: user credentials
- `password`: user credentials
- `password`: user credentials
- `secure-connect-bundle` : path of the zip you downloaded

*Sample file*
```yaml
server:
  port: ${PORT:9966}
spring.data.cassandra:
  keyspace-name: spring_petclinic
  username: petclinic
  password: petclinic
  schema-action: CREATE_IF_NOT_EXISTS
  contact-points: localhost
  port: 9042
  local-datacenter: datacenter1
  astra:
    enabled: true
    secure-connect-bundle: /Users/cedricklunven/Downloads/secure-connect-demos.zip
```

**✅ Start the application** : You can now run the application `mvn spring-boot:run` il will create the table needed. You should now be able to access

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-swagger.png)

### A3 - Build and start the Angular  user interface

As stated before we leverage on the **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. 

```bash
git clone https://github.com/spring-petclinic/spring-petclinic-angular.git
cd spring-petclinic-angular
npm uninstall -g angular-cli @angular/cli
npm cache clean
npm install -g @angular/cli@8.0.3
npm install --save-dev @angular/cli@8.0.3
ng build
ng serve
```

You should now have the UI running at [http://localhost:4200](http://localhost:4200)
![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)

- Owners
![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-owners.png)

- Pet Types
![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-pettypes.png)

- Vet Specialties
![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-specialties.png)

- Veterinians
![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-veterinians.png)



