package org.springframework.samples.petclinic.conf;

import java.util.Collections;

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
 * Swagger configuration v3 does not change with Weblux anymore.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
@EnableSwagger2
public class WebOpenApiConfig {
    
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("petclinic")
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.springframework.samples.petclinic"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Spring Pet Clinic Application Reactive",
                "Leveraging Spring-data-cassandra-reactive and Astra Cassandra-as-a-service",
                "1.0.0-SNAPSHOT",
                "Terms of service",
                new Contact("DataStax Examples", "https://www.datastax.com/examples", "examples@datastax.com"),
                "Apache v2.0", "https://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }

}