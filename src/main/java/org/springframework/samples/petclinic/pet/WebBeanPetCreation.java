package org.springframework.samples.petclinic.pet;

import java.io.Serializable;
import java.util.Set;

import org.springframework.samples.petclinic.owner.OwnerWebBean;
import org.springframework.samples.petclinic.visit.VisitWebBean;

/**
 * Creation a pet.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class WebBeanPetCreation implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 6016259171001348572L;
    
    /** Pet name. */
    protected String name;
    
    /** Pet Type. */
    protected WebBeanPetType type;
    
    // When creating a pet the full owner is sent even if only id is used
    protected OwnerWebBean owner;
    
    // Not used for creation but rendering
    protected Set<VisitWebBean> visits;
    
    // Format "yyyy/MM/dd"
    protected String birthDate;
    
    public WebBeanPetCreation() {}

    /**
     * Getter accessor for attribute 'owner'.
     *
     * @return
     *       current value of 'owner'
     */
    public OwnerWebBean getOwner() {
        return owner;
    }

    /**
     * Setter accessor for attribute 'owner'.
     * @param owner
     * 		new value for 'owner '
     */
    public void setOwner(OwnerWebBean owner) {
        this.owner = owner;
    }

    /**
     * Getter accessor for attribute 'visits'.
     *
     * @return
     *       current value of 'visits'
     */
    public Set<VisitWebBean> getVisits() {
        return visits;
    }

    /**
     * Setter accessor for attribute 'visits'.
     * @param visits
     * 		new value for 'visits '
     */
    public void setVisits(Set<VisitWebBean> visits) {
        this.visits = visits;
    }
    
    /**
     * Getter accessor for attribute 'name'.
     *
     * @return
     *       current value of 'name'
     */
    public String getName() {
        return name;
    }

    /**
     * Setter accessor for attribute 'name'.
     * @param name
     * 		new value for 'name '
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter accessor for attribute 'birthDate'.
     *
     * @return
     *       current value of 'birthDate'
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Setter accessor for attribute 'birthDate'.
     * @param birthDate
     * 		new value for 'birthDate '
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Getter accessor for attribute 'type'.
     *
     * @return
     *       current value of 'type'
     */
    public WebBeanPetType getType() {
        return type;
    }

    /**
     * Setter accessor for attribute 'type'.
     * @param type
     * 		new value for 'type '
     */
    public void setType(WebBeanPetType type) {
        this.type = type;
    }

}
