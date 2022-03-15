<!--- STARTEXCLUDE --->
# Reactive Implementation of 🐈 Spring PetClinic 🐕
*15 minutes, Intermediate, [Start Building](https://github.com/DataStax-Examples/spring-petclinic-reactive#prerequisites)*



This sample is a fully reactive version of the Spring PetClinic application using Spring WebFlux.
<!--- ENDEXCLUDE --->


![image](https://raw.githubusercontent.com/DataStax-Examples/spring-petclinic-reactive/master/hero.png)


## Get Started
To build and play with this app, follow the build instructions that are located here: [https://github.com/DataStax-Examples/spring-petclinic-reactive](https://github.com/DataStax-Examples/spring-petclinic-reactive#prerequisite)


<!--- STARTEXCLUDE --->
## Prerequisites
Let's do some initial setup by creating a serverless(!) database.

### DataStax Astra
1. Create a [DataStax Astra DB account](https://dtsx.io/38yYuif) if you don't already have one:
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-register-basic-auth.png)

2. On the home page. Locate the button **`Create Database`**
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-dashboard.png)

3. Locate the **`Get Started`** button to continue
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-select-plan.png)

4. Define a **database name**, **keyspace name** and select a database **region**, then click **create database**.
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-create-db.png)

5. Your Astra DB will be ready when the status will change from *`Pending`* to **`Active`** 💥💥💥 
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-db-active.png)

6. After your database is provisioned, we need to generate an Application Token for our App. Go to the `Settings` tab in the database home screen.
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-db-settings.png)

1. Select `Admin User` for the role for this Sample App and then generate the token. Download the CSV so that we can use the credentials we need later.
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-db-settings-token.png)

1. After you have your Application Token, head to the database connect screen and select the driver connection that we need. Go ahead and download the `Secure Bundle` for the driver.
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-db-connect-bundle.png)

9. Make note of where to use the `Client Id` and `Client Secret` that is part of the Application Token that we generated earlier.
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/astra-db-connect-bundle-driver.png)

### Github

1. Click `Use this template` at the top of the [GitHub Repository](https://github.com/DataStax-Examples/spring-petclinic-reactive#prerequisite):
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/github-use-template.png)

2. Enter a repository name and click 'Create repository from template':
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/github-create-repository.png)

3. Clone the repository:
![image](https://raw.githubusercontent.com/DataStax-Examples/sample-app-template/master/screenshots/github-clone.png)

## 🚀 Getting Started Paths:
*Make sure you've completed the [prerequisites](#prerequisites) before starting this step*
  - [Running on Gitpod](#running-on-gitpod)
  - [Deploying to Vercel](#deploying-to-vercel)
  - [Deploying to Netlify](#deploying-to-netlify)


### Running on Gitpod

1. Click the 'Open in Gitpod' link:
[![Open in IDE](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/spring-petclinic/spring-petclinic-reactive)
<!--- ENDEXCLUDE --->

![Astra Database Creation Form](doc/img/db-creation-4.png?raw=true)

**✅ View your Database and connect**

View your database. It may take 2-3 minutes for your database to spin up. You will receive an email at that point.

**👁️ Expected output**

*Initializing*

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/dashboard-pending-1000.png?raw=true)

Once the database is ready, notice how the status changes from `Pending` to `Active` and Astra enables the **CONNECT** button.

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/dashboard-withdb-1000.png?raw=true)

### 2. Copy credentials to connect

**✅ Navigate to your credentials**

Locate the combo `Organization: <Your email>` on the top navigation. On the right side of your organization, click the ellipsis (...) then click your `<Your email>`.

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-combo-1000.png?raw=true)

You should land on the following screen. Scroll down to the bottom of the page to locate the `Service Account` in `Security Settings`

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-home-1000.png?raw=true)

**✅ Create Service Account**


Create a service account by clicking `Add Service Account` button above the section as shown below
![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/security-settings-annotated.png?raw=true)
When panel open on the right, click `Add` 

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/security-add-org-annotated.png?raw=true)

**✅ Copy credentials to your clipboard**


Click the ellipsis at end of Service Account row to open menu as select `Copy Credentials`

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/organization-copycredentials-1000.png?raw=true)

The credentials you copied to the clipboard look like the following JSON, we will use it in gitpod to enable connectivity.
```json
{
  "clientId":"149de2c7-9b07-41b3-91ad-9453dee4dc54",
  "clientName":"cedrick.lunven@datastax.com",
  "clientSecret":"aaaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"
}
```

### 3. Start in Gitpod

**✅ Open Gitpod (with creds copied to clipboard)**

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/datastaxdevs/workshop-spring-reactive)

When you first launch gitpod, it builds the image.
![image](doc/img/building-workspace.png?raw=true)

Git pod pulls the image.
![image](doc/img/pulling-image.png?raw=true)

