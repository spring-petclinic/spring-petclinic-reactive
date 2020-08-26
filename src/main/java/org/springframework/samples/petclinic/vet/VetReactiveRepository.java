package org.springframework.samples.petclinic.vet;

import java.util.UUID;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

/**
 * Implementing reactive cruds for Vets.
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
public interface VetReactiveRepository extends ReactiveCassandraRepository<Vet, UUID> {
    
}
