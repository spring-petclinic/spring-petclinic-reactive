package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.conf.MappingUtils;
import org.springframework.samples.petclinic.owner.db.OwnerEntity;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveDao;
import org.springframework.samples.petclinic.owner.db.OwnerReactiveServicesImpl;
import org.springframework.samples.petclinic.pet.PetReactiveDao;
import org.springframework.samples.petclinic.visit.VisitReactiveDao;

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

  private Owner o1 = new Owner(
          UUID.fromString("11111111-1111-1111-1111-111111111111"), 
          "John", "Connor", "T800 street", "Detroit", "0123456789");
  
  @Test
  void should_save_owner_when_save_given_valid_input() {
    // Given
    OwnerEntity e1 = MappingUtils.mapOwnerAsEntity(o1);
    given(ownerDao.save(e1)).willReturn(Mono.just(e1));
    // When
    var ownerService = new OwnerReactiveServicesImpl(ownerDao, petDao, visitDao);
    var mono = ownerService.createOwner(o1);
    // Then
    StepVerifier.create(mono).expectNext(o1).expectComplete().verify();
  }
  
}