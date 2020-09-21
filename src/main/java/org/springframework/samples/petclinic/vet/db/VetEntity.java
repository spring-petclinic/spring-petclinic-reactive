package org.springframework.samples.petclinic.vet.db;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Veterinarians (Vet) in the Data Accesslayer (Cassandra)
 *
 * @author Cedrick LUNVEN (@clunven)
*/
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@CqlName(VetEntity.VET_TABLE)
public class VetEntity implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 7407715795842376538L;
    
    /** Groups Constant. */
    public static final String VET_TABLE            = "petclinic_vet";
    public static final String VET_ATT_ID           = "id";
    public static final String VET_ATT_LASTNAME     = "last_name";
    public static final String VET_ATT_FIRSTNAME    = "first_name";
    public static final String VET_ATT_SPECIALTIES  = "specialties";
    public static final String VET_IDX_NAME         = "petclinic_idx_vetname";

    @PartitionKey
    @CqlName(VET_ATT_ID)
    private UUID id;

    @CqlName(VET_ATT_FIRSTNAME)
    private String firstName;

    @CqlName(VET_ATT_LASTNAME)
    private String lastName;
    
    @CqlName(VET_ATT_SPECIALTIES)
    private Set<String> specialties = new HashSet<>();  
        
    /**
     * Constructor with initialized uid
     */
    public VetEntity(String uid) {
        this.id = UUID.fromString(uid);
    }
    
}
