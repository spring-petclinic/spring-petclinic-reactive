package org.springframework.samples.petclinic.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Home:  API documentation page
 */
@Controller
public class HomeController {
    
    /**
     * As the application does not provide any views (HTTP only) we
     * can enforce a manual HTTP REDIRECT.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Mono<Void> redir(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().add(HttpHeaders.LOCATION, "/swagger-ui/");
        return response.setComplete();
    }
}