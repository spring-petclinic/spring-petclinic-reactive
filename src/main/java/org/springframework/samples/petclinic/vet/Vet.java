package org.springframework.samples.petclinic.vet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 * @author Maciej Walkowiak
 */
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Vet implements Serializable {

    /**
     * Serial Number.
     */
    private static final long serialVersionUID = 7407715795842376538L;

    @Id
    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private Set<String> specialties = new HashSet<>();

    public int getNrOfSpecialties() {
        return specialties.size();
    }

    public void addSpecialty(String specialty) {
        specialties.add(specialty);
    }

}
