package org.springframework.samples.petclinic.visit.db;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple JavaBean domain object representing a visit to a clinic
 */
@Data
@NoArgsConstructor
@Entity
@CqlName(VisitEntity.VISIT_TABLE)
public class VisitEntity implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = -4289614095154632364L;
    
    /** Group constants. */
    public static final String VISIT_TABLE           = "petclinic_visit_by_pet";
    public static final String VISIT_ATT_PET_ID      = "pet_id";
    public static final String VISIT_ATT_VISIT_ID    = "visit_id";
    public static final String VISIT_ATT_VISIT_DATE  = "visit_date";
    public static final String VISIT_ATT_DESCRIPTION = "description";
    public static final String VISIT_IDX_VISITID     = "petclinic_visit_by_pet_idx_visitid";
    
    @PartitionKey
    @CqlName(VISIT_ATT_PET_ID)
    private UUID petId;
    
    @ClusteringColumn
    @CqlName(VISIT_ATT_VISIT_ID)
    private UUID visitId;
    
    @CqlName(VISIT_ATT_DESCRIPTION)
    private String description;
    
    /**
     * A CQL Date is mapped as java LocalDate
     * 
     * - Date      <-> java.time.LocalDate
     * - Timestamp <-> java.time.LocalDate
     * - Time      <-> java.time.LocalTime
     * 
     * @see https://docs.datastax.com/en/developer/java-driver/4.8/manual/core/#cql-to-java-type-mapping
     */
    @CqlName(VISIT_ATT_VISIT_DATE)
    private LocalDate visitDate;
    
    /**
     * Constructor with petId and visit Id.
     */
    public VisitEntity(UUID petId, UUID visitId) {
        this.petId = petId;
        this.visitId = visitId;
    }
    
    /**
     * Constructor with petId and visit Id.
     */
    public VisitEntity(String petId, String visitId) {
        this(UUID.fromString(petId), UUID.fromString(visitId));
    }

}
