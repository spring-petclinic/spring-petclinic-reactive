# Reactive Implementation of 🐈 Spring PetClinic 🐕
*15 minutes, Intermediate*

This sample is a fully reactive version of the Spring PetClinic application using Spring WebFlux.

![image](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)

The steps to run the application are provided below. You can run the application with no installation using Gitpod and Cassandra as DBaas for free or locally. To get started with a free-forever, zero-install Cassandra database **[click here](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive)** 🚀

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

On the Astra home page locate the **Add Database** button

![Astra Database Creation Form](doc/img/db-creation-1.png?raw=true)

Pick the **free tier** plan, this is a true free tier, free forever and no payment method asked 🎉 🎉

![Astra Database Creation Form](doc/img/db-creation-2.png?raw=true)

Pick the proper region and click `configure` button. Number of regions and cloud providers is reduced in the free tier but please notice you can run the DB on any cloud with any VPC Peering.

![Astra Database Creation Form](doc/img/db-creation-3.png?raw=true)

Fill the `database name`, `keyspace name`, `username` and `password`. *Please remember your password as you will be asked to provide it when application start the first time.*

![Astra Database Creation Form](doc/img/db-creation-4.png?raw=true)

**✅ View your Database and connect**

View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point.

**👁️ Expected output**

*Initializing*

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/dashboard-pending-1000.png?raw=true)

Database is ready, notice how the status changed from `pending` to `Active` and now you now have the **connect** button enabled.*

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/dashboard-withdb-1000.png?raw=true)

### 2. Copy credentials to connect

**✅ Navigate to your credentials**

Locate the combo `Organization: <Your email>` on the top navigation. On the right side of your organization, click the elipsis (...) then click your `<Your email>`.

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-combo-1000.png?raw=true)

You should landed to the following screem. Scroll down to the bottom of the page to locate the `Service Account` in `Security Settings`

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-home-1000.png?raw=true)

**✅ Copy credentials to your clipboard**

Click the copy icon to copy your Astra service account credentials to your clipboard:

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-copycredentials-1000.png?raw=true)

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

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-petclinic/spring-petclinic-reactive)

At first launch the gitpod image is built.
![image](doc/img/building-workspace.png?raw=true)

Then image is pulled 
![image](doc/img/pulling-image.png?raw=true)



**✅ Paste credentials in Gitpod terminal**

Once your Gitpod workspace has loaded, you'll be asked to paste your service account credentials in the Gitpod terminal at the bottom of the screen. This questions is asked from the [setup.sh](setup.sh) script at root of the repository.

![image](doc/img/script-copy-creds.png?raw=true)

**✅ Open Swagger UI in browser**

When the app is finished building, a new tab should be opened in your browser showing.

![image](doc/img/exec-start.png?raw=true)

**🎉 Celebrate!**

You've successfully built the Spring Petclinic Reactive backend application!

![image](doc/img/exec-api-page.png?raw=true)

**✅ Start the Web UI** :

You may have noticed another terminal named `spring-petclinic-angular`. This is where the UI should start. 

![image](doc/img/start-ui.png?raw=true)

After answering the question about analytics usage you should now be able to access the UI on a new tab.


![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)

**NOTE** If you want to run everything locally, reference the [LOCAL_README.md](doc/LOCAL_README.md)

## Understand the architecture

### Internal Architecture our of component

Let's have a look inside the main component `spring-petclinic-reactive` to see which libraries and frameworks have been used.

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/internal-architecture.png)

- `Spring-boot`: Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run". We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.

- `Spring-Security`: Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements

- `Spring-WebFlux`: Spring sub framework allowing to create Reactive Rest Endpoint 

- `Spring-Actuator`: Expose Endpoints to expose metrics to third party system: health, infos, jmx,prometheus,...

- `Spring-Test`: Enabled unit testing and mocking with Spring configuration and beans

- `Spring-Cloud`: Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state). Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer’s own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.

- `SpringFox` *(Swagger)*: Annotation based rest documentation generation and test client generation (swagger-ui)

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/swagger.png)


### Logical Architecture

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



### Data Model diagram

The underlying data model implement in Apache Cassandra is different from the one you would have define with a relational database. 

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/data-model.png)

Some denormalization has been realized as there are no joins, no integrity constraints with Apache Cassandra. Some `secondary indices` have been created to queries columns that are not the PARTITION KEY because the cardinality is low (eg:few pets for an owner).

The objects related to the data model (table, indices,udt) are generated by the application at startup. 

## C. Contributing

 The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-reactive/issues)is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the editor config for easy use in common text editors. Read more and download plugins at http://editorconfig.org.


![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/banner.png)

