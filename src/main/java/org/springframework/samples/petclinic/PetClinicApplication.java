package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PetClinic Spring Boot Application.
 * 
 * - Using Webflux
 * - Using Spring-Data-Cassandra-reactive
 * - With Java 11
 * - Expositing REST Endpoints documented with OpenAPI
 * 
 * @author Dave Syer
 * @author Cedrick Lunven (@clunven)
 */
@SpringBootApplication
public class PetClinicApplication {

  public static void main(String[] args) {
    SpringApplication.run(PetClinicApplication.class, args);
  }
  
}
