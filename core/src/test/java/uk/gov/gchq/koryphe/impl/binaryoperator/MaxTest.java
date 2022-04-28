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

package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MaxTest extends BinaryOperatorTest<Max> {

    @Test
    public void testAggregateInIntMode() {
        // Given
        final Max max = new Max();

        // When 1
        Comparable state = max.apply(1, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        state = max.apply(3, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        state = max.apply(2, state);

        // Then 3
        assertThat(state)
                .isEqualTo(3)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInLongMode() {
        // Given
        final Max max = new Max();

        // When 1
        Comparable state = max.apply(2L, null);

        // Then 1
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        state = max.apply(1L, state);

        // Then 2
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        state = max.apply(3L, state);

        // Then 3
        assertThat(state)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInDoubleMode() {
        // Given
        final Max max = new Max();

        // When 1
        Comparable state = max.apply(1.1d, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        state = max.apply(2.1d, state);

        // Then 2
        assertThat(state)
                .isEqualTo(2.1d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        state = max.apply(1.5d, state);

        // Then 3
        assertThat(state)
                .isEqualTo(2.1d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateMixedInput() {
        // Given
        final Max max = new Max();

        // When 1
        final Comparable newState = max.apply(null, 1);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(
            () -> max.apply(newState, 3L)
        );

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(
            () -> max.apply(newState, 2.1d)
        );
        // Then 3
        assertThat(newState)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Max aggregator = new Max();

        // When 1
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.Max\"%n" +
                "}"), json);

        // When 2
        final Max deserialisedAggregator = JsonSerialiser.deserialise(json, Max.class);

        // Then 2
        assertThat(deserialisedAggregator).isNotNull();
    }

    @Override
    protected Max getInstance() {
        return new Max();
    }

    @Override
    protected Iterable<Max> getDifferentInstancesOrNull() {
        return null;
    }
}
