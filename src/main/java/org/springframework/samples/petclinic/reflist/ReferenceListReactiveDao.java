package org.springframework.samples.petclinic.reflist;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

import java.util.Set;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;

import reactor.core.publisher.Mono;

// TODO: more descriptive interface comment?
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

    // TODO: param comments don't seem to match actual method signature
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
    
    // Operations on Pet Types
    
    public Mono<Set<String>> listPetType() {
        return findReferenceList(PET_TYPE);
    }
    
    public Mono<String> addPetType(String value) {
        return addToReferenceList(PET_TYPE, value);
    }
    
    public Mono<Void> removePetType(String value) {
        return removeFromReferenceList(PET_TYPE, value);
    }
    
    public Mono<String> replacePetType(String oldValue, String newValue) {
        return removePetType(oldValue).then(addPetType(newValue));
    }
    
    // Operations on Veterinarian Specialties

    public Mono<Set<String>> listVetSpecialties() {
        return findReferenceList(VET_SPECIALTY);
    }
    
    public Mono<String> addVetSpecialty(String value) {
        return addToReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<Void> removeVetSpecialty(String value) {
        return removeFromReferenceList(VET_SPECIALTY, value);
    }
    
    public Mono<String> replaceVetSpecialty(String oldValue, String newValue) {
        return removeVetSpecialty(oldValue)
                .then(addVetSpecialty(newValue));
    }
    
    protected Mono<Set<String>> findReferenceList(String listName) {
        return Mono.from(cqlSession.executeReactive(psReadList.bind(listName)))
                   .map(rr -> rr.getSet(REFLIST_ATT_VALUES, String.class));
    }
    
    protected Mono<String> addToReferenceList(String listName, String newValue) {
        return Mono.fromDirect(cqlSession.executeReactive(update(REFLIST_TABLE)
                .appendSetElement(REFLIST_ATT_VALUES, literal(newValue))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then(Mono.just(newValue));
    }
    
    protected Mono<Void> removeFromReferenceList(String listName, String newValue) {
        return Mono.from(cqlSession.executeReactive(update(REFLIST_TABLE)
                .removeSetElement(REFLIST_ATT_VALUES, literal(newValue))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then();
    }

}
