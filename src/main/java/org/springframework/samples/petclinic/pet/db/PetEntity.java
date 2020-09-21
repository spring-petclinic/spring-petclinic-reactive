package org.springframework.samples.petclinic.pet.db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Pet (Pet) in the data access layer (Cassandra)
 */
@Entity
@Data
@NoArgsConstructor
@CqlName(PetEntity.PET_TABLE)
public class PetEntity implements Serializable {
    
    /** Serial id. */
    private static final long serialVersionUID = -5340639715920598448L;
    
    /** Group constants in a single Class. */
    public static final String PET_TABLE          = "petclinic_pet_by_owner";
    public static final String PET_INDEX          = "petclinic_idx_petid";
    public static final String PET_ATT_OWNER_ID   = "owner_id";
    public static final String PET_ATT_PET_ID     = "pet_id";
    public static final String PET_ATT_PET_TYPE   = "pet_type";
    public static final String PET_ATT_NAME       = "name";
    public static final String PET_ATT_BIRTHDATE  = "birth_date";
    
    @PartitionKey
    @CqlName(PET_ATT_OWNER_ID)
    private UUID ownerId;
    
    @ClusteringColumn
    @CqlName(PET_ATT_PET_ID)
    private UUID petId;
    
    @CqlName(PET_ATT_PET_TYPE)
    private String petType;
    
    @CqlName(PET_ATT_NAME)
    private String name;
    
    /**
     *  A CQL Date is mapped as java LocalDate
     * - Date      <-> java.time.LocalDate
     * - Timestamp <-> java.time.LocalDate
     * - Time      <-> java.time.LocalTime
     * 
     * @see https://docs.datastax.com/en/developer/java-driver/4.8/manual/core/#cql-to-java-type-mapping
     */
    @CqlName(PET_ATT_BIRTHDATE)
    private LocalDate birthDate;
    
    /**
     * Constructor from the id.
     */
    public PetEntity(UUID petId) {
        this.petId = petId;
    }
}
