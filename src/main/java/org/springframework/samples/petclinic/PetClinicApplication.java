package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PetClinic Spring Boot Application.
 * 
 * In the following implementation we are using
 * - Java 11 (LTS)
 * - Webflux to expose endpoints over HTTP
 * - Swagger3+ (OpenApi) to provide documentation and test client
 * - Datastax v4 driver with reactive support
 * 
 * The API keep using relation-ish objects layer (owner->pet->visit) but data model
 * follows Cassandra best practices. (denormalization).
 * 
 * We did not change the API to match the spring-petclinic-angular project where it could have been useful.
 * 
 * Specially:
 * - /owners/{ownerId}/pets/
 * - /owners/{ownerId}/pets/
 * - /owners/{ownerId}/pets/{petId}/visits
 * - /owners/{ownerId}/pets/{petId}/visits/{visitId}
 * would have been more relevant.
 * 
 * @author Cedrick Lunven (@clunven)
 */
@SpringBootApplication
public class PetClinicApplication {

  /**
   * No args, loading config from `application.yaml`
   */
    public static void main(String[] args) {
    SpringApplication.run(PetClinicApplication.class, args);
  }
  
}
