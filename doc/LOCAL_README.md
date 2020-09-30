# Reactive Implementation of 🐈 Spring PetClinic 🐕

## Cassandra

### Still using Cassandra As a service

To create the DB the instruction [here are relevant](../spring-petclinic-reactive#1-start-the-database)

**✅ Download the secure connect bundle :**

On the summary page locate the `connect` button close to your database name:

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/summary-1000-connect.png?raw=true)

Then on the connection screen select `Drivers`
![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/connect-rest-driver.png?raw=true)

Finally on the last page click the button `Download Secure Bundle`

![my-pic](https://github.com/datastaxdevs/shared-assets/blob/master/astra/connect-driver-1000.png?raw=true)

Save the file in a path you will remember, again we will need it for the next exercises.

**✅ Define environment variables** : To start the application needs a few environment variables in order to setup the connection to databases. Please define them like the following

```
export ASTRA_DB_USERNAME=petclinic
export ASTRA_DB_PASSWORD=petclinic
export ASTRA_DB_KEYSPACE=spring_petclinic
export ASTRA_DB_BUNDLE=/Users/cedricklunven/Downloads/secure-connect-petclinicdb.zip
```

### Work 100% locally

Locate the file `docker-compose.yaml` on the repository and uncomment the block with the cassandra instruction.

Locate the file `application.yaml`  on the repository and switch property `petclinic.astra.enable` to false. The application will now read the configuration file `application-local.conf` and not `application-astra.conf`


## The application


**✅ Start Prometheus,Grafana, Zipkin** :

All this components are available in the `docker-composer.yaml` file so simply. If you uncommented Cassandra it will also start.

```
docker-compose up -d
```

output 
```
Creating network "spring-petclinic-reactive_default" with the default driver
Creating prometheus-server ... done
Creating tracing-server    ... done
Creating grafana-server    ... done
```

- Grafana is available at [localhost:3000](http://localhost:3000)

![Pet Clinic Welcome Screen](img/grafana.png?raw=true)

- Prometehus is available at [localhost:9091](http://localhost:9091)

![Pet Clinic Welcome Screen](img/prometheus.png?raw=true)

- Zipkin is available at [localhost:9091](http://localhost:9091)

![Pet Clinic Welcome Screen](img/zipkin.png?raw=true)

To enable this tracing set the properties to `zipkin.enabled` to true in `application.yaml`. 

```
  zipkin:
    enabled: true
    baseUrl: http://localhost:9411
    sender:
      type: web
```


**✅ Start the backend** : You can now run the application with the command: `mvn spring-boot:run`. This will create the required schema for the application in your Astra database.

![image](img/exec-local.png?raw=true)




**✅ Start the front end* : This REST API is meant to be used with the existing **[spring-petclinic-angular](https://github.com/spring-petclinic/spring-petclinic-angular)** user interface. To run the application please execute the following:

```bash
git clone https://github.com/spring-petclinic/spring-petclinic-angular.git
cd spring-petclinic-angular
npm uninstall -g angular-cli @angular/cli
npm cache clean
npm install -g @angular/cli@8.0.3
npm install --save-dev @angular/cli@8.0.3
npm install

ng build
ng serve
```

### 5. More screenShots

- Owners

![Pet Clinic Owners Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-owners.png)

- Pet Types

![Pet Clinic Pets Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-pettypes.png)

- Vet Specialties

![Pet Clinic Specialties Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-specialties.png)

- veterinarians

![Pet Clinic Veterinarians Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-veterinarians.png)




You should now be able to access the UI on [localhost:4200](http://localhost:4200).
![Pet Clinic Welcome Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-top.png)