package org.springframework.samples.petclinic.owner.db;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerReactiveServices;
import org.springframework.samples.petclinic.pet.PetReactiveDao;
import org.springframework.samples.petclinic.pet.WebBeanPet;
import org.springframework.samples.petclinic.visit.VisitReactiveDao;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of public {@link OwnerReactiveServices} with code Cassandra Driver.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Component
public class OwnerReactiveServicesImpl implements OwnerReactiveServices {

    /** Implementation of Crud operations for owners. */
    private OwnerReactiveDao ownerDao;
    
    /** Implementation of Crud operations for pets. */
    private PetReactiveDao petDao;
    
    /** Implementation of Crud operations for visits. */
    private VisitReactiveDao visitDao;
    
    /**
     * Injection with controller
     */
    public OwnerReactiveServicesImpl(OwnerReactiveDao oDao, PetReactiveDao pDao, VisitReactiveDao vDao) {
        this.ownerDao = oDao;
        this.petDao   = pDao;
        this.visitDao = vDao;
    }

    /** {@inheritDoc} */
    @Override
    public Flux<Owner> findOwnersByName(String searchString) {
        Objects.requireNonNull(searchString);
        return Flux.from(ownerDao.searchByOwnerName(searchString))
                   .map(MappingUtils::mapEntityAsOwner);
    }
    
    /** {@inheritDoc} */
    @Override
    public Flux<Owner> findAllOwners() {
        return Flux.from(ownerDao.findAllReactive())
                   .map(MappingUtils::mapEntityAsOwner)
                   .flatMap(petDao::populatePetsForOwner);  
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Owner> findOwnerById(String ownerId) {
        Objects.requireNonNull(ownerId);
        return Mono.from(ownerDao.findByIdReactive(UUID.fromString(ownerId)))
                   .map(MappingUtils::mapEntityAsOwner)
                   .flatMap(this::populateOwner);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Owner> createOwner(Owner owner) {
        Objects.requireNonNull(owner);
        return ownerDao.save(MappingUtils.mapOwnerAsEntity(owner))
                       .map(MappingUtils::mapEntityAsOwner);
    }
    
    /** {@inheritDoc} */
    @Override
    public Mono<Owner> updateOwner(Owner owner) {
        Objects.requireNonNull(owner);
        return ownerDao.save(MappingUtils.mapOwnerAsEntity(owner))
                       .map(MappingUtils::mapEntityAsOwner)
                       .flatMap(petDao::populatePetsForOwner);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> deleteOwner(String ownerId) {
        Objects.requireNonNull(ownerId);
        return ownerDao.delete(new OwnerEntity(UUID.fromString(ownerId)));
    }

    // TODO: inherit doc?
    private Mono<Owner> populateOwner(Owner wbo) {
        return petDao.findAllByOwnerIdReactive(wbo.getId())
                .map(MappingUtils::fromPetEntityToWebBean)
                .flatMap(visitDao::populateVisitsForPet)
                .collect((Supplier<Set<WebBeanPet>>) HashSet::new, Set::add)
                .doOnNext(wbo::setPets)
                .map(set -> wbo);
    }
    
    

}
