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
public class WebBeanOwner extends WebBeanOwnerCreation implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -140951859331681727L;
    
    private UUID id;
    
    private Set<WebBeanPet> pets = new HashSet<>();
    
    public WebBeanOwner() {}
    
    public WebBeanOwner(UUID ownerId) {
        this.id = ownerId;
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
    
}
