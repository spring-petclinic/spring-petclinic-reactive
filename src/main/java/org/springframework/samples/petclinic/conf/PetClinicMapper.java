package org.springframework.samples.petclinic.conf;

import org.springframework.samples.petclinic.owner.OwnerReactiveDao;
import org.springframework.samples.petclinic.pet.PetReactiveDao;
import org.springframework.samples.petclinic.vet.VetReactiveDao;
import org.springframework.samples.petclinic.visit.VisitReactiveDao;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface PetClinicMapper {
    
    @DaoFactory
    VetReactiveDao vetDao();
    
    @DaoFactory
    VisitReactiveDao visitDao();
    
    @DaoFactory
    PetReactiveDao petDao();
    
    @DaoFactory
    OwnerReactiveDao ownerDao();
}
