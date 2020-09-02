package org.springframework.samples.petclinic.pet;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.visit.WebBeanVisit;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Dao
public interface PetReactiveDao extends CassandraPetClinicSchema {
    
    @Select
    MappedReactiveResultSet<Pet> findAllReactive();
    
    @Select(customWhereClause = PET_ATT_PET_ID + "= :petId", allowFiltering = true)
    MappedReactiveResultSet<Pet> findByPetIdReactiveRs(UUID petId);
    default Mono<Pet> findByPetIdReactive(UUID petId) {
        return Mono.from(findByPetIdReactiveRs(petId));
    }

    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId", allowFiltering = true)
    MappedReactiveResultSet<Pet> findAllByOwnerIdReactiveRs(UUID ownerId);
    default Flux<Pet> findAllByOwnerIdReactive(UUID ownerId) {
        return Flux.from(findAllByOwnerIdReactiveRs(ownerId));
    }
    
    @Update
    ReactiveResultSet updateReactive(Pet pet);
    default Mono<Pet> save(Pet pet) {
        return Mono.from(updateReactive(pet)).map(rr -> pet);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(Pet pet);
    default Mono<Boolean> delete(Pet pet) {
        return Mono.from(deleteReactive(pet)).map(rr -> rr.wasApplied());
    }
    
    default Mono<Owner> populatePetsForOwner(Owner wbo) {
        return findAllByOwnerIdReactive(wbo.getId())
                    .map(MappingUtils::fromPetEntityToWebBean)
                    .collect((Supplier<Set<WebBeanPet>>) HashSet::new, Set::add)
                    .doOnNext(wbo::setPets)
                    .map(set -> wbo);
    }
    
    default Mono<WebBeanVisit> populatePetForVisit(WebBeanVisit wbv) {
        return findByPetIdReactive(wbv.getPet().getId())
                    .map(MappingUtils::fromPetEntityToWebBean)
                    .doOnNext(wbv::setPet)
                    .map(set -> wbv);
    }
    
}
