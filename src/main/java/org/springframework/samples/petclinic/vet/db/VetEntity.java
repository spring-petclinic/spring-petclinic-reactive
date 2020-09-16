package org.springframework.samples.petclinic.vet.db;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

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
@Entity
@CqlName(CassandraPetClinicSchema.VET_TABLE)
public class VetEntity implements Serializable, CassandraPetClinicSchema {

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

    public VetEntity() {}
        
    public VetEntity(String uid) {
        this.id = UUID.fromString(uid);
    }
    
    public VetEntity(UUID id, String firstName, String lastName, Set<String> specialties) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialties = specialties;
    }



    /**
     * Getter accessor for attribute 'id'.
     *
     * @return
     *       current value of 'id'
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter accessor for attribute 'id'.
     * @param id
     * 		new value for 'id '
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter accessor for attribute 'firstName'.
     *
     * @return
     *       current value of 'firstName'
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter accessor for attribute 'firstName'.
     * @param firstName
     * 		new value for 'firstName '
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter accessor for attribute 'lastName'.
     *
     * @return
     *       current value of 'lastName'
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter accessor for attribute 'lastName'.
     * @param lastName
     * 		new value for 'lastName '
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter accessor for attribute 'specialties'.
     *
     * @return
     *       current value of 'specialties'
     */
    public Set<String> getSpecialties() {
        return specialties;
    }

    /**
     * Setter accessor for attribute 'specialties'.
     * @param specialties
     * 		new value for 'specialties '
     */
    public void setSpecialties(Set<String> specialties) {
        this.specialties = specialties;
    }
    
    
}
