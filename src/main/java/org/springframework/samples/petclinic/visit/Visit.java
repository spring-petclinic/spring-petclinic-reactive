package org.springframework.samples.petclinic.visit;

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
@CqlName(CassandraPetClinicSchema.VISIT_TABLE)
public class Visit implements CassandraPetClinicSchema {
    
    @PartitionKey
    @CqlName(VISIT_ATT_PET_ID)
    private UUID petId;
    
    @ClusteringColumn
    @CqlName(VISIT_ATT_VISIT_ID)
    private UUID visitId;
    
    @CqlName(VISIT_ATT_DESCRIPTION)
    private String description;
    
    @CqlName(VISIT_ATT_VISIT_DATE)
    private Date visitDate;

}
