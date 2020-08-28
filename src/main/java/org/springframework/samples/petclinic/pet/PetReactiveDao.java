package org.springframework.samples.petclinic.pet;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Mono;

@Dao
public interface PetReactiveDao extends CassandraPetClinicSchema {
    
    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId")
    MappedReactiveResultSet<Pet> findByOwnerIdReactive(UUID ownerId);

    @Update
    ReactiveResultSet updateReactive(Pet pet);
    
    default Mono<Pet> save(Pet pet) {
        return Mono.from(updateReactive(pet)).map(rr -> pet);
    }
}
