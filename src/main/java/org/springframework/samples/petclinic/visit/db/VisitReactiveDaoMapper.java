package org.springframework.samples.petclinic.visit.db;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_DESCRIPTION;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_PET_ID;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_VISIT_DATE;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_ATT_VISIT_ID;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_IDX_VISITID;
import static org.springframework.samples.petclinic.visit.db.VisitEntity.VISIT_TABLE;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * DataStax Java driver mapper for Visit. The mapper generates the 
 * boilerplate to execute queries and convert the results into 
 * application-level objects.
 * 
 * Documentation of the mapper:
 * - {@link https://docs.datastax.com/en/developer/java-driver/4.9/manual/mapper/}
 */
@Mapper
public interface VisitReactiveDaoMapper {
    
    @DaoFactory
    VisitReactiveDao visitDao(@DaoKeyspace CqlIdentifier keyspace);
    
    /**
     * Creating objects Table,Index for Visits.
     */
    default void createSchema(CqlSession cqlSession) {
        
        /** 
         * CREATE TABLE IF NOT EXISTS petclinic_visit_by_pet (
         *  pet_id      uuid,
         *  visit_id    uuid,
         *  visit_date date,
         *  description text,
         *  PRIMARY KEY ((pet_id), visit_id));
         **/
        cqlSession.execute(
                createTable(VISIT_TABLE).ifNotExists()
                .withPartitionKey(VISIT_ATT_PET_ID, DataTypes.UUID)
                .withClusteringColumn(VISIT_ATT_VISIT_ID, DataTypes.UUID)
                .withColumn(VISIT_ATT_DESCRIPTION, DataTypes.TEXT)
                .withColumn(VISIT_ATT_VISIT_DATE, DataTypes.DATE)
                .build());
        
        /** Create a secondary index on visitId as cardinality is low. */
        cqlSession.execute(createIndex(VISIT_IDX_VISITID).ifNotExists()
                .onTable(VISIT_TABLE)
                .andColumn(VISIT_ATT_VISIT_ID)
                .build());
    }
}
