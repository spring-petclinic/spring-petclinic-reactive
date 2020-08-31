package org.springframework.samples.petclinic.pet;

import java.util.UUID;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Help for advanced queries
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class PetReactiveDaoQueryProvider implements CassandraPetClinicSchema {

    private final CqlSession session;
    
    private final EntityHelper<Pet> petHelper;
    
    private PreparedStatement psFindPetByPetId;
    private PreparedStatement psFindPetByOwnerId;
    
    public PetReactiveDaoQueryProvider(MapperContext ctx, EntityHelper<Pet> helper) {
      this.session       = ctx.getSession();
      this.petHelper   = helper;
      this.psFindPetByPetId = session.prepare(STMT_PET_FINDBY_PET_ID);
      this.psFindPetByOwnerId = session.prepare(STMT_PET_FINDBY_OWNER_ID);
    }
    
    public Mono<Pet> findByPetIdReactive(UUID petId) {
        return Mono.from(session.executeReactive(psFindPetByPetId.bind(petId)))
                   .map(petHelper::get);
    }

    public Flux<Pet> findAllByOwnerIdReactive(UUID ownerId) {
        return  Flux.from(session.executeReactive(psFindPetByOwnerId.bind(ownerId)))
                    .map(petHelper::get);
    }
    
}
