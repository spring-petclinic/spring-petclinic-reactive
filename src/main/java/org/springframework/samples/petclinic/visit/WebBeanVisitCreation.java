package org.springframework.samples.petclinic.visit;

import java.io.Serializable;

import org.springframework.samples.petclinic.pet.WebBeanPet;

public class WebBeanVisitCreation implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -787431802048881983L;
    protected String date;
    protected String description;
    protected WebBeanPet pet;
    
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
     *      new value for 'description '
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
     *      new value for 'pet '
     */
    public void setPet(WebBeanPet pet) {
        this.pet = pet;
    }
    
    /**
     * Getter accessor for attribute 'date'.
     *
     * @return
     *       current value of 'date'
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Setter accessor for attribute 'date'.
     * @param date
     *      new value for 'date '
     */
    public void setDate(String date) {
        this.date = date;
    }
}
