package org.springframework.samples.petclinic.visit;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of services for Visit
 * 
 * This class leverages on multiple Dao (visit, pet, reference lists) 
 * to compose data needed at UI level.
 */
@Component
@Validated
public class VisitReactiveServices {
    
    /** Implementation of Crud operations for pets. */
    private PetReactiveDao petDao;
    
    /** Implementation of Crud operations for visits. */
    private VisitReactiveDao visitDao;
    
    /**
     * Visit services.
     */
    public VisitReactiveServices(PetReactiveDao petDao, VisitReactiveDao visitDao) {
        this.petDao = petDao;
        this.visitDao = visitDao;
    }
    
    /**
     * Retrieve all visits from the database.
     * 
     * @return
     *      a Flux (reactor) with the stream of Owners.
     */
    public Flux<Visit> findAllVisits() {
        return Flux.from(visitDao.findAll())
                   .map(MappingUtils::mapEntityAsVisit)
                   .flatMap(petDao::populatePetForVisit);
    }
    
    /**
     * Retrieve a visit from its identifier in DB.
     * 
     * @return
     *      a Mono of Visit or empty
     */
    public Mono<Visit> findVisitById(@NotBlank String visitId) {
        return Mono.from(visitDao.findByVisitById((UUID.fromString(visitId))))
                   .map(MappingUtils::mapEntityAsVisit)
                   .flatMap(petDao::populatePetForVisit);
    }
    
    public Mono<Visit> createVisit(Visit v) {
        return null;
    }
    
    public Mono<Void> deleteVisitById(UUID visitId) {
        return null;
    }
    
    /*
    VisitEntity v = MappingUtils.fromVisitWebBeanCreationToEntity(dto);
    
    return visitDao.save(v)
            .map(MappingUtils::mapEntityToVisit)
           
*/
}
