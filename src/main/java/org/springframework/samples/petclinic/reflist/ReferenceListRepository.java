package org.springframework.samples.petclinic.reflist;

import java.util.Set;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import reactor.core.publisher.Mono;

/**
 * Work with table 
 * @author Cedrick LUNVEN (@clunven)
 */
@Component
public class ReferenceListRepository implements CassandraPetClinicSchema {
    
    /** Constants in the DB. */
    public static final String PET_TYPE      = "pet_type";
    public static final String VET_SPECIALTY = "vet_specialty";
    
    private CqlSession          cqlSession   = null;
    private PreparedStatement   psReadList   = null;  
    private PreparedStatement   psUpsertList = null;  
    
    /**
     * Table create could be synchronous here.
     *
     * @param cqlT
     *      cql template
     * @param reactCqlT
     *       cql template reactive
     */
    public ReferenceListRepository(CqlSession cqlS) {
        this.cqlSession = cqlS;
        psReadList   = cqlSession.prepare(STMT_REFLIST_READ);
        psUpsertList = cqlSession.prepare(STMT_REFLIST_INSERT);
    }
        
    public Mono<Boolean> saveList(String list, Set<String> values) {
        return Mono.from(cqlSession.executeReactive(psUpsertList.bind(list, values)))
                   .map(rr -> rr.wasApplied());
    }
    
    public Mono<Set<String>> listPetType() {
        return findReferenceList(PET_TYPE);
    }
    
    public Mono<Set<String>>listVeretinianSpecialties() {
        return findReferenceList(VET_SPECIALTY);
    }
    
    protected Mono<Set<String>> findReferenceList(String listName) {
        return Mono.from(cqlSession.executeReactive(psReadList.bind(listName)))
                   .map(rr -> rr.getSet(REFLIST_ATT_VALUES, String.class));
    }

}
