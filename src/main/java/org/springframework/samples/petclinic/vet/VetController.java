package org.springframework.samples.petclinic.vet;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.OPTIONS;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/vets")
@CrossOrigin(
 methods = {PUT, POST, GET, OPTIONS, DELETE, PATCH},
 maxAge = 3600,
 allowedHeaders = {"x-requested-with", "origin", "content-type", "accept"},
 origins = "*"
)
public class VetController {
    
    @RequestMapping(method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Flux<Vet>> getAllVets() {
        return new ResponseEntity<Flux<Vet>>(Flux.just(
                new Vet(1L, "cedrick", "lunven", new HashSet<String>()),
                new Vet(2L, "david", "gilardi", new HashSet<String>())), HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{vetId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Mono<Vet>> getVet(@PathVariable("vetId") int vetId){
        return new ResponseEntity<Mono<Vet>>(
                Mono.just(new Vet(1L, "cedrick", "lunven", new HashSet<String>())), 
                HttpStatus.OK);
    }
    
    

}
