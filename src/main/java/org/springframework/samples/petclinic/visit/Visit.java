package org.springframework.samples.petclinic.visit;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.samples.petclinic.pet.Pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Visit (visit) in the presentation layer (REST)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -7178251172136975063L;

    /** Unique identifier for visit. */
    protected UUID id;
    
    /** Visit date. */
    protected String date;
    
    /** Visit Description. */
    protected String description;
    
    /** Related pet for the visit. */
    protected Pet pet;
    
    /**
     * Create a visit from its id.
     */
    public Visit(UUID id) {
        this.id = id;
    }
}
