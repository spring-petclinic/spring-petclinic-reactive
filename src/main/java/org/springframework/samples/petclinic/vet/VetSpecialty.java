package org.springframework.samples.petclinic.vet;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represent a vet specialty at presentation layer (REST)
 */
@Data
@NoArgsConstructor
public class VetSpecialty implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -6848331396030706076L;

    /** identifier for the specialty. */
    private String id;
    
    /** name for the specialty (same as id in Cassandra DB>) */
    private String name;
    
    /** Constructor with single Parameter. */
    public VetSpecialty(String name) {
        this.id   = name;
        this.name = name;
    }

}
