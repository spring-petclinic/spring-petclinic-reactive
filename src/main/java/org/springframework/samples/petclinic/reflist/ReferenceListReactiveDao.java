package org.springframework.samples.petclinic.reflist;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import java.util.Set;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import reactor.core.publisher.Mono;

/**
 * Work with table.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@Component
public class ReferenceListReactiveDao implements CassandraPetClinicSchema {
    
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
    public ReferenceListReactiveDao(CqlSession cqlS) {
        this.cqlSession  = cqlS;
        psReadList       = cqlSession.prepare(STMT_REFLIST_READ);
        psUpsertList     = cqlSession.prepare(STMT_REFLIST_INSERT);
    }
        
    public Mono<Boolean> saveList(String list, Set<String> values) {
        return Mono.from(cqlSession.executeReactive(psUpsertList.bind(list, values)))
                   .map(rr -> rr.wasApplied());
    }
    
    // Operation on Pet Types
    
    public Mono<Set<String>> listPetType() {
        return findReferenceList(PET_TYPE);
    }
    
    public Mono<String> addPetType(String value) {
        return addToReferenceList(PET_TYPE, value);
    }
    
    public Mono<Void> removePetType(String value) {
        return removeFromToReferenceList(PET_TYPE, value);
    }
    
    public Mono<String> replacePetType(String oldValue, String newValue) {
        return removePetType(oldValue).then(addPetType(newValue));
    }
    
    // Operation on Veterinian Specialties
    
    public Mono<Set<String>> listVeretinianSpecialties() {
        return findReferenceList(VET_SPECIALTY);
    }
    
    public Mono<String> addVeretinianSpecialty(String value) {
        return addToReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<Void> removeFromVeretinianSpecialty(String value) {
        return removeFromToReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<String> replaceVeretinianSpecialty(String oldValue, String newValue) {
        return removeFromVeretinianSpecialty(oldValue)
                .then(addVeretinianSpecialty(newValue));
    }
    
    protected Mono<Set<String>> findReferenceList(String listName) {
        return Mono.from(cqlSession.executeReactive(psReadList.bind(listName)))
                   .map(rr -> rr.getSet(REFLIST_ATT_VALUES, String.class));
    }
    
    /* 
     * update petclinic_reference_lists 
     * set values = values + {'something'} 
     * where list_name='vet_specialty';
     */
    SimpleStatement STMT_ADD_TO_LIST = 
            update(REFLIST_TABLE)
            .appendSetElement(REFLIST_ATT_VALUES, bindMarker())
            .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(bindMarker())
            .build();
    
    protected Mono<String> addToReferenceList(String listName, String newValue) {
        return Mono.fromDirect(cqlSession.executeReactive(update(REFLIST_TABLE)
                .appendSetElement(REFLIST_ATT_VALUES, literal(newValue))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then(Mono.just(newValue));
    }
    
    protected Mono<Void> removeFromToReferenceList(String listName, String newValue) {
        return Mono.from(cqlSession.executeReactive(update(REFLIST_TABLE)
                .removeSetElement(REFLIST_ATT_VALUES, literal(newValue))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then();
    }

}
