package org.springframework.samples.petclinic.vet;

import java.util.UUID;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.Select;
import com.datastax.oss.driver.api.mapper.annotations.Update;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Dao
public interface VetReactiveDao {
    
    @Select
    MappedReactiveResultSet<Vet> findByIdReactive(UUID vetid);
    
    default Mono<Vet> findById(UUID vetid) {
        return Mono.from(findByIdReactive(vetid));
    }
    
    @Select
    MappedReactiveResultSet<Vet> findAllReactive();
    
    default Flux<Vet> findAll() {
        return Flux.from(findAllReactive());
    }
    
    @Update
    ReactiveResultSet updateReactive(Vet vet);
    
    default Mono<Vet> save(Vet vet) {
        // must be applied as not LWT, no checks
        return Mono.from(updateReactive(vet))
                   .map(rr -> vet);
    }
    
    @Delete
    ReactiveResultSet deleteReactive(Vet vet);
    
    default Mono<Boolean> delete(Vet vet) {
        return Mono.from(deleteReactive(vet))
                   .map(rr -> rr.wasApplied());
    }
    
}
