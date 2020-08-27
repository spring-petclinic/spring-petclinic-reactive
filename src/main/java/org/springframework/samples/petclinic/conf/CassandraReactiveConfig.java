package org.springframework.samples.petclinic.conf;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractReactiveCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.ReactiveCqlTemplate;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.OptionsMap;
import com.datastax.oss.driver.api.core.config.TypedDriverOption;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

/**
 * Enabling ReactiveCqlTemplate to execute custom queries.
 * 
 * @note Behaviour of Spring Data Cassandra and Design. Testing different conventions this is what we notice
 * 
 * 1. Using ONLY crud repository (ReactiveCassandraRepository) and tables,
 * we can leverage on autoconfiguration. Spring Data load all key spring.data.cassandra.
 * and initiate a CqlSession bean. To connect to ASTRA we can simply add a bean
 * 
 * @Bean
 * public CqlSessionBuilderCustomizer sessionBuilderCustomizer() {
 *   Path bundle = new File("secure.zip").toPath();
 *   return builder -> builder.withCloudSecureConnectBundle(bundle);
 * }
 * 
 * 2. To be able to use and inject {@link ReactiveCqlTemplate} you need to add the current class with
 * the @EnableReactiveCassandraRepositories AND extending AbstractReactiveCassandraConfiguration. But then
 * for some reason the CqlSession is not initialized in the same way. getKeySpace(), getDatacenter() override
 * everything and keys are not loaded.
 * 
 * As a consequence here the CqlSession if initialized manually even if using the standard keys. We added
 * datastax.astra.enabled to switch fron local cassandra to DBaas DataStax Astra without code change.
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
