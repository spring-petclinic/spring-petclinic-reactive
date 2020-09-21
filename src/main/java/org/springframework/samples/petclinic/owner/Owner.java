package org.springframework.samples.petclinic.owner;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.samples.petclinic.pet.Pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an Owner (Owner) in the presentation layer (REST)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Owner implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = -140951859331681727L;
    
    private UUID id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
    private Set<Pet> pets = new HashSet<>();
    
    public Owner(UUID uid) {
        this.id = uid;
    }
    
    public Owner(String uid) {
        this.id = UUID.fromString(uid);
    }
    
}
