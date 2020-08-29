package org.springframework.samples.petclinic.owner;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface OwnerReactiveDaoMapper {

    @DaoFactory
    OwnerReactiveDao ownerDao(@DaoKeyspace CqlIdentifier keyspace); 
    
}
