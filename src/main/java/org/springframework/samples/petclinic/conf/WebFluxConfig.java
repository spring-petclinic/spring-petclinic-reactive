package org.springframework.samples.petclinic.conf;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

// TODO: can we add a bit more context? What is the need for cross-origin?
/**
 * Add a CORS Filter to allow cross-origin
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {
    
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN";
    private static final String ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String MAX_AGE = "3600";

    /** {@inheritDoc} */
    //@Override
    //public void addCorsMappings(CorsRegistry registry) {
    //    registry.addMapping("/**")
    //            .allowedOrigins("*")
    //           .allowedMethods("*");
    //}
    
    @Bean
    public WebFilter corsFilter() {
      return (ServerWebExchange ctx, WebFilterChain chain) -> {
        ServerHttpRequest request = ctx.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
          ServerHttpResponse response = ctx.getResponse();
          HttpHeaders headers = response.getHeaders();
          headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
          headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
          headers.add("Access-Control-Max-Age", MAX_AGE);
          headers.add("Access-Control-Allow-Headers",ALLOWED_HEADERS);
          if (request.getMethod() == HttpMethod.OPTIONS) {
            response.setStatusCode(HttpStatus.OK);
            return Mono.empty();
          }
        }
        return chain.filter(ctx);
      };
    }

    
}
