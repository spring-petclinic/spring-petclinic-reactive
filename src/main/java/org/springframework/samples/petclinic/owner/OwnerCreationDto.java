package org.springframework.samples.petclinic.owner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerCreationDto {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String telephone;
}
