package org.springframework.samples.petclinic.conf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetReactiveRepository;
import org.springframework.stereotype.Component;

@Component
public class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    /** Injecting through contructor. */
    private final VetReactiveRepository vetRepository; 

    public SampleDataInitializer(VetReactiveRepository repository) {
        this.vetRepository = repository;
    }
    
    /** {@inheritDoc} */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        //Vet v1 = new Vet();
        //v1.setId(UUID.randomUUID());
        //v1.setFirstName("aaa");
        //v1.setLastName("bbbb");
        //v1.setSpecialties(new HashSet<>(Arrays.asList("surgery")));
        // Enforce a block to realize the operation.
        //vetRepository.save(v1).block();
    }

}