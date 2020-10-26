package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;

import java.util.HashSet;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDao;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.db.PetReactiveDao;
import org.springframework.samples.petclinic.visit.db.VisitReactiveDao;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Testing the {@link OwnerReactiveServices} layer.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
@ExtendWith(MockitoExtension.class)
public class OwnerReactiveServicesTest {

  @Mock
  private PetReactiveDao petDao;
  
  @Mock
  private VisitReactiveDao visitDao;

  @Mock
  private OwnerReactiveDao ownerDao;
  
  @Mock
  private OwnerReactiveServices ownerServices;
  
  private Owner o1 = new Owner(
          UUID.fromString("11111111-1111-1111-1111-111111111111"), 
          "John", "Connor", "T800 street", "Detroit", "0123456789", new HashSet<Pet>());
  
  @Test
  void should_save_owner_when_save_given_valid_input() {
    // Given
    given(ownerServices.createOwner(o1)).willReturn(Mono.just(o1));
    // When
    var mono = ownerServices.createOwner(o1);
    // Then
    StepVerifier.create(mono).expectNext(o1).expectComplete().verify();
  }
  
}