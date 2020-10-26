package org.springframework.samples.petclinic.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Setup of spring-security to enforce authentiation when flag
 * 'petclinic.security.enable' is set to true.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@ConditionalOnProperty(name = "petclinic.security.enable", havingValue = "true")
public class SecurityEnabledConfig {

    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityEnabledConfig.class);
    
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        LOGGER.info("Authentication on APIs has been enabled");
        return http.csrf().disable()
                   .authorizeExchange()
                     // Exclude public resources from the filter
                     .pathMatchers("/", "/csrf", 
                             "/v2/api-docs", 
                             "/swagger-resources/**",
                             "/swagger-ui.html", 
                             "/swagger-ui/**",
                             "/webjars/**").permitAll()
                     .anyExchange().authenticated()
                   .and()
                     .httpBasic()
                   .and()
                     .formLogin().disable()
                .build();
    }

    /**
     * Password is prefixed with {noop} to indicate to DelegatingPasswordEncoder 
     * that NoOpPasswordEncoder should be used. (demo!)
     */
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }

}
