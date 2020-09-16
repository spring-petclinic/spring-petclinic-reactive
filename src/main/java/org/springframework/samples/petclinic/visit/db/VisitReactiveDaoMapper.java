package org.springframework.samples.petclinic.visit.db;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

// TODO: add interface comment
@Mapper
public interface VisitReactiveDaoMapper extends CassandraPetClinicSchema {
    
    @DaoFactory
    VisitReactiveDao visitDao(@DaoKeyspace CqlIdentifier keyspace);
}
