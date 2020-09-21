package org.springframework.samples.petclinic.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Setup of spring-security to permit everything if the flag 
 * 'petclinic.security.enable' is set to false.
 */
@Configuration
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "false")
public class SecurityDisabledConfig {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityDisabledConfig.class);
    
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        LOGGER.info("Authentication on API is disabled");
        return http.authorizeExchange()
                   .anyExchange().permitAll()
                   .and().csrf().disable().build();
    }
}
