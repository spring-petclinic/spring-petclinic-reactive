package org.springframework.samples.petclinic.owner.db;

import static com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy.DO_NOT_SET;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_LASTNAME;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.DefaultNullSavingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;

/**
 * Definition of operations relative to table 'petclinic_owner'. 
 * 
 * The DataStax Cassandra driver will generate the implementation at compile time.
 * More information can be found 
 * {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/select/#return-type}
 */
@Dao
@Validated
@DefaultNullSavingStrategy(DO_NOT_SET)
public interface OwnerReactiveDao {
    
    /**
     * Find an owner by its unique identifier.
     *
     * The Dao layer returned a Publisher<T> and not Flux<T> nor Mono<T>
     * as those are relative to Reactor only (not Rx Java).
     */
    @Select
    MappedReactiveResultSet<OwnerEntity> findById(@NotNull UUID ownerid);
    
    /**
     * Retrieve all owners from table 'petclinic_owner'. 
     * 
     * Note that a query of select all with no where clause is not 
     * recommended with Cassandra as it cal lead to large number of record. 
     */
    @Select
    MappedReactiveResultSet<OwnerEntity> findAll();
    
    /**
     * Retrieve all owners matching the last_name clause. As this column
     * is not part of the primary key a secondary index has been created.
     */
    @Select(customWhereClause = OWNER_ATT_LASTNAME + "= :ownerLastname")
    Flux<OwnerEntity> searchByOwnerName(String ownerLastname);
    
    /**
     * Upsert owner record.
     */
    @Update
    ReactiveResultSet upsert(OwnerEntity owner);
    
    /**
     * Delete owner record.
     */
    @Delete
    ReactiveResultSet delete(OwnerEntity owner);
    
}
