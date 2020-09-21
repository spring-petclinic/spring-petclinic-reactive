package org.springframework.samples.petclinic.vet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.samples.petclinic.reflist.ReferenceListReactiveDao;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.vet.db.VetEntity;
import org.springframework.samples.petclinic.vet.db.VetReactiveDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of services for Vet. 
 * 
 * This class leverages on multiple Dao (pet, reference lists) 
 * to compose data needed at UI level.
 */
@Component
@Validated
public class VetReactiveServices implements InitializingBean {
    
    /** Group constants relative to Data Model in single place for the domain. */
    public static final String VET_SPECIALTY = "vet_specialty";
    
    /** Default values for the vet specialtiess. */
    public Set<String> default_vet_specialties = new HashSet<>(
            Arrays.asList("dentistry", "radiology", "surgery"));
    
    /** Implementation of Crud operations for vets. */
    private final VetReactiveDao vetDao;
    
    /** Implementation of CRUD operations for references lists (here for vet specialties). */
    private ReferenceListReactiveDao refDao;
    
    /**
     * Constructor woith Implementations or Dao.
     * @param vetRepo
     */
    public VetReactiveServices(VetReactiveDao vetRepo, ReferenceListReactiveDao refDao) {
        this.vetDao = vetRepo;
        this.refDao  = refDao;
    }
    
    /**
     * Spring interface {@link InitializingBean} let you execute some
     * coode after bean has been initialized.
     * 
     * Here we enter default values for Vet and Vet Specialties.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        
        // Ref list
        refDao.saveList(VET_SPECIALTY, default_vet_specialties).subscribe();
        
        // Add all vets entities
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("11111111-1111-1111-1111-111111111111"),
                "James", "Carter", new HashSet<String>()))).subscribe();
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("22222222-2222-2222-2222-222222222222"), 
                "Helen", "Leary", new HashSet<String>(Arrays.asList("radiology"))))).subscribe();
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("33333333-3333-3333-3333-333333333333"), 
                "Linda", "Douglas", new HashSet<String>(Arrays.asList("dentistry", "surgery"))))).subscribe();
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("44444444-4444-4444-4444-444444444444"), 
                "Rafael", "Ortega", new HashSet<String>(Arrays.asList("surgery"))))).subscribe();
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("55555555-5555-5555-5555-555555555555"),
                "Henry", "Stevens", new HashSet<String>(Arrays.asList("radiology"))))).subscribe();
        Mono.from(vetDao.upsert(new VetEntity(UUID.fromString("66666666-6666-6666-6666-666666666666"),
                "Sharon", "Jenkins", new HashSet<String>()))).subscribe();
    }
    
    /**
     * Retrieve all owners from the database.
     * 
     * @return
     *      a Flux (reactor) with the stream of Owners.
     */
    public Flux<Vet> findAllVets() {
        return Flux.from(vetDao.findAll())
                   .map(MappingUtils::mapEntityAsVet);  
    }
    
    /**
     * Find a Vet from its unique identifier.
     * 
     * @param vetId
     *      unique vet identifier
     * @return
     *      Mono (reactor) containing the vet or empty
     */
    public Mono<Vet> findVetById(@NotBlank String vetId) {
        return Mono.from(vetDao.findById(UUID.fromString(vetId)))
                   .map(MappingUtils::mapEntityAsVet);
    }
    
    public Mono< Vet> createVet(@NotNull Vet vet) {
        VetEntity ve = MappingUtils.mapVetAsEntity(vet);
        return Mono.from(vetDao.upsert(ve))
                   .map(rr -> ve)
                   .map(MappingUtils::mapEntityAsVet);
    }
    
    public Mono<Void> deleteVetById(@NotBlank String vetId) {
        return Mono.from(vetDao.delete(new VetEntity(vetId))).then();
        //return Mono.from(vetDao.findById(vetId))
        //           .map(vetDao::delete)
        //           .then();
    }
    
    // --- Operations on Vet Specialties ---
    
    public Mono<Set<String>> listVetSpecialtiesCodes() {
        return refDao.findReferenceList(VET_SPECIALTY);
    }
                
    public Mono<Set<VetSpecialty>> listVetSpecialties() {
        return refDao.findReferenceList(VET_SPECIALTY)
                     .map(Set::stream)
                     .map(s -> s.map(VetSpecialty::new)
                     .collect(Collectors.toSet()));
    }
    
    public Mono<String> addVetSpecialty(String value) {
        return refDao.addToReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<Void> removeVetSpecialty(String value) {
        return refDao.removeFromReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<String> replaceVetSpecialty(String oldValue, String newValue) {
        return removeVetSpecialty(oldValue).then(addVetSpecialty(newValue));
    }

    
    
}
