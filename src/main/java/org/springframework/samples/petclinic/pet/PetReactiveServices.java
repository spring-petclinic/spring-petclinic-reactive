package org.springframework.samples.petclinic.pet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.samples.petclinic.pet.db.PetEntity;
import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of services for Pet. 
 * 
 * This class leverages on multiple Dao (pet, visit, reference lists) 
 * to compose data needed at UI level.
 */
@Component
@Validated
public class PetReactiveServices implements InitializingBean {
    
    /** Implementation of Crud for pets. */
    private PetReactiveDao petDao;
    
    /** Implementation of Crud for visits. */
    private VisitReactiveDao visitDao;
    
    /** Implementation of CRUD operations for references lists (here for pet types). */
    private ReferenceListReactiveDao refDao;

    /** Use ReferenceListReactiveDao to retrieve this list. */
    public static final String PET_TYPE = "pet_type";
    
    /** Init reference list of pet types with those default values. */
    public Set<String> default_pets_types = 
            new HashSet<>(Arrays.asList("bird", "cat", "dog", "hamster", "lizard", "snake"));
    
    /**
     * Inject through constructor.
     */
    public PetReactiveServices(PetReactiveDao petDao, VisitReactiveDao visitDao, 
                               ReferenceListReactiveDao refList) {
        this.petDao   = petDao;
        this.visitDao = visitDao;
        this.refDao   = refList;
    }
    
    /**
     * Spring interface {@link InitializingBean} let you execute some
     * coode after bean has been initialized.
     * 
     * Here we enter default values for pet list.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        refDao.saveList(PET_TYPE, default_pets_types).subscribe();
    }
    
    // --- Operations on Pets ---
    
    /**
     * Fill all pets in the DB.
     * 
     * @implNote Be careful if your data is large or you work with a large cluster,
     * the function do a full scan cluster but paging is not implemented 
     * in the Angular UI.
     */
    public Flux<Pet> findAllPets() {
        return Flux.from(petDao.findAll())
                   .map(MappingUtils::mapEntityAsPet)
                   .flatMap(visitDao::populateVisitsForPet);
    }

    /**
     * Find a pet from its identifier or empty.
     */
    public Mono<Pet> findPetByPetId(UUID petId) {
        return Mono.from(petDao.findByPetId(petId))
                     .map(MappingUtils::mapEntityAsPet)
                     .flatMap(visitDao::populateVisitsForPet);
    }

    /**
     * List all pets types from DB. This list will be used in the
     * user interface as a dropdown combo element.
     * 
     * @return
     *      a set of PetType
     */
    public Mono<Set<PetType>> findAllPetTypes() {
        return refDao.findReferenceList(PET_TYPE)
                     .map(Set::stream)
                     .map(s -> s.map(PetType::new)
                     .collect(Collectors.toSet()));
    }
    
    public Mono<Pet> createPet(Pet pet) {
        Objects.requireNonNull(pet);
        PetEntity pe = MappingUtils.mapPetAsEntity(pet);
        return Mono.from(petDao.upsert(pe))
                   .map(rr -> pe)
                   .map(MappingUtils::mapEntityAsPet);
    }

    public Mono<Void> deletePetById(@NotNull UUID petId) {
        return Mono.from(petDao.findByPetId(petId)).map(petDao::delete).then();
    }

    // -- Operations on Pet List reference. 
    
    public Mono<Set<String>> listPetType() {
        return refDao.findReferenceList(PET_TYPE);
    }

    public Mono<PetType> addPetType(PetType value) {
       return refDao.addToReferenceList(PET_TYPE, value.getName())
                    .thenReturn(value);
    }

    public Mono<PetType> replacePetType(String oldValue, String newValue) {
        return refDao.removeFromReferenceList(PET_TYPE, oldValue)
                     .then(refDao.addToReferenceList(PET_TYPE, newValue))
                     .map(PetType::new);
    }

    public Mono<Void> removePetType(String value) {
        return refDao.removeFromReferenceList(PET_TYPE, value);
    }

    
    
}
