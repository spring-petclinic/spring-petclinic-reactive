package org.springframework.samples.petclinic.visit;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.springframework.samples.petclinic.pet.WebBeanPet;

/**
 * Bean expected by the UI with heriarchy owner 1..n -> Pet 1..n -> Visit.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class WebBeanVisit implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -2248593252322452786L;
    protected UUID id;
    private Date date;
    private String description;
    private WebBeanPet pet;
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
     * Getter accessor for attribute 'date'.
     *
     * @return
     *       current value of 'date'
     */
    public Date getDate() {
        return date;
    }
    /**
     * Setter accessor for attribute 'date'.
     * @param date
     * 		new value for 'date '
     */
    public void setDate(Date date) {
        this.date = date;
    }
    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Getter accessor for attribute 'pet'.
     *
     * @return
     *       current value of 'pet'
     */
    public WebBeanPet getPet() {
        return pet;
    }
    /**
     * Setter accessor for attribute 'pet'.
     * @param pet
     * 		new value for 'pet '
     */
    public void setPet(WebBeanPet pet) {
        this.pet = pet;
    }

}
