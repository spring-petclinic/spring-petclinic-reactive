package org.springframework.samples.petclinic.visit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.WebBeanPet;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Dao
public interface VisitReactiveDao extends CassandraPetClinicSchema {
    
    @Select
    MappedReactiveResultSet<Visit> findAllReactive();
    
    @Select(customWhereClause = VISIT_ATT_PET_ID + "= :petId")
    MappedReactiveResultSet<Visit> findAllVisitsByPetIdReactive(UUID petId);
    default Flux<Visit> findAllVisitsForAPet(UUID petId) {
        return Flux.from(findAllVisitsByPetIdReactive(petId));
    }

    @Select(customWhereClause = VISIT_ATT_VISIT_ID + "= :visitId", allowFiltering = true)
    MappedReactiveResultSet<Visit> findByVisitIdReactiveRs(UUID visitId);
    default Mono<Visit> findByVisitIdReactive(UUID visitId) {
        return Mono.from(findByVisitIdReactiveRs(visitId));
    }
    
    @Update
    ReactiveResultSet updateReactive(Visit visit);
    default Mono<Visit> save(Visit visit) {
        return Mono.from(updateReactive(visit)).map(rr -> visit);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(Visit visit);
    default Mono<Boolean> delete(Visit visit) {
        return Mono.from(deleteReactive(visit))
                   .map(rr -> rr.wasApplied());
    }
    
    default Mono<WebBeanPet> populateVisitsForPet(WebBeanPet wbp) {
        // Flux<Visit> 
        return findAllVisitsForAPet(wbp.getId())
            // Flux<VisitWebBean>
            .map(MappingUtils::fromVisitEntityToWebBean)
            // Mono<HashSet<Object>>
            .collect((Supplier<Set<WebBeanVisit>>) HashSet::new, Set::add)
            // Populate input
            .doOnNext(wbp::setVisits)
            // return object populated
            .map(set -> wbp);
    }
    
    default Mono<Owner> populateVisitsForOwner(Owner wbo) {
        wbo.getPets().forEach(this::populateVisitsForPet);
        return Mono.just(wbo);
    }
    
}
