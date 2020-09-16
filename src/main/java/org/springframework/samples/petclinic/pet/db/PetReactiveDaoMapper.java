package org.springframework.samples.petclinic.pet.db;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface PetReactiveDaoMapper {
    
    @DaoFactory
    PetReactiveDao petDao(@DaoKeyspace CqlIdentifier keyspace);
    
}
