package org.springframework.samples.petclinic.pet.db;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetReactiveServices;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PetReactiveServicesImpl implements PetReactiveServices {

    /** Implementation of Crud for repo. */
    private PetReactiveDao petDao;
    
    /** Implementation of Crud for repo. */
    private VisitReactiveDao visitDao;
    
    /** List available lists. */
    private ReferenceListReactiveDao refDao;

    /**
     * Inject through constructor.
     */
    public PetReactiveServicesImpl(PetReactiveDao petDao, 
            VisitReactiveDao visitDao, ReferenceListReactiveDao refList) {
        this.petDao   = petDao;
        this.visitDao = visitDao;
        this.refDao   = refList;
    }
    
    /** {@inheritDoc} */
    @Override
    public Flux<Pet> findAllPets() {
        return Flux.from(petDao.findAllReactive())
                   .map(MappingUtils::mapEntityAsPet)
                   .flatMap(visitDao::populateVisitsForPet);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Pet> findPetByPetId(UUID petId) {
        return petDao.findByPetIdReactive(petId)
                     .map(MappingUtils::mapEntityAsPet)
                     .flatMap(visitDao::populateVisitsForPet);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Set<PetType>> findAllPetTypes() {
        return refDao.listPetType()
                     .map(Set::stream)
                     .map(s -> s.map(PetType::new).collect(Collectors.toSet()));
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Pet> createPet(Pet pet) {
        Objects.requireNonNull(pet);
        return petDao.save(MappingUtils.mapPetAsEntity(pet))
                     .map(MappingUtils::mapEntityAsPet);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Boolean> deletePetById(UUID petId) {
        return petDao.findByPetIdReactive(petId).flatMap(petDao::delete);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Set<String>> listPetType() {
        return refDao.listPetType();
    }

    /** {@inheritDoc} */
    @Override
    public Mono<PetType> addPetType(PetType value) {
       return refDao.addPetType(value.getName()).thenReturn(value);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<PetType> replacePetType(String oldValue, String newValue) {
        return refDao.replacePetType(oldValue, newValue).map(PetType::new);
    }

    /** {@inheritDoc} */
    @Override
    public Mono<Void> removePetType(String value) {
        return refDao.removePetType(value);
    }
    
}
