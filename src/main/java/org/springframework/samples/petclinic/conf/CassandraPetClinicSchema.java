package org.springframework.samples.petclinic.conf;

/**
 * Group every constants in an Interface to easily use them everywhere by simple 'implements'.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public interface CassandraPetClinicSchema {
    
    String VET_TABLE            = "petclinic_vet";
    String VET_ATT_ID           = "id";
    String VET_ATT_LASTNAME     = "last_name";
    String VET_ATT_FIRSTNAME    = "first_name";
    String VET_ATT_SPECIALTIES  = "specialties";
    String VET_IDX_NAME         = "petclinic_idx_vetname";
    
    String OWNER_TABLE          = "petclinic_owner";
    String OWNER_ATT_ID         = "id";
    String OWNER_ATT_LASTNAME   = "last_name";
    String OWNER_ATT_FIRSTNAME  = "first_name";
    String OWNER_ATT_ADDRESS    = "address";
    String OWNER_ATT_CITY       = "city";
    String OWNER_ATT_TELEPHONE  = "telephone";
    String OWNER_IDX_NAME       = "petclinic_idx_ownername";
    
    String PET_TABLE          = "petclinic_pet_by_owner";
    String PET_ATT_OWNER_ID   = "owner_id";
    String PET_ATT_PET_ID     = "pet_id";
    String PET_ATT_PET_TYPE   = "pet_type";
    String PET_ATT_NAME       = "name";
    String PET_ATT_BIRTHDATE  = "birth_date";
    
    String VET_SPECIALTY_TABLE          = "petclinic_vet_by_specialty";
    String VET_SPECIALTY_ATT_SPECIALTY  = "specialty";
    String VET_SPECIALTY_ATT_VETID      = "vet_id";
    String VET_SPECIALTY_ATT_LASTNAME   = "last_name";
    String VET_SPECIALTY_ATT_FIRSTNAME  = "first_name";
    
    String REFLIST_TABLE         = "petclinic_reference_lists";
    String REFLIST_ATT_LISTNAME  = "list_name";
    String REFLIST_ATT_VALUES    = "values";
    
    String VISIT_TABLE           = "petclinic_visit_by_pet";
    String VISIT_ATT_PET_ID      = "pet_id";
    String VISIT_ATT_VISIT_ID    = "visit_id";
    String VISIT_ATT_VISIT_DATE  = "visit_date";
    String VISIT_ATT_DESCRIPTION = "description";
   
}
