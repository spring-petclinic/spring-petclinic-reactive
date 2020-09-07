package org.springframework.samples.petclinic.conf.security;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration v3 does not change with Weblux anymore.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "true")
public class ApiDocSecurityEnabled {
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("petclinic")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.samples.petclinic"))
                .paths(PathSelectors.ant("/petclinic/api/**"))
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(basicAuthScheme()))
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Spring Pet Clinic Application Reactive (Secured)",
                "Leveraging Reactive Datastax driver and Astra Cassandra-as-a-service",
                "1.0.0-SNAPSHOT",
                "Terms of service",
                new Contact("DataStax Examples", 
                    "https://www.datastax.com/examples", 
                    "examples@datastax.com"), "Apache v2.0", 
                    "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }
    
    @SuppressWarnings("deprecation")
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(basicAuthReference()))
                .forPaths(PathSelectors.ant("/petclinic/api/**"))
                .build();
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }

}