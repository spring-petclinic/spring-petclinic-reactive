package org.springframework.samples.petclinic.pet;

import java.util.UUID;

/**
 * Bean expected by the UI with heriarchy owner 1..n -> Pet 1..n -> Visit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class WebBeanPet extends WebBeanPetCreation {
    
    /** Serial. */
    private static final long serialVersionUID = 6533528868325708859L;
    
    protected UUID id;
    
    public WebBeanPet() {
        super();
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
    
}
