package org.springframework.samples.petclinic.owner.db;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

/**
 * Simple JavaBean domain object representing an owner.s
 */
@Entity
@CqlName(CassandraPetClinicSchema.OWNER_TABLE)
public class OwnerEntity implements CassandraPetClinicSchema {

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
    
    public OwnerEntity() {}
    
    public OwnerEntity(UUID uid) {
        this.id = uid;
    }
    public OwnerEntity(String uid) {
        this(UUID.fromString(uid));
    }
    
    public OwnerEntity(UUID id, String firstName, String lastName, String address, String city, String telephone) {
        super();
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.telephone = telephone;
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
     * Getter accessor for attribute 'address'.
     *
     * @return
     *       current value of 'address'
     */
    public String getAddress() {
        return address;
    }

    /**
     * Setter accessor for attribute 'address'.
     * @param address
     * 		new value for 'address '
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Getter accessor for attribute 'city'.
     *
     * @return
     *       current value of 'city'
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter accessor for attribute 'city'.
     * @param city
     * 		new value for 'city '
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter accessor for attribute 'telephone'.
     *
     * @return
     *       current value of 'telephone'
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Setter accessor for attribute 'telephone'.
     * @param telephone
     * 		new value for 'telephone '
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
