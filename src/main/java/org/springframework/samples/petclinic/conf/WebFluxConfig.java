package org.springframework.samples.petclinic.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

// TODO: can we add a bit more context? What is the need for cross-origin?
/**
 * Add a CORS Filter to allow cross-origin
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {
    
}
