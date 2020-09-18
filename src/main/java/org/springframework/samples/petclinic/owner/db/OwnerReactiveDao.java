package org.springframework.samples.petclinic.owner.db;

import static com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy.DO_NOT_SET;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.validation.annotation.Validated;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.DefaultNullSavingStrategy;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Definition of operations relative to table 'petclinic_owner'. 
 * 
 * The DataStax Cassandra driver will generate the implementation at compile time.
 * More information can be found 
 * {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/select/#return-type}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
@Validated
@DefaultNullSavingStrategy(DO_NOT_SET)
public interface OwnerReactiveDao extends CassandraPetClinicSchema {
    
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
    
    @Update
    ReactiveResultSet upsert(OwnerEntity owner);
    
    @Delete
    ReactiveResultSet deleteReactive(OwnerEntity owner);
    default Mono<Boolean> delete(OwnerEntity owner) {
        return Mono.from(deleteReactive(owner))
                   .map(rr -> rr.wasApplied());
    }
    
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchemaOwner(CqlSession cqlSession) {
        /**
         * CREATE TABLE IF NOT EXISTS petclinic_owner (
         *      id         uuid,
         *      first_name text,
         *      last_name  text,
         *      address    text,
         *      city       text,
         *      telephone  text,
         *      PRIMARY KEY ((id))
         *); */
        cqlSession.execute(createTable(OWNER_TABLE).ifNotExists()
                .withPartitionKey(OWNER_ATT_ID, DataTypes.UUID)
                .withColumn(OWNER_ATT_FIRSTNAME, DataTypes.TEXT)
                .withColumn(OWNER_ATT_LASTNAME, DataTypes.TEXT)
                .withColumn(OWNER_ATT_ADDRESS, DataTypes.TEXT)
                .withColumn(OWNER_ATT_CITY, DataTypes.TEXT)
                .withColumn(OWNER_ATT_TELEPHONE, DataTypes.TEXT)
                .build());
        cqlSession.execute(createIndex(OWNER_IDX_NAME).ifNotExists()
                .onTable(OWNER_TABLE)
                .andColumn(OWNER_ATT_LASTNAME)
                .build());
    }
    
}
