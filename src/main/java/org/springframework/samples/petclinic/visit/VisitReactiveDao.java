package org.springframework.samples.petclinic.visit;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Mono;

@Dao
public interface VisitReactiveDao extends CassandraPetClinicSchema {
    
    @Select(customWhereClause = VISIT_ATT_PET_ID + "= :petId")
    MappedReactiveResultSet<Visit> findByPetIdReactive(UUID petId);

    @Update
    ReactiveResultSet updateReactive(Visit visit);
    
    default Mono<Visit> save(Visit visit) {
        return Mono.from(updateReactive(visit)).map(rr -> visit);
    }
    
}
