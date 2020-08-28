package org.springframework.samples.petclinic.pet;

import java.util.Date;
import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Entity
@AllArgsConstructor
@NoArgsConstructor
@CqlName(CassandraPetClinicSchema.PET_TABLE)
public class Pet implements CassandraPetClinicSchema {
    
    @PartitionKey
    @CqlName(PET_ATT_OWNER_ID)
    private UUID ownerId;
    
    @ClusteringColumn
    @CqlName(PET_ATT_PET_ID)
    private UUID petId;
    
    @CqlName(PET_ATT_PET_TYPE)
    private String petType;
    
    @CqlName(PET_ATT_NAME)
    private String name;
    
    @CqlName(PET_ATT_BIRTHDATE)
    private Date birthDate;

}
