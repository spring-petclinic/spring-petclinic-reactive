package org.springframework.samples.petclinic.owner;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDao;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of services for Owner. This class leverages
 * on multiple Dao (owner, pet, visit) to compose data needed at UI level.
 */
@Component
@Validated
public class OwnerReactiveServices {

    /** Implementation of Crud operations for owners. */
    private OwnerReactiveDao ownerDao;
    
    /** Implementation of Crud operations for pets. */
    private PetReactiveDao petDao;
    
    /** Implementation of Crud operations for visits. */
    private VisitReactiveDao visitDao;
    
    /**
     * Injection the different Dao through a constructor
     */
    public OwnerReactiveServices(OwnerReactiveDao oDao, PetReactiveDao pDao, VisitReactiveDao vDao) {
        this.ownerDao = oDao;
        this.petDao   = pDao;
        this.visitDao = vDao;
    }

    /**
     * Original user interface of PetClinic (not angular UI) expect to be able to 
     * retrieve owners based on they last_name. 
     * 
     * As Last_name is not the partition key
     * we used a secondary index for the implementation.
     * 
     * @param searchString
     *      last name search expression
     * 
     * @return
     *      a Flux (reactor) with the stream of Owners.
     */
    public Flux<Owner> findOwnersByName(@NotBlank String searchString) {
        return Flux.from(ownerDao.searchByOwnerName(searchString)) // look for entities
                   .map(MappingUtils::mapEntityAsOwner);           // and map for exposition layer
    }
    
    /**
     * Retrieve all owners from the database.
     * 
     * @return
     *      a Flux (reactor) with the stream of Owners.
     */
    public Flux<Owner> findAllOwners() {
        return Flux.from(ownerDao.findAll())
                   .map(MappingUtils::mapEntityAsOwner)
                   .flatMap(petDao::populatePetsForOwner);  
    }

    /**
     * Find an owner from its unique identifier and populate
     * returned bean with Pets and visits.
     * 
     * @param ownerId
     *      unique owner identifier
     * @return
     *      Mono (reactor) containing the Owner or empty
     */
    public Mono<Owner> findOwnerById(@NotBlank String ownerId) {
        return Mono.from(ownerDao.findById(UUID.fromString(ownerId)))
                   .map(MappingUtils::mapEntityAsOwner)
                   .flatMap(this::populateOwner);
    }

    /**
     * Insert a new Owner in the database.
     * 
     * @return
     *      Mono (reactor) containing the initial input 
     */
    public Mono<Owner> createOwner(@NotNull Owner owner) {
        return Mono.from(ownerDao.upsert(MappingUtils.mapOwnerAsEntity(owner)))
                   .map(rr -> owner);
    }
    
    /**
     * Update the owner general informations. 
     * The underlying Pets and Visit are NOT updated.
     *
     * @param owner
     *      current owner
     * @return
     *      the owner updated and populated with visits and pets.
     */
    public Mono<Owner> updateOwner(@NotNull Owner owner) {
        return Mono.from(ownerDao.upsert(MappingUtils.mapOwnerAsEntity(owner)))
                   .map(rr -> owner)
                   .flatMap(petDao::populatePetsForOwner);
    }

    /**
     * Delete an owner based on his unique identifier. Pets and visits are NOT deleted in cascade. 
     *
     * @param ownerId
     *      unique owner identifier
     * @return
     *      a Mono (Reactor) with {@link Owner} containing only the identifier.
     */
    public Mono<Boolean> deleteOwner(@NotBlank String ownerId) {
        return Mono.from(ownerDao.delete(new OwnerEntity(UUID.fromString(ownerId))))
                   .map(rr -> rr.wasApplied());
    }

    /**
     * Populate the owner objects with Pets and then Visits user other tables.
     */
    private Mono<Owner> populateOwner(@NotNull Owner wbo) {
        return Flux.from(petDao.findByOwnerId(wbo.getId()))
                .map(MappingUtils::mapEntityAsPet)
                .flatMap(visitDao::populateVisitsForPet)
                .collect((Supplier<Set<Pet>>) HashSet::new, Set::add)
                .doOnNext(wbo::setPets)
                .map(set -> wbo);
    }

}
