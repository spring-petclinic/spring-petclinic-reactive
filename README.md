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
1. Click `Use this template` at the top of the [GitHub Repository](GITHUB_URL):
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
