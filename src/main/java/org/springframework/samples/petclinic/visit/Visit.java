package org.springframework.samples.petclinic.visit;

import java.util.Date;
import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@Entity
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
    
    public Visit() {
    }
    
    public Visit(UUID petId, UUID visitId) {
        this.petId = petId;
        this.visitId = visitId;
    }
    
    public Visit(String petId, String visitId) {
        this(UUID.fromString(petId), UUID.fromString(visitId));
    }
    
    public Visit(UUID petId, UUID visitId, String description, Date visitDate) {
        super();
        this.petId = petId;
        this.visitId = visitId;
        this.description = description;
        this.visitDate = visitDate;
    }

    /**
     * Getter accessor for attribute 'petId'.
     *
     * @return
     *       current value of 'petId'
     */
    public UUID getPetId() {
        return petId;
    }

    /**
     * Setter accessor for attribute 'petId'.
     * @param petId
     * 		new value for 'petId '
     */
    public void setPetId(UUID petId) {
        this.petId = petId;
    }

    /**
     * Getter accessor for attribute 'visitId'.
     *
     * @return
     *       current value of 'visitId'
     */
    public UUID getVisitId() {
        return visitId;
    }

    /**
     * Setter accessor for attribute 'visitId'.
     * @param visitId
     * 		new value for 'visitId '
     */
    public void setVisitId(UUID visitId) {
        this.visitId = visitId;
    }

    /**
     * Getter accessor for attribute 'description'.
     *
     * @return
     *       current value of 'description'
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter accessor for attribute 'description'.
     * @param description
     * 		new value for 'description '
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter accessor for attribute 'visitDate'.
     *
     * @return
     *       current value of 'visitDate'
     */
    public Date getVisitDate() {
        return visitDate;
    }

    /**
     * Setter accessor for attribute 'visitDate'.
     * @param visitDate
     * 		new value for 'visitDate '
     */
    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }
    

}
