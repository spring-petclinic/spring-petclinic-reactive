package org.springframework.samples.petclinic.vet;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperBuilder;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface VetReactiveDaoMapper {

    @DaoFactory
    VetReactiveDao vetDao();
    
    static MapperBuilder<VetReactiveDaoMapper> builder(CqlSession session) {
        return new VetReactiveDaoMapperBuilder(session);
    }
}

