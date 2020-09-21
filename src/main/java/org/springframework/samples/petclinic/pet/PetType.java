package org.springframework.samples.petclinic.pet;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent a PetType in the presentation layer (REST) 
 *
 * As name is used as primary no need for 2 fields but this is the structure
 * expected from the Angular used to work with this demo.  
 */
@Data
@NoArgsConstructor
public class PetType implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -6848331396030706076L;

    /** Pet identifier (which is also the name). */
    private String id;
    
    /** Pet name (which is also the identifer). */
    private String name;
    
    /** Constuctor with the name. */
    public PetType(String name) {
        this.id   = name;
        this.name = name;
    }
}
