package org.springframework.samples.petclinic.vet.db;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

/**
 * Definition of operations relative to table 'petclinic_vet'. 
 * 
 * The DataStax Cassandra driver will generate the implementation at compile time.
 * More information can be found {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/select/#return-type}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
public interface VetReactiveDao {
    
    /**
     * Find an Vet by its unique identifier.
     *
     * The Dao layer returned a Publisher<T> and not Flux<T> nor Mono<T>
     * as those are relative to Reactor only (not Rx Java).
     */
    @Select
    MappedReactiveResultSet<VetEntity> findById(@NotNull UUID vetId);
    
    /**
     * Retrieve all vet from table 'petclinic_vet'. 
     * 
     * Note that a query of select all with no where clause is not 
     * recommended with Cassandra as it cal lead to large number of record. 
     */
    @Select
    MappedReactiveResultSet<VetEntity> findAll();
    
    /**
     * Upsert Vet record.
     */
    @Update
    ReactiveResultSet upsert(VetEntity vet);
    
    /**
     * Delete Vet record.
     */
    @Delete
    ReactiveResultSet delete(VetEntity vet);
    
}
