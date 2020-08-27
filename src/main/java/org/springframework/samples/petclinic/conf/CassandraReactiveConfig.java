package org.springframework.samples.petclinic.conf;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.OptionsMap;
import com.datastax.oss.driver.api.core.config.TypedDriverOption;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

import org.slf4j.Logger;

/**
 * If we only use ReactiveCassandraRepository we can leverage on autoconfiguration.
 * 
 * If we need ReactiveCqlTemplate (here yes)
 * - @EnableReactiveCassandraRepositories is not enough
 * - you need to extends AbstractReactiveCassandraConfiguration
 * - When you use AbstractReactiveCassandraConfiguration autoconfiguration is KO
 * 
 * As a conseqience I define  CqlSession based on Sring Data Keys but manually
 * I reuse same keys from spring-data
 * 
 * @author Cedrick LUNVEN (@clunven)
 */
@Configuration
@EnableReactiveCassandraRepositories(basePackages = {"org.springframework.samples.petclinic.owner"})
public class CassandraReactiveConfig extends AbstractReactiveCassandraConfiguration {
    
    /** Logger for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraReactiveConfig.class);
    
    /** Spring Data Keys when autoconfiguration is OK (no AbstractReactiveCassandraConfiguration). **/
   
    @Value("${spring.data.cassandra.keyspace-name}")
    private String keyspace;
    
    @Value("${spring.data.cassandra.schema-action}")
    private String schemaAction;
    
    @Value("${spring.data.cassandra.local-datacenter}")
    private String dc;
    
    @Value("${spring.data.cassandra.username}")
    private String username;
    
    @Value("${spring.data.cassandra.password}")
    private String password;
    
    @Value("${spring.data.cassandra.port}")
    private int port;
    
    @Value("#{'${spring.data.cassandra.contact-points}'.split(',')}") 
    private List<String> contactPoints;
    
    @Value("${datastax.astra.enabled}")
    private boolean useAstra;
    
    @Value("${datastax.astra.secure-connect-bundle}")
    private String secureConnectBundle;
    
    /** get RequiredSession is invoked multiple time = singleton. */
    private CqlSession cqlSession;
    
    /** {@inheritDoc} */
    @Override
    protected CqlSession getRequiredSession() {
        if (null == cqlSession) {
            LOGGER.info("Initializing connection to Cassandra...");
            OptionsMap om = OptionsMap.driverDefaults();
            if (useAstra) {
                LOGGER.info("Connection to Datastax Astrax:");
                om.put(TypedDriverOption.CLOUD_SECURE_CONNECT_BUNDLE, secureConnectBundle);
                om.put(TypedDriverOption.AUTH_PROVIDER_CLASS, "PlainTextAuthProvider");
                om.put(TypedDriverOption.AUTH_PROVIDER_USER_NAME, username);
                om.put(TypedDriverOption.AUTH_PROVIDER_PASSWORD, password);
                om.put(TypedDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(10));
                om.put(TypedDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, Duration.ofSeconds(10));
                om.put(TypedDriverOption.CONNECTION_SET_KEYSPACE_TIMEOUT, Duration.ofSeconds(10));
                om.put(TypedDriverOption.CONTROL_CONNECTION_AGREEMENT_TIMEOUT, Duration.ofSeconds(10));
            } else {
                LOGGER.info("+ Connection to a Local Cassandra instance");
                createLocalKeyspace(getKeyspaceName(), 1);
                //om.put(TypedDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, dc); // not used
                om.put(TypedDriverOption.CONTACT_POINTS, contactPoints);
            }
            cqlSession = CqlSession.builder().withConfigLoader(DriverConfigLoader.fromMap(om)).build();
            LOGGER.info("[OK] Connection established to keyspace '{}'", keyspace);
        }
        return cqlSession;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getLocalDataCenter() {
        return dc;
    }
    
    /** {@inheritDoc} */
    @Override
    public String getKeyspaceName() {
        return keyspace;
    }
    
    /** {@inheritDoc} */
    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(schemaAction);
    }
    
   
    
    private void createLocalKeyspace(String keyspaceName, int replicationFactor) {
        try (CqlSession cqlSession = CqlSession.builder()
                .addContactPoints(contactPoints.stream()
                                               .map(cp -> new InetSocketAddress(cp, port))
                                               .collect(Collectors.toList()))
                .withLocalDatacenter(getLocalDataCenter())
                .build()) {
            cqlSession.execute(createKeyspaceSimpleStrategy(keyspaceName, replicationFactor));
            LOGGER.info("+ Keyspace '{}' created (if needed).", getKeyspaceName());
        }
    }
    
    private SimpleStatement createKeyspaceSimpleStrategy(String keyspaceName, int replicationFactor) {
        return SchemaBuilder.createKeyspace(keyspaceName)
                    .ifNotExists()
                    .withSimpleStrategy(replicationFactor)
                    .withDurableWrites(true)
                    .build();
    }

}
