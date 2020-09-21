package org.springframework.samples.petclinic.pet;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.visit.Visit;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Pet in the presentation layer (REST)
 */
@Data
@NoArgsConstructor
public class Pet implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 6533528868325708859L;
    
    /** unique identifier. */
    protected UUID id;
    
    /** Pet name. */
    protected String name;
    
    /** Pet birthDate formatted as yyyy/MM/dd" */
    protected String birthDate;
    
    /** Pet Type. */
    protected PetType type;
    
    /** Reference to Owner. */
    protected Owner owner;
    
    /** Set of visit for this pet. */
    protected Set<Visit> visits;
    
    /**
     * Constructor with only petId (syntaxic sugar).
     */
    public Pet(UUID petId) {
        this.id = petId;
    }

}
