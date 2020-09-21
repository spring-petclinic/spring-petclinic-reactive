package org.springframework.samples.petclinic.conf;

import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * This class provides configuration for the Swagger framework to generate a testing client
 * in the user interface (Swagger UI).
 *  
 * This bean is used if 'petclinic.security.enable' property has been set to false. 
 * => The generated swagger user interface will NOT have any button to define username and password
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "false")
public class ApiDocSecurityDisabledConfig {
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("petclinic")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.samples.petclinic"))
                .paths(PathSelectors.ant("/petclinic/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Spring Pet Clinic Application Reactive",
                "Leveraging Reactive Datastax driver and DataStax Astra Cassandra-as-a-service",
                "1.0.0-SNAPSHOT",
                "Terms of service",
                new Contact("DataStax Examples",
                    "https://www.datastax.com/examples",
                    "examples@datastax.com"), "Apache v2.0",
                    "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }

}