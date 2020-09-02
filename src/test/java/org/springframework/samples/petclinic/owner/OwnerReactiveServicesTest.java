package org.springframework.samples.petclinic.owner;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.datastax.oss.driver.api.core.CqlSession;

@ExtendWith(MockitoExtension.class)
public class OwnerReactiveServicesTest {

  @Mock
  private CqlSession session;

  private Owner owner1 = new Owner(
          UUID.fromString("11111111-1111-1111-1111-111111111111"), "John", 
                          "Connor", "T800 street", "Detroit", "0123456789");
  private Owner owner2 = new Owner(
          UUID.fromString("22222222-2222-2222-2222-222222222222"), "Sara", 
                          "Connor", "T1000 factoru", "Detroit", "9876543210");
  
  @Test
  void should_save_owner_when_save_given_valid_input() {
    // given
    //given(insert.bind(stock1.getSymbol(), stock1.getDate(), stock1.getValue())).willReturn(bound);
    //given(session.executeReactive(bound)).willReturn(new MockReactiveResultSet());
    // when
    //var stockRepository =
    //    new ReactiveStockRepository(session, insert, delete, findById, findBySymbol, rowMapper);
    //var mono = stockRepository.save(stock1);
    // then
    //StepVerifier.create(mono).expectNext(stock1).expectComplete().verify();
    //verify(session).executeReactive(bound);
  }
  
}