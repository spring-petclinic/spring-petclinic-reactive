/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class MockReactiveResultSet implements ReactiveResultSet {

  private final Publisher<ReactiveRow> source;

  public MockReactiveResultSet(ReactiveRow... rows) {
    this.source = Flux.fromArray(rows);
  }

  @NonNull
  @Override
  public Publisher<? extends ColumnDefinitions> getColumnDefinitions() {
    return Flux.just(EmptyColumnDefinitions.INSTANCE);
  }

  @NonNull
  @Override
  public Publisher<? extends ExecutionInfo> getExecutionInfos() {
    return Mono.just(mock(ExecutionInfo.class));
  }

  @NonNull
  @Override
  public Publisher<Boolean> wasApplied() {
    return Mono.just(false);
  }

  @Override
  public void subscribe(Subscriber<? super ReactiveRow> s) {
    source.subscribe(s);
  }
}