**✅ Paste credentials in Gitpod terminal**

Once Gitpod loads the workspace, you'll be asked to paste your service account credentials in the Gitpod terminal at the bottom of the screen. The [setup.sh](setup.sh) script at the root of the repository is what asks this question.

![image](doc/img/script-copy-creds.png?raw=true)

**✅ Open Swagger UI in browser**

When gitpod finishes building the app, a new tab will open in your browser showing the following.

![image](doc/img/exec-start.png?raw=true)

**🎉 Celebrate!**

You've successfully built the Spring Petclinic Reactive backend application!

![image](doc/img/exec-api-page.png?raw=true)

**✅ Start the Web UI** :

You may have noticed another terminal named `spring-petclinic-angular`. This is where the UI should start.

![image](doc/img/start-ui.png?raw=true)

After answering the question about analytics usage, you should be able to access the UI on a new tab.

![Pet Clinic Welcome Screen](doc/img/ui-top.png?raw=true)

**NOTE** If you want to run everything locally, reference the [LOCAL_README.md](doc/LOCAL_README.md)

## Understand the architecture

### Internal Architecture our of component

Let's have a look inside the main component `spring-petclinic-reactive` to see which libraries and frameworks have been used.

![Pet Clinic Welcome Screen](doc/img/internal-architecture.png?raw=true)

- `Spring-boot`: Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run". We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.

- `Spring-Security`: Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. Spring Security is a framework that focuses on providing both authentication and authorization to Java applications. Like all Spring projects, the real power of Spring Security is found in how easily it can be extended to meet custom requirements.

- `Spring-WebFlux`: Spring sub framework allowing to create Reactive Rest Endpoint.

- `Spring-Actuator`: Expose Endpoints to expose metrics to third party system: health, infos, jmx,prometheus,...

- `Spring-Test`: Enabled unit testing and mocking with Spring configuration and beans.

- `Spring-Cloud`: Spring Cloud provides tools for developers to quickly build some of the common patterns in distributed systems (e.g. configuration management, service discovery, circuit breakers, intelligent routing, micro-proxy, control bus, one-time tokens, global locks, leadership election, distributed sessions, cluster state). Coordination of distributed systems leads to boiler plate patterns, and using Spring Cloud developers can quickly stand up services and applications that implement those patterns. They will work well in any distributed environment, including the developer’s own laptop, bare metal data centres, and managed platforms such as Cloud Foundry.

- `SpringFox` *(Swagger)*: Annotation based rest documentation generation and test client generation (swagger-ui).

![Pet Clinic Welcome Screen](doc/img/swagger.png?raw=true)


### Logical Architecture

![Pet Clinic Welcome Screen](doc/img/logical-architecture.png?raw=true)

Here, you can find a description of the logical architecture components:

- `spring-parclinic-angular`: This is the existing project that provides a user interface implementation using Angular. It has been used as well for other backend projects like the
**spring-petclinic-rest**

- `prometheus`: Our component exposes some metrics through the actuator endpoint. A registry will push this information into the Prometheus database (docker-based).

![Pet Clinic Welcome Screen](doc/img/prometheus.png?raw=true)

- `Grafana`: Allows to create dashboards based on data stored in prometheus.

![Pet Clinic Welcome Screen](doc/img/grafana.png?raw=true)

- `zipkin`: Our component includes the `spring-cloud-sleuth` dependency allowing Brave to push metrics usage of the API to the distributed tracing component Zipkin. To enable this tracing
set the property `zipkin.enabled` to true in `application.yaml`.
To start zipkin use `docker-compose up -d`

```
  zipkin:
    enabled: true
    baseUrl: http://localhost:9411
    sender:
      type: web
```

![Pet Clinic Welcome Screen](doc/img/zipkin.png?raw=true)

- `Apache Cassandra`: A NoSQL database

- `DataStax Astra` : Apache Cassandra available in the Cloud for free as a managed service (DBaas)


### Data Model diagram

The underlying data model implemented in Apache Cassandra is different from the one you would have defined with a relational database.

![Pet Clinic Welcome Screen](doc/img/data-model.png?raw=true)

To enable scalability, Apache Cassandra does not support joins or integrity constraints. Therefore we used some denormalization.
We also created some `secondary indices` to queries columns that are not the PARTITION KEY. These secondary indices work well in this case because the cardinality is low (e.g, few pets for an owner).

The application generates the objects related to the data model (e.g., tables, indices, udts) at startup.

## C. Contributing

 The [issue tracker](https://github.com/spring-petclinic/spring-petclinic-reactive/issues)is the preferred channel for bug reports, features requests and submitting pull requests.

For pull requests, editor preferences are available in the editor config for easy use in common text editors. Read more and download plugins at http://editorconfig.org.

![banner](doc/img/banner.png?raw=true)


