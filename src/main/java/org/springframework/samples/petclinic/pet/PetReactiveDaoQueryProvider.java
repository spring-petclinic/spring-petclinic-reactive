package org.springframework.samples.petclinic.pet;

import org.springframework.samples.petclinic.conf.CassandraPetClinicSchema;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;

/**
 * Help for advanced queries
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class PetReactiveDaoQueryProvider implements CassandraPetClinicSchema {

 private final CqlSession session;
    
    private final EntityHelper<Pet> petHelper;
    
    private PreparedStatement psSearchOwner;
    
    public PetReactiveDaoQueryProvider(MapperContext ctx, EntityHelper<Pet> helper) {
      this.session       = ctx.getSession();
      this.petHelper   = helper;
      this.psSearchOwner = session.prepare(STMT_OWNER_SEARCHBYNAME);
    }
    
}
