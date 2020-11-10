package org.springframework.samples.petclinic.conf;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDao;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDaoMapper;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDaoMapperBuilder;
import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.pet.db.PetReactiveDaoMapper;
import org.springframework.samples.petclinic.pet.db.PetReactiveDaoMapperBuilder;
import org.springframework.samples.petclinic.vet.db.VetReactiveDao;
import org.springframework.samples.petclinic.vet.db.VetReactiveDaoMapper;
import org.springframework.samples.petclinic.vet.db.VetReactiveDaoMapperBuilder;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDaoMapper;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDaoMapperBuilder;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

/**
 * Setup connectivity to Cassandra (locally or using Dbaas) using the Datastax Java driver and configuration files.
 * Define different dao(s) as singletons for the application, initializing table and statements when relevant.
 * 
 * - Documentation on CqlSession with Java Driver
 * {@link https://docs.datastax.com/en/developer/java-driver/latest/manual/core/}
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
public class CassandraConfig {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraConfig.class);
   
    /**
     * This flag will help us decide between 2 configurations files
     * `application-astra.conf` or `application-local.conf`
     * 
     * Why not simply injecting the filename ? If we are using local
     * we also create the keyspace.
     */
    @Value("${petclinic.astra.enable:true}")
    private boolean useAstra;
    
    @Value("${petclinic.cassandra.local-keyspace.create}")
    private boolean localKeyspaceCreate;
    
    @Value("${petclinic.cassandra.local-keyspace.name}")
    private String localKeyspaceName;
    
    @Value("${petclinic.cassandra.local-keyspace.cql}")
    private String localKeyspaceCql;
    
    /**
     * Create the Singleton {@link CqlSession} used everywhere to 
     * access the Cassandra DB.
     */
    @Bean
    public CqlSession cqlSession() {
        
        LOGGER.info("Reading configuration");
        Map<String, String> env = System.getenv();
        if (env != null) {
            for (String key : env.keySet()) {
                LOGGER.info("key=" + key + ", value=" + env.get(key));
            }
        }
        
        DriverConfigLoader configReader;
        CqlSession cqlSession;
        if (useAstra) {
             LOGGER.info("Loading configuration to Astra");
             // the file 'application-astra.conf' contains all configuration keys
             configReader = DriverConfigLoader.fromClasspath("application-astra.conf");
             cqlSession   = CqlSession.builder().withConfigLoader(configReader).build();
        } else {
             // the file 'application-astra.local' contains all configuration keys to work locally
             configReader = DriverConfigLoader.fromClasspath("application-local.conf");
             cqlSession   = CqlSession.builder().withConfigLoader(configReader).build();
             // If we are working locally (docker) we may need to create the keypace
             if (localKeyspaceCreate) {
                 cqlSession.execute(localKeyspaceCql);
             }
             cqlSession.execute("use " + localKeyspaceName);
        }
        
        // Create schema upfront
        return cqlSession;
    }
    
    /**
     * Initialized {@link VetReactiveDaos} as a Spring Singleton.
     * 
     * It will hold  implementations of accesses to Cassandra DB
     * for Vet business domain.
     */
    @Bean
    public VetReactiveDao vetDao(CqlSession cqlSession) {
        // A mapper is initiliazed with a Session.
        VetReactiveDaoMapper vetMapper = new VetReactiveDaoMapperBuilder(cqlSession).build();
        // Create tables required for this DAO.
        vetMapper.createSchema(cqlSession);
        // From the mapper we can access the Dao instance by specifying the proper keyspace.
        VetReactiveDao vetDao = vetMapper.vetDao(cqlSession.getKeyspace().get());
        return vetDao;
    }
 
    /**
     * Initialized {@link OwnerReactiveDao} as a Spring Singleton.
     * It will hold the implementations of access to Cassandra DB
     */
    @Bean
    public OwnerReactiveDao ownerDao(CqlSession cqlSession) {
        OwnerReactiveDaoMapper ownerMapper = new OwnerReactiveDaoMapperBuilder(cqlSession).build();
        ownerMapper.createSchema(cqlSession);
        OwnerReactiveDao ownerDao = ownerMapper.ownerDao(cqlSession.getKeyspace().get());
        return ownerDao;
    }
    
    /**
     * Initialized {@link PetReactiveDao} as a Spring Singleton.
     * It will hold the implementations of access to Cassandra DB
     */
    @Bean
    public PetReactiveDao petDao(CqlSession cqlSession) {
        PetReactiveDaoMapper petMapper = new PetReactiveDaoMapperBuilder(cqlSession).build(); 
        petMapper.createSchema(cqlSession);
        PetReactiveDao petDao = petMapper.petDao(cqlSession.getKeyspace().get());
        return petDao;
    }
    
    /**
     * Initialized {@link VisitReactiveDao} as a Spring Singleton.
     * It will hold the implementations of access to Cassandra DB
     */
    @Bean
    public VisitReactiveDao visitDao(CqlSession cqlSession) {
        VisitReactiveDaoMapper visitMapper = new VisitReactiveDaoMapperBuilder(cqlSession).build();
        visitMapper.createSchema(cqlSession);
        VisitReactiveDao visitDao = visitMapper.visitDao(cqlSession.getKeyspace().get());
        return visitDao;
    }
}
