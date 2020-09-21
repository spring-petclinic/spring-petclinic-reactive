package org.springframework.samples.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;

import com.datastax.oss.driver.api.core.CqlSession;

/**
 * PetClinic Spring Boot Application.
 * 
 * In the following implementation we are using
 * - Java 11 (LTS)
 * - Webflux to expose endpoints over HTTP
 * - Swagger3+ (OpenApi) to provide documentation and test client
 * - DataStax v4 driver with reactive support
 * 
 * The API uses a relational-style objects layer (owner->pet->visit) but the underlying data model
 * follows Cassandra best practices (denormalization). We did not change the API specification 
 * in order to match existing spring-petclinic-angular project.
 * 
 * As we are NOT using Spring-Data in the version we disable {@link CassandraAutoConfiguration}
 * and explicitely initialized the {@link CqlSession}.
 * 
 * @author Cedrick Lunven (@clunven)
 */
@SpringBootApplication(exclude = CassandraAutoConfiguration.class)
public class PetClinicApplication {

    /** No args needed. */
    public static void main(String[] args) {
        SpringApplication.run(PetClinicApplication.class, args);
    }
  
}
