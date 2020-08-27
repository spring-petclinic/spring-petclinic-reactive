package org.springframework.samples.petclinic.vet;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

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
@Table("petclinic_vet")
public class Vet implements Serializable {

    /** Serial Number. */
    private static final long serialVersionUID = 7407715795842376538L;

    @PrimaryKey
    @NotEmpty
    private UUID id;

    @Column("first_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String firstName;

    @Column("last_name")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String lastName;

    @Column("specialties")
    @CassandraType(type = CassandraType.Name.SET, typeArguments = CassandraType.Name.TEXT)
    private Set<String> specialties = new HashSet<>();  

    public Vet(String uid) {
        this.id = UUID.fromString(uid);
    }
}
