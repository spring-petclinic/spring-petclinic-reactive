package org.springframework.samples.petclinic.pet.db;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_BIRTHDATE;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_NAME;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_OWNER_ID;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_PET_ID;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_ATT_PET_TYPE;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_INDEX;
import static org.springframework.samples.petclinic.pet.db.PetEntity.PET_TABLE;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * DataStax Java driver mapper for Pet. The mapper generates the 
 * boilerplate to execute queries and convert the results into 
 * application-level objects.
 * 
 * Documentation of the mapper:
 * - {@link https://docs.datastax.com/en/developer/java-driver/4.9/manual/mapper/}
 */
@Mapper
public interface PetReactiveDaoMapper {
    
    @DaoFactory
    PetReactiveDao petDao(@DaoKeyspace CqlIdentifier keyspace);
    
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchema(CqlSession cqlSession) {
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
        
        /**
         * The UI needs to retrieve a Pet with only the id. As this is
         * not the primary key but the cardinality is low we create a
         * secondary index.
         */
        cqlSession.execute(createIndex(PET_INDEX).ifNotExists()
                .onTable(PET_TABLE)
                .andColumn(PET_ATT_PET_ID)
                .build());
    }
    
}
