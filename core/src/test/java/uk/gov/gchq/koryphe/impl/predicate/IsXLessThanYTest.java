/*
 * Copyright 2017-2022 Crown Copyright
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
import uk.gov.gchq.koryphe.signature.InputValidatorAssert;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class IsXLessThanYTest extends PredicateTest<IsXLessThanY> {

    @Test
    public void shouldAcceptWhenLessThan() {
        // Given
        final IsXLessThanY filter = new IsXLessThanY();

        // When
        Tuple2 inputs = new Tuple2<>(1, 2);

        // Then
        assertThat(filter).accepts(inputs);
    }

    @Test
    public void shouldRejectTheValueWhenMoreThan() {
        // Given
        final IsXLessThanY filter = new IsXLessThanY();

        // When
        Tuple2 inputs = new Tuple2<>(6, 5);

        // Then
        assertThat(filter).rejects(inputs);
    }

    @Test
    public void shouldRejectTheValueWhenEqualTo() {
        // Given
        final IsXLessThanY filter = new IsXLessThanY();

        // When
        Tuple2 inputs = new Tuple2<>(5, 5);

        // Then
        assertThat(filter).rejects(inputs);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsXLessThanY filter = new IsXLessThanY();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsXLessThanY\"%n" +
                "}"), json);

        // When 2
        final IsXLessThanY deserialisedFilter = JsonSerialiser.deserialise(json, IsXLessThanY.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
    }

    @Test
    public void shouldCheckInputClass() {
        // When
        final IsXLessThanY predicate = new IsXLessThanY();

        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(Integer.class, Integer.class)
                .acceptsInput(String.class, String.class)
                .rejectsInput(Double.class)
                .rejectsInput(Integer.class, Double.class);
    }

    @Override
    protected IsXLessThanY getInstance() {
        return new IsXLessThanY();
    }

    @Override
    protected Iterable<IsXLessThanY> getDifferentInstancesOrNull() {
        return null;
    }
}
