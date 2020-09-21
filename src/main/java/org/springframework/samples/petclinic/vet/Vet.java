package org.springframework.samples.petclinic.vet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Veterinarians (Vet) in the presentation layer (REST)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vet implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 5116674190977860592L;
    
    /** unique identifier in DB. */
    private UUID id;
   
    /** vet first name. */
    private String firstName;
    
    /** vet last name. */
    private String lastName;
    
    /** Specialties for a vet. */
    private Set<VetSpecialty> specialties = new HashSet<>();
     
}
