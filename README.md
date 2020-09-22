# Reactive Implementation of 🐈 Spring PetClinic 🐕

[![Build Status](https://travis-ci.org/ff4j/ff4j.svg?branch=master)](https://travis-ci.org/clun/spring-petclinic-reactive)
[![License Apache2](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Gitpod ready-to-code](https://img.shields.io/badge/Gitpod-ready--to--code-blue?logo=gitpod)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive)

This application serves a REST API that's implemented with Spring WebFlux as the web framework and Apache Cassandra&reg; as the database. This backend is meant to be used with the **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface.

The steps to run the full application are provided below.

To get started with a free-forever, zero-install Cassandra database **[click here](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive)** 🚀

![banner](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/banner.png)

## Table of contents

- [Run the application](#run-the-application)
- [Understand the architecture](#understand-the-architecture)
- [Contribute](#contribute)

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

You should now be able to access the UI on port 4200.

![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)


**NOTE** If you want to run everything locally, reference the [LOCAL_README.md](doc/LOCAL_README.md)

## Understand the architecture

### Understanding the Pet Clinic Application

Best resources is probably https://spring-petclinic.github.io/.  The Spring PetClinic is a sample application designed to show how the Spring stack can be used to build simple, but powerful database-oriented applications.

![image](doc/img/pet-clinic.png?raw=true)

### Architecture diagram

*in progress*

### Data Model diagram

*in progress*

## C. Contribute

### Blog post and communications

*in progress*



