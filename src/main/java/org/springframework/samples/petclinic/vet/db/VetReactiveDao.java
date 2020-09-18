package org.springframework.samples.petclinic.vet.db;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Definition of operations relative to table 'petclinic_vet'. 
 * 
 * The DataStax Cassandra driver will generate the implementation at compile time.
 * More information can be found {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/select/#return-type}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
public interface VetReactiveDao extends CassandraPetClinicSchema {
    
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchemaVet(CqlSession cqlSession) {
        cqlSession.execute(
                createTable(VET_TABLE).ifNotExists()
                .withPartitionKey(VET_ATT_ID, DataTypes.UUID)
                .withColumn(VET_ATT_FIRSTNAME, DataTypes.TEXT)
                .withColumn(VET_ATT_LASTNAME, DataTypes.TEXT)
                .withColumn(VET_ATT_SPECIALTIES, DataTypes.setOf(DataTypes.TEXT))
                .build());
        cqlSession.execute( 
                createIndex(VET_IDX_NAME).ifNotExists()
                .onTable(VET_TABLE)
                .andColumn(VET_ATT_LASTNAME)
                .build());
        /**
         * CREATE TABLE IF NOT EXISTS petclinic_vet_by_specialty (
         *      specialty   text,
         *      vet_id          uuid,
         *      first_name  text,
         *      last_name   text,
         *      PRIMARY KEY ((specialty), vet_id)
         *); */
        cqlSession.execute(
                createTable(VET_SPECIALTY_TABLE).ifNotExists()
                .withPartitionKey(VET_SPECIALTY_ATT_SPECIALTY, DataTypes.TEXT)
                .withClusteringColumn(VET_SPECIALTY_ATT_VETID, DataTypes.UUID)
                .withColumn(VET_SPECIALTY_ATT_LASTNAME, DataTypes.TEXT)
                .withColumn(VET_SPECIALTY_ATT_FIRSTNAME, DataTypes.TEXT)
                .build());
    }
    
    /**
     * Look for a record based on its id.
     */
    @Select
    MappedReactiveResultSet<VetEntity> findByIdReactive(UUID vetid);
    
    @Select
    MappedReactiveResultSet<VetEntity> findAllReactive();
    
    default Flux<VetEntity> findAll() {
        return Flux.from(findAllReactive());
    }
    
    @Update
    ReactiveResultSet updateReactive(VetEntity vet);
    
    default Mono<VetEntity> save(VetEntity vet) {
        // TODO: should we explain more about what is meant by LWT or even add doc link?
        // must be applied as not LWT, no checks
        return Mono.from(updateReactive(vet))
                   .map(rr -> vet);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(VetEntity vet);
    
    default Mono<Boolean> delete(VetEntity vet) {
        return Mono.from(deleteReactive(vet))
                   .map(rr -> rr.wasApplied());
    }
    
}
