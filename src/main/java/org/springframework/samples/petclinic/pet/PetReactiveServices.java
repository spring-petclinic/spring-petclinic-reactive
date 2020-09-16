package org.springframework.samples.petclinic.pet;

import java.util.Set;
import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PetReactiveServices {
    
    // Operations on pets
    
    Flux<Pet> findAllPets();
    
    Mono<Pet> findPetByPetId(UUID petId);
    
    Mono<Pet> createPet(Pet pet);
    
    Mono<Set<PetType>> findAllPetTypes();
    
    Mono<Boolean> deletePetById(UUID petId);
    
    // Operations on pet types
    
    Mono<Set<String>> listPetType();
    
    Mono<PetType> addPetType(PetType value);
    
    Mono<PetType> replacePetType(String oldValue, String newValue);
    
    Mono<Void> removePetType(String value);
    
}
