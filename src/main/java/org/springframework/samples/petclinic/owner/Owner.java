package org.springframework.samples.petclinic.owner;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.samples.petclinic.pet.WebBeanPet;

/**
 * Bean expected by the UI with heriarchy owner 1..n -> Pet 1..n -> Visit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class Owner implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -140951859331681727L;
    
    private UUID id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
    private Set<WebBeanPet> pets = new HashSet<>();
    
    public Owner() {}
    
    public Owner(UUID ownerId) {
        this.id = ownerId;
    }
    
    public Owner(UUID id, String firstName, String lastName, String address, String city, String telephone) {
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
     * Getter accessor for attribute 'pets'.
     *
     * @return
     *       current value of 'pets'
     */
    public Set<WebBeanPet> getPets() {
        return pets;
    }
    /**
     * Setter accessor for attribute 'pets'.
     * @param pets
     * 		new value for 'pets '
     */
    public void setPets(Set<WebBeanPet> pets) {
        this.pets = pets;
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
     *      new value for 'firstName '
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
     *      new value for 'lastName '
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
     *      new value for 'address '
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
     *      new value for 'city '
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
     *      new value for 'telephone '
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Owner other = (Owner) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
