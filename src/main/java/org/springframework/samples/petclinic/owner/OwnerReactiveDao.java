package org.springframework.samples.petclinic.owner;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Dao
public interface OwnerReactiveDao extends CassandraPetClinicSchema {
    
    @Select
    MappedReactiveResultSet<Owner> findByIdReactive(UUID ownerid);
   
    default Mono<Owner> findById(UUID ownerid) {
        return Mono.from(findByIdReactive(ownerid));
    }
    
    @Select
    MappedReactiveResultSet<Owner> findAllReactive();
    
    default Flux<Owner> findAll() {
        return Flux.from(findAllReactive());
    }
    
    /**
     * A secondary index has been defined on this column*
     */
    @Select(customWhereClause = OWNER_ATT_LASTNAME + "= :ownerName")
    MappedReactiveResultSet<Owner> findByOwnerNameReactive(String ownerName);
    
    default Flux<Owner> searchByName(String ownerName) {
        return Flux.from(findByOwnerNameReactive(ownerName));
    }
    
    @Update
    ReactiveResultSet updateReactive(Owner owner);
    
    default Mono<Owner> save(Owner owner) {
        // must be applied as not LWT, no checks
        return Mono.from(updateReactive(owner))
                   .map(rr -> owner);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(Owner owner);
    
    default Mono<Boolean> delete(Owner owner) {
        return Mono.from(deleteReactive(owner))
                   .map(rr -> rr.wasApplied());
    }
    
}
