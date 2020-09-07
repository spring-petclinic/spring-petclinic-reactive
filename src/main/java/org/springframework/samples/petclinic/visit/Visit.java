package org.springframework.samples.petclinic.visit;

import java.util.UUID;

/**
 * Bean expected by the UI with heriarchy owner 1..n -> Pet 1..n -> Visit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class Visit extends WebBeanVisitCreation  {
    
    /** Serial. */
    private static final long serialVersionUID = -7178251172136975063L;
    
    protected UUID id;
    
    public Visit() {
    }
    
    public Visit(UUID id) {
        this.id = id;
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
