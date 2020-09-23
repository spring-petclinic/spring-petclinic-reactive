package org.springframework.samples.petclinic.pet.db;

import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_OWNER_ID;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_PET_ID;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.utils.MappingUtils;
import org.springframework.samples.petclinic.visit.Visit;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
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
public interface PetReactiveDao {
    
    /**
     * Find all records in the table.
     * 
     * @implNote Be careful if your data is large or you work with a large cluster,
     * the function do a full scan cluster but paging is not implemented 
     * in the Angular UI.
     * 
     * @return
     *      the resultSet not exhausted to get the infos
     */
    @Select
    MappedReactiveResultSet<PetEntity> findAll();
    
    /**
     * Find all pets for a owner
     */
    @Select(customWhereClause = PET_ATT_OWNER_ID + "= :ownerId")
    MappedReactiveResultSet<PetEntity> findByOwnerId(UUID ownerId);
    
    /**
     * Find a pet from its unique identifier id.
     * 
     * As pet is not the partition key and cardinality is low
     * we created a secondary index and `allowFiltering` is needed only as start up
     * before tables exist.
     */
    @Select(customWhereClause = PET_ATT_PET_ID + "= :petId", allowFiltering = true)
    MappedReactiveResultSet<PetEntity> findByPetId(UUID petId);

    /**
     * Update are upsert in Cassandra with no read-before-write
     */
    @Update
    ReactiveResultSet upsert(PetEntity pet);

    /**
     * Delete a Pet.
     * We need the full primary key here, with both ownerid and petid.
     */
    @Delete
    ReactiveResultSet delete(PetEntity pet);
    
    /**
     * N+1 select pattern to retrieve pets associated to an Owner.
     *
     * There are no joins in Cassandra but we can leverage the 
     * reactive to fetch multiple entities at the same time.
     */
    default Mono<Owner> populatePetsForOwner(Owner wbo) {
        return Flux.from(findByOwnerId(wbo.getId()))
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
        return Mono.from(findByPetId(wbv.getPet().getId()))
                    .map(MappingUtils::mapEntityAsPet)
                    .doOnNext(wbv::setPet)
                    .map(set -> wbv);
    }
    
}
