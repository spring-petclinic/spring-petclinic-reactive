package org.springframework.samples.petclinic;

import static org.mockito.Mockito.mock;

import com.datastax.dse.driver.api.core.cql.reactive.ReactiveResultSet;
import com.datastax.dse.driver.api.core.cql.reactive.ReactiveRow;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.internal.core.cql.EmptyColumnDefinitions;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Mock Implementation of a {@link ReactiveResultSet}.
 *
 * @author Cedrick LUNVEN (@clunven)
 */
public class MockReactiveResultSet implements ReactiveResultSet {

  private final Publisher<ReactiveRow> source;

  public MockReactiveResultSet(ReactiveRow... rows) {
    this.source = Flux.fromArray(rows);
  }

  /** {@inheritDoc} */
  @NonNull @Override
  public Publisher<? extends ColumnDefinitions> getColumnDefinitions() {
    return Flux.just(EmptyColumnDefinitions.INSTANCE);
  }

  /** {@inheritDoc} */
  @NonNull @Override
  public Publisher<? extends ExecutionInfo> getExecutionInfos() {
    return Mono.just(mock(ExecutionInfo.class));
  }

  /** {@inheritDoc} */
  @NonNull @Override
  public Publisher<Boolean> wasApplied() {
    return Mono.just(false);
  }

  /** {@inheritDoc} */
  @Override
  public void subscribe(Subscriber<? super ReactiveRow> s) {
    source.subscribe(s);
  }
}
