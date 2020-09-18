package org.springframework.samples.petclinic.owner.db;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.Data;

/**
 * Simple JavaBean domain object representing an owner.
 */
@Entity
@Data
@CqlName(CassandraPetClinicSchema.OWNER_TABLE)
public class OwnerEntity implements CassandraPetClinicSchema {

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
    
    public OwnerEntity() {}
    
    public OwnerEntity(UUID uid) {
        this.id = uid;
    }
    
    public OwnerEntity(String uid) {
        this(UUID.fromString(uid));
    }

}
