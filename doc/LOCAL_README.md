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

- veterinarians

![Pet Clinic Veterinarians Screen](https://raw.githubusercontent.com/clun/spring-petclinic-reactive/master/doc/img/ui-veterinarians.png)

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