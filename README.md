# Reactive implementation of 🐈 Spring PetClinic 🐕 

[![Build Status](https://travis-ci.org/ff4j/ff4j.svg?branch=master)](https://travis-ci.org/clun/spring-petclinic-reactive)
![Fun](https://img.shields.io/badge/Build_with-Fun-blacke.svg?style=flat)
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive) 
![Java](https://img.shields.io/badge/-Java-black.svg?style=falt&logo=java)
![Cassandra](https://img.shields.io/badge/-Cassandra-black.svg?style=flat&logo=apache-cassandra)

Implementation of the [Spring Pet Clinic] backend including a REST API implemented with **Spring Weblux** and **Apache Cassandra** for the backend. This is meant to be used with the **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. All required steps to run the full application are provided here.

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/banner.png)

## Table of contents

- **(A) [Run the APPLICATION](#run-the-petclinic-application)**
 - [Get the Database running](#1-get-the-database-running) (*using DataStax Astra*)
 - [Get your credentials](#2-copy-database-credentials) (*copied from the UI*)
 - [Setup and run the application on gitpod ](#3-start-in-gitpod) (*no installation required*)
 - [(alternative) Setup and run the application on your laptop](#4-start-locally) (*maven+npm required*)

- **(B) Architecture**
  - [Understanding the Pet Clinic Application](#)
  - [Architecture diagram](#)
  - [Data Model diagram](#)

- **(C) Contribute**
  - [Blog post and communications](#)
  - [Architecture diagram](#)
  - [Data Model diagram](#)

## Run the PetClinic Application

### 1. Get the Database running

**✅ Create an FREE FOREVER Apache Cassandra DBaas instance** : 

You can sign in with your `Github` or `Google` account or sign up with an `email`

- [REGISTER HERE](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive) 🚀

![Astra Registration Screen](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-register.png?raw=true)


**✅ Use the form to create New database** : 

If you don't have an existing instance,  login process will route automatically to the database creation form. You will find below which values to enter for each field.

![Astra Database Creation Form](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-2.png?raw=true)

- **Set the Compute Size**: For your first Astra database, we recommend using the `Free tier`. You instance will be there forever, free of charge. 

- **Select region**: This is the region where your database will reside physically (choose one close to you or your users to reduce latency). For example, users in EMEA might choose `europe-west-1`.

- **Fill in the database name** - Suggested value: `petclinicdb`. You can use any alphanumeric value. While you will not need to provide this name in order to connect to your database, the database name will be included in the name of a file you download later, so we recommend using all lower case letters for simplicity.

For the 3 fields below you can pick whatever text you desire:

- **Fill in the keyspace name** - Suggested value:  `spring_petclinic` (no spaces, alpha numeric)

- **Fill in the user name** - `petclinic`. Note the user name is case-sensitive. Please use the case we suggest here.

- **Fill in the user password** - `petclinic`. Fill in both the password and the confirmation fields. Note that the password is also case-sensitive. Please use the case we suggest here.

- **Launch the database**. Review all the fields to make sure they are as shown, and click the Launch Database button.

**👁️ Expected output**

![Database launching popup](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-3.png?raw=true)

**✅ View your Database and connect** : View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point.

**👁️ Expected output**

*Initializing*
![Initializing Screen](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-4.png?raw=true)

*Database is ready*
![Database ready screen](https://github.com/DataStax-Academy/microservices-java-workshop-online/blob/master/z-materials/images/astra-create-5.png?raw=true)

### 2. Copy Database credentials
   
- Click `Your Databases`, then `Manage Organizations` on the top navigation: 

![image](https://user-images.githubusercontent.com/3254549/90944069-9f63a880-e3d1-11ea-834a-968ffe69e37b.png)

- On the right side of your organization, click the elipsis (...) then `Manage Organizations`:

![image](https://user-images.githubusercontent.com/3254549/90944096-c02bfe00-e3d1-11ea-9513-b3362cdfd77a.png)

- Click the action menu, then select 'Add Service Account':

![image](https://user-images.githubusercontent.com/3254549/90944155-05503000-e3d2-11ea-9d2a-8c376b027358.png)

- Click the copy icon to copy your Astra service account credentials to your clipboard:

![image](https://user-images.githubusercontent.com/3254549/90944221-3c264600-e3d2-11ea-9d04-46915f1c3731.png)

This value copie looks like the following JSON:
```json
{ 
  "clientId":"149de2c7-9b07-41b3-91ad-9453dee4dc54",
  "clientName":"cedrick.lunven@datastax.com",
  "clientSecret":"aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
}
```

### 3. Start in Gitpod

- With the credentials in the copy board you can now open gitpod

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive)

- Once your Gitpod workspace has loaded, you'll be asked to paste your service account credentials in the Gitpod terminal at the bottom of the screen:

![image](https://user-images.githubusercontent.com/3254549/90944321-e900c300-e3d2-11ea-9624-dae5f81b6a0a.png)

- When the app is finished building, click the 'Open Browser' button on the bottom right of the screen:
![image](https://user-images.githubusercontent.com/3254549/90944371-249b8d00-e3d3-11ea-8305-b7d4fad9742c.png)

- The Application is ready to go
![image](https://user-images.githubusercontent.com/3254549/90944387-439a1f00-e3d3-11ea-9df4-e8a5580c62cd.png)

### 4. Start locally

**✅ Download the secure connection bundle** : UI

**✅ Setup the configuration file** : In the folder `src/main/resources` of this repo locate the file `application.yaml` and edit the following keys:

- `keyspace-name`: Name of the keyspace as stated in the user interface
- `username`: user credentials
- `password`: user credentials
- `secure-connect-bundle` : path of the zip you downloaded

*Sample configuration file*
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

**✅ Start the application** : You can now run the application with the command: `mvn spring-boot:run`. This will create the required schema for the application in your Astra database. You should now be able to access

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-swagger.png)

### A3 - Build and start the Angular user interface

We suggest using the existing **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. 

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

You should now be able to access the UI at: [http://localhost:4200](http://localhost:4200)
![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)

- Owners
![Pet Clinic Owners Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-owners.png)

- Pet Types
![Pet Clinic Pets Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-pettypes.png)

- Vet Specialties
![Pet Clinic Specialties Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-specialties.png)

- Veterinians
![Pet Clinic Veterinarians Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-veterinians.png)



