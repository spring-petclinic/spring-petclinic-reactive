package org.springframework.samples.petclinic.owner;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple JavaBean domain object representing an owner.s
 */
@Data 
@Entity
@AllArgsConstructor
@NoArgsConstructor
@CqlName(CassandraPetClinicSchema.OWNER_TABLE)
public class Owner implements CassandraPetClinicSchema {

    @PartitionKey
    @CqlName(OWNER_ATT_ID)
    private UUID id;
    
    @CqlName(OWNER_ATT_FIRSTNAME)
    private String firstName;

    @CqlName(OWNER_ATT_LASTNAME)
    private String lastName;

    @CqlName(OWNER_ATT_ADRESS)
    private String address;

    @CqlName(OWNER_ATT_CITY)
    private String city;
    
    @CqlName(OWNER_ATT_TELEPHONE)
    private String telephone;
    
    public Owner(String uid) {
        this.id = UUID.fromString(uid);
    }

}
