package org.springframework.samples.petclinic.conf;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createKeyspace;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;


/**
 * Contains DDL statements to build all Cassandra tables used by the application
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public interface CassandraPetClinicSchema {
    
    String VET_TABLE            = "petclinic_vet";
    String VET_ATT_ID           = "id";
    String VET_ATT_LASTNAME     = "last_name";
    String VET_ATT_FIRSTNAME    = "first_name";
    String VET_ATT_SPECIALTIES  = "specialties";
    String VET_IDX_NAME         = "petclinic_idx_vetname";
    
    /**
     * CREATE TABLE IF NOT EXISTS petclinic_vet (
     *  id          uuid,
     *  first_name  text,
     *  last_name   text,
     *  specialties set<text>,
     *  PRIMARY KEY ((id))
     * );
     */
    SimpleStatement CREATE_TABLE_VET = 
            createTable(VET_TABLE).ifNotExists()
            .withPartitionKey(VET_ATT_ID, DataTypes.UUID)
            .withColumn(VET_ATT_FIRSTNAME, DataTypes.TEXT)
            .withColumn(VET_ATT_LASTNAME, DataTypes.TEXT)
            .withColumn(VET_ATT_SPECIALTIES, DataTypes.setOf(DataTypes.TEXT))
            .build();
    
    SimpleStatement CREATE_INDEX_VETNAME = 
            createIndex(VET_IDX_NAME).ifNotExists()
            .onTable(VET_TABLE)
            .andColumn(VET_ATT_LASTNAME)
            .build();
    
    String OWNER_TABLE          = "petclinic_owner";
    String OWNER_ATT_ID         = "id";
    String OWNER_ATT_LASTNAME   = "last_name";
    String OWNER_ATT_FIRSTNAME  = "first_name";
    String OWNER_ATT_ADRESS     = "address";
    String OWNER_ATT_CITY       = "city";
    String OWNER_ATT_TELEPHONE  = "telephone";
    String OWNER_IDX_NAME       = "petclinic_idx_ownername";
    
    /**
     * CREATE TABLE IF NOT EXISTS petclinic_owner (
     *      id         uuid,
     *      first_name text,
     *      last_name  text,
     *      address    text,
     *      city       text,
     *      telephone  text,
     *      PRIMARY KEY ((id))
     *);
     */
    SimpleStatement CREATE_TABLE_OWNER = 
            createTable(OWNER_TABLE).ifNotExists()
            .withPartitionKey(OWNER_ATT_ID, DataTypes.UUID)
            .withColumn(OWNER_ATT_FIRSTNAME, DataTypes.TEXT)
            .withColumn(OWNER_ATT_LASTNAME, DataTypes.TEXT)
            .withColumn(OWNER_ATT_ADRESS, DataTypes.TEXT)
            .withColumn(OWNER_ATT_CITY, DataTypes.TEXT)
            .withColumn(OWNER_ATT_TELEPHONE, DataTypes.TEXT)
            .build();
    
    SimpleStatement CREATE_INDEX_OWNERNAME = 
            createIndex(OWNER_IDX_NAME).ifNotExists()
            .onTable(OWNER_TABLE)
            .andColumn(OWNER_ATT_LASTNAME)
            .build();
    
    SimpleStatement STMT_OWNER_SEARCHBYNAME =
            selectFrom(OWNER_TABLE).all()
            .whereColumn(OWNER_ATT_LASTNAME)
            .isEqualTo(QueryBuilder.bindMarker())
            .build();
    
    String VET_SPECIALTY_TABLE          = "petclinic_vet_by_specialty";
    String VET_SPECIALTY_ATT_SPECIALTY  = "specialty";
    String VET_SPECIALTY_ATT_VETID      = "vet_id";
    String VET_SPECIALTY_ATT_LASTNAME   = "last_name";
    String VET_SPECIALTY_ATT_FIRSTNAME  = "first_name";
    
    /**
     * CREATE TABLE IF NOT EXISTS petclinic_vet_by_specialty (
     *      specialty   text,
     *      vet_id          uuid,
     *      first_name  text,
     *      last_name   text,
     *      PRIMARY KEY ((specialty), vet_id)
     *);
     */
    SimpleStatement CREATE_TABLE_VET_SPECIALTY =
            createTable(VET_SPECIALTY_TABLE).ifNotExists()
            .withPartitionKey(VET_SPECIALTY_ATT_SPECIALTY, DataTypes.TEXT)
            .withClusteringColumn(VET_SPECIALTY_ATT_VETID, DataTypes.UUID)
            .withColumn(VET_SPECIALTY_ATT_LASTNAME, DataTypes.TEXT)
            .withColumn(VET_SPECIALTY_ATT_FIRSTNAME, DataTypes.TEXT)
            .build();
    
    String REFLIST_TABLE         = "petclinic_reference_lists";
    String REFLIST_ATT_LISTNAME  = "list_name";
    String REFLIST_ATT_VALUES    = "values";
    
    /** 
     * Here we want all values on a single node, avoiding full scan. 
     * We pick am unordered set to avoid duplication, list to be sorted at ui side.
     * 
     * CREATE TABLE IF NOT EXISTS petclinic_reference_lists (
     *  list_name text,
     *  values set<text>,
     *  PRIMARY KEY ((list_name))
     * );
     */
    SimpleStatement CREATE_TABLE_REFLIST = 
            createTable(REFLIST_TABLE).ifNotExists()
            .withPartitionKey(REFLIST_ATT_LISTNAME, DataTypes.TEXT)
            .withColumn(REFLIST_ATT_VALUES, DataTypes.setOf(DataTypes.TEXT))
            .build();
    
    SimpleStatement STMT_REFLIST_READ =
            selectFrom(REFLIST_TABLE).column(REFLIST_ATT_VALUES)
            .whereColumn(REFLIST_ATT_LISTNAME)
            .isEqualTo(QueryBuilder.bindMarker())
            .build();
    
    SimpleStatement STMT_REFLIST_INSERT =
            insertInto(REFLIST_TABLE)
            .value(REFLIST_ATT_LISTNAME, QueryBuilder.bindMarker())
            .value(REFLIST_ATT_VALUES, QueryBuilder.bindMarker())
            .build();
    
    String PET_TABLE          = "petclinic_pet_by_owner";
    String PET_ATT_OWNER_ID   = "owner_id";
    String PET_ATT_PET_ID     = "pet_id";
    String PET_ATT_PET_TYPE   = "pet_type";
    String PET_ATT_NAME       = "name";
    String PET_ATT_BIRTHDATE  = "birth_date";
    
    /**
     * CREATE TABLE IF NOT EXISTS petclinic_pet_by_owner (
     *  owner_id   uuid,
     *  pet_id     uuid,
     *  pet_type   text,
     *  name       text,
     *  birth_date date,
     *  PRIMARY KEY ((owner_id), pet_id)
     * );
     */
    SimpleStatement CREATE_TABLE_PET = 
            createTable(PET_TABLE)
            .ifNotExists()
            .withPartitionKey(PET_ATT_OWNER_ID, DataTypes.UUID)
            .withClusteringColumn(PET_ATT_PET_ID, DataTypes.UUID)
            .withColumn(PET_ATT_PET_TYPE, DataTypes.TEXT)
            .withColumn(PET_ATT_NAME, DataTypes.TEXT)
            .withColumn(PET_ATT_BIRTHDATE, DataTypes.DATE)
            .build();
    
    String VISIT_TABLE           = "petclinic_visit_by_pet";
    String VISIT_ATT_PET_ID      = "pet_id";
    String VISIT_ATT_VISIT_ID    = "visit_id";
    String VISIT_ATT_VISIT_DATE  = "visit_date";
    String VISIT_ATT_DESCRIPTION = "description";
    
    /**
     * CREATE TABLE IF NOT EXISTS petclinic_visit_by_pet (
     *  pet_id      uuid,
     *  visit_id    uuid,
     *  visit_date date,
     *  description text,
     *  PRIMARY KEY ((pet_id), visit_id)
     * );
     */
    SimpleStatement CREATE_TABLE_VISIT = 
            createTable(VISIT_TABLE)
            .ifNotExists()
            .withPartitionKey(VISIT_ATT_PET_ID, DataTypes.UUID)
            .withClusteringColumn(VISIT_ATT_VISIT_ID, DataTypes.UUID)
            .withColumn(VISIT_ATT_DESCRIPTION, DataTypes.TEXT)
            .withColumn(VISIT_ATT_VISIT_DATE, DataTypes.DATE)
            .build();
    
    default void createSchema(CqlSession cqlSession) {
        cqlSession.execute(CREATE_TABLE_VET);
        cqlSession.execute(CREATE_INDEX_VETNAME);
        cqlSession.execute(CREATE_TABLE_VET_SPECIALTY);
        cqlSession.execute(CREATE_TABLE_OWNER);
        cqlSession.execute(CREATE_INDEX_OWNERNAME);
        cqlSession.execute(CREATE_TABLE_REFLIST);
        cqlSession.execute(CREATE_TABLE_PET);
        cqlSession.execute(CREATE_TABLE_VISIT);
    }
    
    default void createKeyspaceSimpleStrategy(CqlSession cqlSession, String keyspaceName, int replicationFactor) {
        cqlSession.execute(createKeyspace(keyspaceName)
                   .ifNotExists()
                   .withSimpleStrategy(replicationFactor)
                   .withDurableWrites(true)
                   .build());
   }
    
}
