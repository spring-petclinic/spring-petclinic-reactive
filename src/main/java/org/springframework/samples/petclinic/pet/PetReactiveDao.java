package org.springframework.samples.petclinic.pet;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Mono;

@Dao
public interface PetReactiveDao extends CassandraPetClinicSchema {
    
    @Select
    MappedReactiveResultSet<Pet> findAllReactive();

    @Select(customWhereClause = PET_ATT_PET_ID + "= :petId", allowFiltering = true)
    MappedReactiveResultSet<Pet> findByIdReactive(UUID petId);

    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId")
    MappedReactiveResultSet<Pet> findAllByOwnerIdReactive(UUID ownerId);
    
    @Update
    ReactiveResultSet updateReactive(Pet pet);
    
    default Mono<Pet> save(Pet pet) {
        return Mono.from(updateReactive(pet)).map(rr -> pet);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(Pet pets);
    
    default Mono<Boolean> delete(Pet pet) {
        return Mono.from(deleteReactive(pet))
                   .map(rr -> rr.wasApplied());
    }
    
}
