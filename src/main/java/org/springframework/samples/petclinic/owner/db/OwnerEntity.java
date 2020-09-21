package org.springframework.samples.petclinic.owner.db;

import java.io.Serializable;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Owner (Owner) in the data access layer (Cassandra)
 */
@Data
@Entity
@NoArgsConstructor
@CqlName(OwnerEntity.OWNER_TABLE)
public class OwnerEntity implements Serializable {

    /** Serial.*/
    private static final long serialVersionUID = 313944474970753001L;
    
    /** Group schema related constants in a single place. */
    public static final String OWNER_TABLE          = "petclinic_owner";
    public static final String OWNER_ATT_ID         = "id";
    public static final String OWNER_ATT_LASTNAME   = "last_name";
    public static final String OWNER_ATT_FIRSTNAME  = "first_name";
    public static final String OWNER_ATT_ADDRESS    = "address";
    public static final String OWNER_ATT_CITY       = "city";
    public static final String OWNER_ATT_TELEPHONE  = "telephone";
    public static final String OWNER_IDX_NAME       = "petclinic_idx_ownername";
    
    @PartitionKey
    @CqlName(OWNER_ATT_ID)
    private UUID id;

    @CqlName(OWNER_ATT_FIRSTNAME)
    private String firstName;

    @CqlName(OWNER_ATT_LASTNAME)
    private String lastName;

    @CqlName(OWNER_ATT_ADDRESS)
    private String address;

    @CqlName(OWNER_ATT_CITY)
    private String city;
    
    @CqlName(OWNER_ATT_TELEPHONE)
    private String telephone;
    
    /** Constructor with initialized identifier. */
    public OwnerEntity(UUID uid) {
        this.id = uid;
    }
    
    /** Constructor with initialized identifier. */
    public OwnerEntity(String uid) {
        this(UUID.fromString(uid));
    }

}
