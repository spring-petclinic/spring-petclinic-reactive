package org.springframework.samples.petclinic.vet.db;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_ATT_FIRSTNAME;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_ATT_ID;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_ATT_LASTNAME;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_ATT_SPECIALTIES;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_IDX_NAME;
import static org.springframework.samples.petclinic.vet.db.VetEntity.VET_TABLE;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * DataStax Java driver mapper for Vets. The mapper generates the 
 * boilerplate to execute queries and convert the results into 
 * application-level objects.
 * 
 * Documentation of the mapper:
 * - {@link https://docs.datastax.com/en/developer/java-driver/4.9/manual/mapper/}
 */
@Mapper
public interface VetReactiveDaoMapper {
    
    @DaoFactory
    VetReactiveDao vetDao(@DaoKeyspace CqlIdentifier keyspace);
    
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchema(CqlSession cqlSession) {
        
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
    }
     
}
