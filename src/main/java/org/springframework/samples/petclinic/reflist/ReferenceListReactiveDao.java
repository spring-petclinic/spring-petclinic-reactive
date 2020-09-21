package org.springframework.samples.petclinic.reflist;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.insertInto;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.selectFrom;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;
import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.createTable;

import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;

import reactor.core.publisher.Mono;

/**
 * Storing all finite list of elements has been implemented using a single
 * table name `petclinic_reference_lists`. Purpose is to retrieve all the values
 * of a reference list with a single call, on a SINGLE NODE (aka single partition).
 * 
 * - The partition key is the name of the list (list_name)
 * - The list of values are a saved in 'values'
 */
@Component
public class ReferenceListReactiveDao implements InitializingBean {
    
    /** Group constants .*/
    public static final String REFLIST_TABLE         = "petclinic_reference_lists";
    public static final String REFLIST_ATT_LISTNAME  = "list_name";
    public static final String REFLIST_ATT_VALUES    = "values";
    
    /** Explicit usage of the CqlSession. */
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
    }
    
    /**
     * Spring interface {@link InitializingBean} let you execute some
     * coode after bean has been initialized.
     * 
     * Here we enter default values for pet list.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
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
    
    /**
     * CqlSession should be be gracefullyy closed at application shutdown.
     *
     * https://docs.datastax.com/en/latest-java-driver-api/com/datastax/oss/driver/api/core/AsyncAutoCloseable.html#close--
     */
    @PreDestroy
    public void cleanUp() {
        if (null != cqlSession) {
            cqlSession.closeAsync();
        }
    }
        
    /**
     * Save a reference list in one call.
     *
     * @param listName
     *      the list name
     * @param values
     *      different values of the list
     * @return
     *      nothing to return
     */
    public Mono<Void> saveList(String listName, Set<String> values) {
        return Mono.from(cqlSession.executeReactive(psUpsertList.bind(listName, values)))
                   .then();
    }
    
    /**
     * Retrieve the set of values in one call.
     * 
     * @param listName
     *      list name
     * @return
     *      list of values
     */
    public Mono<Set<String>> findReferenceList(String listName) {
        return Mono.from(cqlSession.executeReactive(psReadList.bind(listName)))
                   .map(rr -> rr.getSet(REFLIST_ATT_VALUES, String.class));
    }
    
    /**
     * Add an element to existing list.
     *
     * @param listName
     *      existing list name
     * @param newValue
     *      new value to add
     * @return
     *      the new value
     */
    public Mono<String> addToReferenceList(String listName, String newValue) {
        return Mono.fromDirect(cqlSession.executeReactive(update(REFLIST_TABLE)
                .appendSetElement(REFLIST_ATT_VALUES, literal(newValue))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then(Mono.just(newValue));
    }
    
    /**
     * Remove an element form existing list.
     *
     * @param listName
     *      existing list name
     * @param valueToDelete
     *      value to delete
     * @return
     *      nothing to return.
     */
    public Mono<Void> removeFromReferenceList(String listName, String valueToDelete) {
        return Mono.from(cqlSession.executeReactive(update(REFLIST_TABLE)
                .removeSetElement(REFLIST_ATT_VALUES, literal(valueToDelete))
                .whereColumn(REFLIST_ATT_LISTNAME).isEqualTo(literal(listName))
                .build())).then();
    }

}
