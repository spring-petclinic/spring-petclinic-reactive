package org.springframework.samples.petclinic.owner;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: services are to be implemented by whom?
/**
 * Services to be implemented to interact with the backend.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface OwnerReactiveServices {
    
    Flux<Owner> findAllOwners();
    
    Flux<Owner> findOwnersByName(String searchString);
    
    Mono<Owner> findOwnerById(String ownerId);
    
    Mono<Owner> createOwner(Owner owner);
    
    Mono<Owner> updateOwner(Owner owner);
    
    Mono<Boolean> deleteOwner(String ownerId);
    
}
