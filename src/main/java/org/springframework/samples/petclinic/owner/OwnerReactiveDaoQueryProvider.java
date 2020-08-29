package org.springframework.samples.petclinic.owner;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;

import reactor.core.publisher.Flux;

/**
 * Help for advanced queries
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class OwnerReactiveDaoQueryProvider implements CassandraPetClinicSchema {

    private final CqlSession session;
    
    private final EntityHelper<Owner> ownerHelper;
    
    private PreparedStatement psSearchOwner;
    
    public OwnerReactiveDaoQueryProvider(MapperContext ctx, EntityHelper<Owner> helper) {
      this.session       = ctx.getSession();
      this.ownerHelper   = helper;
      this.psSearchOwner = session.prepare(STMT_OWNER_SEARCHBYNAME);
    }
    
    public Flux<Owner> searchByOwnerName(String ownerName) {
        return  Flux.from(session.executeReactive(psSearchOwner.bind(ownerName)))
                    .map(ownerHelper::get);
    }
    
}
