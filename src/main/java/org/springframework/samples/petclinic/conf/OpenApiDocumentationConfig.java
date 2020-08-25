package org.springframework.samples.petclinic.conf;

//@Configuration
public class OpenApiDocumentationConfig {

    /*@Bean
    public OpenAPI openApiSpec(@Value("${springdoc.version}") String appVersion) {
        String des = "Spring Pet Clinic Application Reactive";
        Info info  = new Info().title("Spring Pet Clinic Application Reactive")
                .version(appVersion).description(des)
                .termsOfService("http://swagger.io/terms/")
                .license(new License().name("Apache 2.0")
                .url("https://spring-petclinic.github.io/"));
        return new OpenAPI().addServersItem(new Server().url("/")).info(info);
    }
    
    //@Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder().setGroup("Monitoring (Actuator)")
                .pathsToMatch("/actuator/**")
                .pathsToExclude("/actuator/health/*")
                .build();
    }*/
    
}
