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

- **(A) Run the APPLICATION**
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

## Run the PetClinic Application

### 1. Get the Database running

**✅ Create an FREE FOREVER Apache Cassandra DBaas instance** : 

You can sign in with your `Github` or `Google` account or sign up with an `email`

- [REGISTER HERE](https://astra.datastax.com/register?utm_source=github&utm_medium=referral&utm_campaign=spring-petclinic-reactive) 🚀

![Astra Registration Screen](doc/img/db-auth.png?raw=true)


**✅ Use the form to create New database** : 

If you don't have an existing instance,  login process will route automatically to the database creation form. You will find below which values to enter for each field.

![Astra Database Creation Form](doc/img/db-creation.png?raw=true)

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
![Initializing Screen](doc/img/db-initializing.png?raw=true)

*Database is ready*
![Database ready screen](doc/img/db-ready.png?raw=true)

### 2. Copy Database credentials
   
- Click `Your Databases`, then `Manage Organizations` on the top navigation: 

![image](https://user-images.githubusercontent.com/3254549/90944069-9f63a880-e3d1-11ea-834a-968ffe69e37b.png)

- On the right side of your organization, click the elipsis (...) then `Manage Organizations`:

![image](https://user-images.githubusercontent.com/3254549/90944096-c02bfe00-e3d1-11ea-9513-b3362cdfd77a.png)

- Click the action menu, then select 'Add Service Account':

![image](https://user-images.githubusercontent.com/3254549/90944155-05503000-e3d2-11ea-9d2a-8c376b027358.png)

- Click the copy icon to copy your Astra service account credentials to your clipboard:

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

- With the credentials in the copy board you can now open gitpod

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/clun/spring-petclinic-reactive)


- At startup gitpod will compile the application. 
![Initializing Screen](doc/img/exec-home-gitpod.png?raw=true)

- Once your Gitpod workspace has loaded, you'll be asked to paste your service account credentials in the Gitpod terminal at the bottom of the screen. This questions is asked from the [setup.sh](setup.sh) script at root of the repository.

![image](doc/img/script-copy-creds.png?raw=true)

- After parsing this JSON the script will ask you your `password`. 

![image](doc/img/script-ask-password.png?raw=true)

-  In the end,a couple of environment variables has been created for you could list them later this
```
env | grep ASTRA
```

**👁️ Expected output**

```init
ASTRA_DB_BUNDLE=astra-creds.zip
ASTRA_DB_USERNAME=petclinic
ASTRA_SECURE_BUNDLE_URL="https://datastax-cluster-config-prod.s3.us-east-2.amazonaws.com/bd8c8102-aab4-4cbe-81e7-76680c904f26/secure-connect-petclinicdb.zip?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2AIQRQ76TUCOHUQ4%2F20200916%2Fus-east-2%2Fs3%2Faws4_request&X-Amz-Date=20200916T130746Z&X-Amz-Expires=300&X-Amz-SignedHeaders=host&X-Amz-Signature=4d85e18911153b0954a6adc52ffe8e6ac44b887c3a6d23e6e72fd28d14d934d3"
ASTRA_DB_REGION=europe-west1
ASTRA_DB_ID=bd8c8102-aab4-4cbe-81e7-76680c904f26
ASTRA_DB_PASSWORD=astra2020
ASTRA_DB_KEYSPACE=spring_petclinic
```

- When the app is finished building, click the 'Open Browser' button on the bottom right of the screen:
![image](doc/img/exec-start.png?raw=true)

- The Application is ready to go
![image](doc/img/exec-api-page.png?raw=true)

- As schema and reference data are loaded as startup you can already invoke the api like listing the pet types. Locate the resource `GET` on `/petclinic/api/pettypes`, click on the blue bar then `Try it out` and then `execute` you should have the data coming back from the DB. Congratulations you made it !
![image](doc/img/exec-list-pettypes.png?raw=true)

**✅ Start the WEB UI** : This REST API is meant to be used with the existing **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. To run the application please execute the following:

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


### 4. Start locally

**✅ Download the secure connect bundle :** Go to the home page. Execute a refresh of the page using (F5) (the download link will be valid for 5 minutes and we want to ensure NOT to reach the timeout). Locate link Download secure connect bundle and click. You should download a file named secure-connect-<your_db_name>.zip. Please remember the location of the file.

![image](doc/img/cloud-secure-bundle.png?raw=true)

Save the file in a path you will remember, again we will need it for the next exercises.

**✅ Define environment variables** : To start the application needs a few environment variables in order to setup the connection to databases. Please define them like the following

```
export ASTRA_DB_USERNAME=petclinic
export ASTRA_DB_PASSWORD=petclinic
export ASTRA_DB_KEYSPACE=spring_petclinic
export ASTRA_DB_BUNDLE=/Users/cedricklunven/Downloads/secure-connect-petclinicdb.zip
```

**✅ Start the application** : You can now run the application with the command: `mvn spring-boot:run`. This will create the required schema for the application in your Astra database.

![image](doc/img/exec-local.png?raw=true)

### 5. More screenShots

- Owners
![Pet Clinic Owners Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-owners.png)

- Pet Types
![Pet Clinic Pets Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-pettypes.png)

- Vet Specialties
![Pet Clinic Specialties Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-specialties.png)

- Veterinians
![Pet Clinic Veterinarians Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-veterinians.png)

**✅ Start the WEB UI** : This REST API is meant to be used with the existing **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. To run the application please execute the following:

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

You should now be able to access the UI on [localhost:4200](http://localhost:4200).
![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)

## B. Architecture

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



