package org.springframework.samples.petclinic.owner;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

/**
 * Definition of operation for mapping.
 */
@Mapper
public interface OwnerReactiveDaoMapper {
  
    @DaoFactory
    OwnerReactiveDao ownerDao();
    
    static MapperBuilder<OwnerReactiveDaoMapper> builder(CqlSession session) {
        return new OwnerDaoMapperBuilder(session);
    }
}

