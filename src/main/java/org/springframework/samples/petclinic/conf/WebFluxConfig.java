package org.springframework.samples.petclinic.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * We need to put in place a CORS (cross-origin resource sharing) web filter in order
 * to allow a user interfaces (using Javascript) to invoke our API seven if they are not in the 
 * same domain.
 * 
 * - Documentation on CORS
 * {@link https://www.codecademy.com/articles/what-is-cors}
 * 
 * - Documentaton on WebFluxConfigurer 
 * {@link https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/config/WebFluxConfigurer.html}
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {
    
    /**
     * Override the default webfilter to ass a CORS filter.
     * We pay attention to preflight requets with HTTP verb OPTIONS. 
     * For this @CorsOrigin may not be enough.
     */
    @Bean
    public WebFilter corsFilter() {
      return (ServerWebExchange ctx, WebFilterChain chain) -> {
        ServerHttpRequest request = ctx.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
          // Add an header on the response explaining we are allowing other domains
          ServerHttpResponse response = ctx.getResponse();
          HttpHeaders headers = response.getHeaders();
          headers.add("Access-Control-Allow-Origin", "*");
          headers.add("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, OPTIONS");
          headers.add("Access-Control-Max-Age", "3600");
          headers.add("Access-Control-Allow-Headers", "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN");
          // Specific for preflight request. 
          if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
          }
        }
        return chain.filter(ctx);
      };
    }
    
    /**
     * Enable validation of parameters through Annotation. (spring-validation)
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
    
}
