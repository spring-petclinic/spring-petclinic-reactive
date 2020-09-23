package org.springframework.samples.petclinic.visit.db;

import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_PET_ID;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_VISIT_ID;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.Visit;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Definition of operations relative to table 'petclinic_visit_by_pet'. 
 * 
 * The DataStax Cassandra driver will generate the implementation at compile time.
 * More information can be found {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/select/#return-type}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
public interface VisitReactiveDao {
    
    @Select
    MappedReactiveResultSet<VisitEntity> findAll();
    
    @Select(customWhereClause = VISIT_ATT_PET_ID + "= :petId")
    MappedReactiveResultSet<VisitEntity> findAllVisitsForAPet(UUID petId);
    
    /**
     * Find a visit from its unique identifier id.
     * 
     * As pet is not the partition key and cardinality is low
     * we created a secondary index and `allowFiltering` is needed only as start up
     * before tables exist.
     */
    @Select(customWhereClause = VISIT_ATT_VISIT_ID + "= :visitId", allowFiltering = true)
    MappedReactiveResultSet<VisitEntity> findVisitById(UUID visitId);
    
    @Update
    ReactiveResultSet upsert(VisitEntity visit);
    
    @Delete
    ReactiveResultSet delete(VisitEntity visit);
    
    /**
     * A pet can have multiple visits. (1..n).
     * 
     * We populate the input with the list of visit computed
     * from 'findAllVisitsForAPet' and a bit of mapping.
     */
    default Mono<Pet> populateVisitsForPet(Pet wbp) {
        // Flux<VisitEntity> 
        return Flux.from(findAllVisitsForAPet(wbp.getId()))
            // Flux<Visit>
            .map(MappingUtils::mapEntityAsVisit)
            // Mono<HashSet<Visits>>
            .collect((Supplier<Set<Visit>>) HashSet::new, Set::add)
            // Populate input
            .doOnNext(wbp::setVisits)
            // return object populated
            .map(set -> wbp);
    }
    
    /**
     * An owner contains multiple Pets and then each pet has some visits.
     * 
     * The tree of dependents objects are loaded from this method.
     */
    default Mono<Owner> populateVisitsForOwner(Owner wbo) {
        wbo.getPets().forEach(this::populateVisitsForPet);
        return Mono.just(wbo);
    }
    
}
