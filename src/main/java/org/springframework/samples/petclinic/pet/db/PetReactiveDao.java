package org.springframework.samples.petclinic.pet.db;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.visit.Visit;

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
    MappedReactiveResultSet<PetEntity> findAllReactive();
    
    @Select(customWhereClause = PET_ATT_PET_ID + "= :petId", allowFiltering = true)
    MappedReactiveResultSet<PetEntity> findByPetIdReactiveRs(UUID petId);
    default Mono<PetEntity> findByPetIdReactive(UUID petId) {
        return Mono.from(findByPetIdReactiveRs(petId));
    }

    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId", allowFiltering = true)
    MappedReactiveResultSet<PetEntity> findAllByOwnerIdReactiveRs(UUID ownerId);
    default Flux<PetEntity> findAllByOwnerIdReactive(UUID ownerId) {
        return Flux.from(findAllByOwnerIdReactiveRs(ownerId));
    }
    
    @Update
    ReactiveResultSet updateReactive(PetEntity pet);
    default Mono<PetEntity> save(PetEntity pet) {
        return Mono.from(updateReactive(pet)).map(rr -> pet);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(PetEntity pet);
    default Mono<Boolean> delete(PetEntity pet) {
        return Mono.from(deleteReactive(pet)).map(rr -> rr.wasApplied());
    }
    
    default Mono<Owner> populatePetsForOwner(Owner wbo) {
        return findAllByOwnerIdReactive(wbo.getId())
                    .map(MappingUtils::mapEntityAsPet)
                    .collect((Supplier<Set<Pet>>) HashSet::new, Set::add)
                    .doOnNext(wbo::setPets)
                    .map(set -> wbo);
    }
    
    default Mono<Visit> populatePetForVisit(Visit wbv) {
        return findByPetIdReactive(wbv.getPet().getId())
                    .map(MappingUtils::mapEntityAsPet)
                    .doOnNext(wbv::setPet)
                    .map(set -> wbv);
    }
    
}
