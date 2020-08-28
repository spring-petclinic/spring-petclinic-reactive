package org.springframework.samples.petclinic.vet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 * @author Maciej Walkowiak
 * @author Cedrick Lunven
 */
@Data 
@Entity
@AllArgsConstructor
@NoArgsConstructor
@CqlName(CassandraPetClinicSchema.VET_TABLE)
public class Vet implements Serializable, CassandraPetClinicSchema {

    /** Serial Number. */
    private static final long serialVersionUID = 7407715795842376538L;

    @PartitionKey
    @CqlName(VET_ATT_ID)
    private UUID id;

    @CqlName(VET_ATT_FIRSTNAME)
    private String firstName;

    @CqlName(VET_ATT_LASTNAME)
    private String lastName;
    
    @CqlName(VET_ATT_SPECIALTIES)
    private Set<String> specialties = new HashSet<>();  

    public Vet(String uid) {
        this.id = UUID.fromString(uid);
    }
}
