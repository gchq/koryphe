/*
 * Copyright 2017-2020 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AreEqualTest extends PredicateTest<AreEqual> {

    @Test
    public void shouldAcceptTheWhenEqualValues() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        Tuple2<Object, Object> inputs = new Tuple2<>("test", "test");

        // Then
        assertThat(equals).accepts(inputs);
    }

    @Test
    public void shouldAcceptWhenAllNull() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        Tuple2<Object, Object> inputs = new Tuple2<>(null, null);

        // Then
        assertThat(equals).accepts(inputs);
    }

    @Test
    public void shouldRejectWhenOneIsNull() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        Tuple2<Object, Object> inputs = new Tuple2<>(null, "test");

        // Then
        assertThat(equals).rejects(inputs);
    }

    @Test
    public void shouldRejectWhenNotEqual() {
        // Given
        final AreEqual equals = new AreEqual();

        // When
        Tuple2<Object, Object> inputs = new Tuple2<>("test", "test2");

        // Then
        assertThat(equals).rejects(inputs);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AreEqual filter = new AreEqual();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.AreEqual\"%n" +
                "}"), json);

        // When 2
        final AreEqual deserialisedFilter = JsonSerialiser.deserialise(json, AreEqual.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
    }

    @Override
    protected AreEqual getInstance() {
        return new AreEqual();
    }

    @Override
    protected Iterable<AreEqual> getDifferentInstancesOrNull() {
        return null;
    }
}
