package org.springframework.samples.petclinic.reflist;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.util.Set;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;

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
    
    /**
     * Constructor initializing and preparing statements at launch
     * to speed-up the queries later on.
     * 
     * @param cqlS
     *      current Cassandra Session injected by Spring
     */
    public ReferenceListReactiveDao(CqlSession cqlS) {
        this.cqlSession  = cqlS;
        
        /** 
         * The table has been designed with the list_name as partition key.
         * We want all values on a single Cassandra node (avoiding full scan cluster).
         * We pick an unordered set to avoid duplication, list will be sorted in the UI.
         * 
         * CREATE TABLE IF NOT EXISTS petclinic_reference_lists (
         *  list_name text,
         *  values set<text>,
         *  PRIMARY KEY ((list_name))
         * ); */
        cqlSession.execute(createTable(REFLIST_TABLE).ifNotExists()
                .withPartitionKey(REFLIST_ATT_LISTNAME, DataTypes.TEXT)
                .withColumn(REFLIST_ATT_VALUES, DataTypes.setOf(DataTypes.TEXT))
                .build());
        
        psReadList = cqlSession.prepare(
                selectFrom(REFLIST_TABLE).column(REFLIST_ATT_VALUES)
                .whereColumn(REFLIST_ATT_LISTNAME)
                .isEqualTo(QueryBuilder.bindMarker())
                .build());
        psUpsertList     = cqlSession.prepare(
                insertInto(REFLIST_TABLE)
                .value(REFLIST_ATT_LISTNAME, QueryBuilder.bindMarker())
                .value(REFLIST_ATT_VALUES, QueryBuilder.bindMarker())
                .build());
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
