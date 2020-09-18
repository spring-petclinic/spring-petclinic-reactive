package org.springframework.samples.petclinic.pet.db;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.Visit;

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
 * DAO implement operations to interact with table 'petclinic_pet_by_owner'
 * 
 * A DAO is an interface that defines a set of query methods. 
 * DataStax driver will generate the proper implementation at compile time using an annotation processor.
 * More documentation here {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/mapper/daos/daos/}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Dao
public interface PetReactiveDao extends CassandraPetClinicSchema {
    
    /**
     * Find all records in the table (be careful if the data does have a lot of record and use paging)
     */
    @Select
    MappedReactiveResultSet<PetEntity> findAllReactive();
    
    /**
     * Find all pets for a owner
     */
    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId")
    MappedReactiveResultSet<PetEntity> findAllByOwnerId(UUID ownerId);
    
    /**
     * Find a pet from its unique identifier id.
     * 
     * As pet is not the partition key (PRIMARY KEY ((owner_id), pet_id))
     * we add the ALLOW FILTERING clause. 
     */
    @Select(customWhereClause = PET_ATT_PET_ID + "= :petId", allowFiltering = true)
    MappedReactiveResultSet<PetEntity> findByPetIdReactive(UUID petId);

    /**
     * Update are upsert in Cassandra with no read-before-write
     */
    @Update
    ReactiveResultSet upsertReactive(PetEntity pet);

    /**
     * Delete a Pet.
     * We need the full primary key here, with both ownerid and petid.
     */
    @Delete
    ReactiveResultSet deleteReactive(PetEntity pet);
    
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchemaPet(CqlSession cqlSession) {
        /**
         * CREATE TABLE IF NOT EXISTS petclinic_pet_by_owner (
         *  owner_id   uuid,
         *  pet_id     uuid,
         *  pet_type   text,
         *  name       text,
         *  birth_date date,
         *  PRIMARY KEY ((owner_id), pet_id)
         * ); */
        cqlSession.execute(createTable(PET_TABLE)
                .ifNotExists()
                .withPartitionKey(PET_ATT_OWNER_ID, DataTypes.UUID)
                .withClusteringColumn(PET_ATT_PET_ID, DataTypes.UUID)
                .withColumn(PET_ATT_PET_TYPE, DataTypes.TEXT)
                .withColumn(PET_ATT_NAME, DataTypes.TEXT)
                .withColumn(PET_ATT_BIRTHDATE, DataTypes.DATE)
                .build());
    }
    
    /**
     * N+1 select pattern to retrieve pets associated to an Owner.
     *
     * There are no joins in Cassandra but we can leverage the 
     * reactive to fetch multiple entities at the same time.
     */
    default Mono<Owner> populatePetsForOwner(Owner wbo) {
        return Flux.from(findAllByOwnerId(wbo.getId()))
                    .map(MappingUtils::mapEntityAsPet)
                    .collect((Supplier<Set<Pet>>) HashSet::new, Set::add)
                    .doOnNext(wbo::setPets)
                    .map(set -> wbo);
    }
    
    /**
     * N+1 select pattern to the pet informations related to the Visit
     *
     * There are no joins in Cassandra but we can leverage the 
     * reactive to fetch multiple entities at the same time.
     */
    default Mono<Visit> populatePetForVisit(Visit wbv) {
        return Mono.from(findByPetIdReactive(wbv.getPet().getId()))
                    .map(MappingUtils::mapEntityAsPet)
                    .doOnNext(wbv::setPet)
                    .map(set -> wbv);
    }
    
}
