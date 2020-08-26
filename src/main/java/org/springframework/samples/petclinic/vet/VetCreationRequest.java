package org.springframework.samples.petclinic.vet;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VetCreationRequest {

    private String firstName;

    private String lastName;

    private Set<String> specialties = new HashSet<>();  
}
