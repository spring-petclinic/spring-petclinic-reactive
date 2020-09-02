package org.springframework.samples.petclinic.owner.db;

import static com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy.DO_NOT_SET;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.DefaultNullSavingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Dao
@DefaultNullSavingStrategy(DO_NOT_SET)
public interface OwnerReactiveDao extends CassandraPetClinicSchema {
    
    @Select
    MappedReactiveResultSet<OwnerEntity> findByIdReactive(UUID ownerid);
    default Mono<OwnerEntity> findById(UUID ownerid) {
        return Mono.from(findByIdReactive(ownerid));
    }
    
    @Select
    MappedReactiveResultSet<OwnerEntity> findAllReactive();
    default Flux<OwnerEntity> findAll() {
        return Flux.from(findAllReactive());
    }
    
    @Select(customWhereClause = OWNER_ATT_LASTNAME + "= :ownerLastname")
    Flux<OwnerEntity> searchByOwnerName(String ownerLastname);
    
    @Update
    ReactiveResultSet updateReactive(OwnerEntity owner);
    default Mono<OwnerEntity> save(OwnerEntity owner) {
        // must be applied as not LWT, no checks
        return Mono.from(updateReactive(owner))
                   .map(rr -> owner);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(OwnerEntity owner);
    default Mono<Boolean> delete(OwnerEntity owner) {
        return Mono.from(deleteReactive(owner))
                   .map(rr -> rr.wasApplied());
    }
    
}
