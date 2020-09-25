# Reactive Implementation of 🐈 Spring PetClinic 🐕

[![Build Status](https://travis-ci.org/ff4j/ff4j.svg?branch=master)](https://travis-ci.org/clun/spring-petclinic-reactive)
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive)

This application serves a REST API that's implemented with **Spring WebFlux** as the web framework and *Apache Cassandra&reg;* as the database. This backend is meant to be used with the `spring-petclinic-angular` user interface.

The steps to run the full application are provided below.

To get started with a free-forever, zero-install Cassandra database **[click here](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive)** 🚀


![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/logical-architecture.png)
*Architecture overview*

## Table of contents

- [Run the application](#run-the-application)
- [Understand the architecture](#understand-the-architecture)
- [Contributing](#contributing)

## Run the application

### 1. Start the database

**✅ Create an free-forever Cassandra database with DataStax Astra**: [click here to get started](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive) 🚀


![Astra Registration Screen](doc/img/db-auth.png?raw=true)


**✅ Use the form to create new database**

If you don't have an existing instance,  login process will route automatically to the database creation form. You will find below which values to enter for each field.

![Astra Database Creation Form](doc/img/db-creation.png?raw=true)


**✅ View your Database and connect**

View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point.

**👁️ Expected output**

*Initializing*

![Initializing Screen](doc/img/db-initializing.png?raw=true)

*Database is ready*

![Database ready screen](doc/img/db-ready.png?raw=true)

### 2. Copy credentials to connect

**✅ Navigate to your credentials**

Click `Your Databases`, then `Manage Organizations` on the top navigation:

![image](https://user-images.githubusercontent.com/3254549/90944069-9f63a880-e3d1-11ea-834a-968ffe69e37b.png)

On the right side of your organization, click the elipsis (...) then `Manage Organizations`:

![image](https://user-images.githubusercontent.com/3254549/90944096-c02bfe00-e3d1-11ea-9513-b3362cdfd77a.png)


**✅ Add a service account if one does not exist**
Click the action menu, then select 'Add Service Account':

![image](https://user-images.githubusercontent.com/3254549/90944155-05503000-e3d2-11ea-9d2a-8c376b027358.png)

**✅ Copy credentials to your clipboard**

Click the copy icon to copy your Astra service account credentials to your clipboard:

![image](https://user-images.githubusercontent.com/3254549/90944221-3c264600-e3d2-11ea-9d04-46915f1c3731.png)

This credentials copied in the clipboard look like the following JSON:
```json
{ 
  "clientId":"149de2c7-9b07-41b3-91ad-9453dee4dc54",
  "clientName":"cedrick.lunven@datastax.com",
  "clientSecret":"aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
}
```

### 3. Start in Gitpod

**✅ Open Gitpod (with creds copied to clipboard)**

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive)

**✅ Paste credentials in Gitpod terminal**

Once your Gitpod workspace has loaded, you'll be asked to paste your service account credentials in the Gitpod terminal at the bottom of the screen. This questions is asked from the [setup.sh](setup.sh) script at root of the repository.

![image](doc/img/script-copy-creds.png?raw=true)

**✅ Open Swagger UI in browser**

When the app is finished building, click the 'Open Browser' button on the bottom right of the screen:

![image](doc/img/exec-start.png?raw=true)

**🎉 Celebrate!**

You've successfully built the Spring Petclinic Reactive backend application!

![image](doc/img/exec-api-page.png?raw=true)


**✅ Start the Web UI** :

This REST API is meant to be used with the existing **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. To run the application please execute the following:

- Installation of the components
```bash
git clone https://github.com/spring-petclinic/spring-petclinic-angular.git
cd spring-petclinic-angular
npm uninstall -g angular-cli @angular/cli
npm install -g @angular/cli@8.0.3
npm install --save-dev @angular/cli@8.0.3
npm install --save-dev @angular-devkit/build-angular
npm install -g typescript
npm install
npm run build
```

- Copy the url of the backend to get it use the following command:

```
gp url 9966
```

- Locate the file `src/environments/environment.ts` 

```
export const environment = {
  production: false,
  REST_API_URL: 'https://localhost:9966/petclinic/api/'
};
```

- Change `localhost:9966` with valid backend adress. As an EXAMPLE this is what it look like for us

```
export const environment = {
  production: false,
  REST_API_URL: 'https://9966-ea945100-94e7-4790-9b49-9514424ded86.ws-eu01.gitpod.io/petclinic/api/'
};
```

- Start the UI with `ng serve`

You should now be able to access the UI on port 4200.

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)


**NOTE** If you want to run everything locally, reference the [LOCAL_README.md](doc/LOCAL_README.md)

## Understand the architecture

### Architecture diagram

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/logical-architecture.png)

You can find here an description of the logical architecture components:

- `spring-parclinic-angular` :This is the existing project provide user interface implementation using Angular. It has been used as well for other backend projects like the 
**spring-petclinic-rest**

- `prometheus`: Our component expose some metrics through the actuator endpoint. A regitry will push those information into Prometheus database (docker-based).

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/prometheus.png)

- `Grafana`: Allows to create dhasboards based on data store in prometheus.

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/grafana.png)

- `zipkin`: Our component include the `spring-cloud-sleuth` dependency allow Brave to push metrics usage of the API to distributed tracing component Zipkin. To enable this tracing
set the properties to `zipkin.enabled` to true in `application.yaml`. 
To start zipkin use `docker-compose up -d`

```
  zipkin:
    enabled: true
    baseUrl: http://localhost:9411
    sender:
      type: web
```

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/zipkin.png)

- `Apache Cassandra`: A NoSQL database

- `DataStax Astra` : Apache Cassandra available in the Cloud for free as a managed service (DBaas)

Let's have a look inside the main component `spring-petclinic-reactive` to see which libraries and frameworks have been used.

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/internal-architecture.png)

- `Spring-boot`: Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run". We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.

- `Spring-Security`: Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements

- `Spring-WebFlux`: Spring sub framework allowing to create Reactive Rest Endpoint 

- `Spring-Actuator`: Expose Endpoints to expose metrics to third party system: health, infos, jmx,prometheus,...

- `Spring-Test`: Enabled unit testing and mocking with Spring configuration and beans

- `Spring-Cloud`: Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state). Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer’s own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.

- `SpringFox` *(Swagger)*: Annotation based rest documentation generation and test client generation (swagger-ui)

### Data Model diagram

The underlying data model implement in Apache Cassandra is different from the one you would have define with a relational database. 

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/data-model.png)

Some denormalization has been realized as there are no joins, no integrity constraints with Apache Cassandra. Some `secondary indices` have been created to queries columns that are not the PARTITION KEY because the cardinality is low (eg:few pets for an owner).

The objects related to the data model (table, indices,udt) are generated by the application at startup. 

## C. Contributing

 The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-reactive/issues)is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the editor config for easy use in common text editors. Read more and download plugins at http://editorconfig.org.


![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/banner.png)

