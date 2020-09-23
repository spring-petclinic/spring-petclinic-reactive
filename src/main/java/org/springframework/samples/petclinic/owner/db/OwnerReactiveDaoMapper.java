package org.springframework.samples.petclinic.owner.db;


import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createIndex;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_ADDRESS;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_CITY;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_FIRSTNAME;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_ID;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_LASTNAME;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_ATT_TELEPHONE;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_IDX_NAME;
import static org.springframework.samples.petclinic.owner.db.OwnerEntity.OWNER_TABLE;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * DataStax Java driver mapper for Owner. The mapper generates the 
 * boilerplate to execute queries and convert the results into 
 * application-level objects.
 * 
 * Documentation of the mapper:
 * - {@link https://docs.datastax.com/en/developer/java-driver/4.9/manual/mapper/}
 */
@Mapper
public interface OwnerReactiveDaoMapper {

    @DaoFactory
    OwnerReactiveDao ownerDao(@DaoKeyspace CqlIdentifier keyspace);
     
    /**
     * Create objects required for this business domain (tables, index, udt) if they do not exist.
     */
    default void createSchema(CqlSession cqlSession) {
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
