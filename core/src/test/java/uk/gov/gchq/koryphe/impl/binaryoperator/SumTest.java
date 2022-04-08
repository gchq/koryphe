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

public class SumTest extends BinaryOperatorTest<Sum> {

    @Test
    public void testAggregateShorts() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply((short) 1, null);

        // Then 1
        assertThat(state)
                .isEqualTo((short) 1)
                .isExactlyInstanceOf(Short.class);

        // When 2
        state = sum.apply((short) 3, state);

        // Then 2
        assertThat(state)
                .isEqualTo((short) 4)
                .isExactlyInstanceOf(Short.class);

        // When 3
        state = sum.apply((short) 2, state);

        // Then 3
        assertThat(state)
                .isEqualTo((short) 6)
                .isExactlyInstanceOf(Short.class);

        // When 4 - check it cannot exceed MAX_VALUE
        state = sum.apply((short) (Short.MAX_VALUE - (short) state + 1), state);

        // Then 4
        assertThat(state)
                .isEqualTo(Short.MAX_VALUE)
                .isExactlyInstanceOf(Short.class);
    }

    @Test
    public void testAggregateInIntMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply(1, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        state = sum.apply(3, state);

        // Then 2
        assertThat(state)
                .isEqualTo(4)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        state = sum.apply(2, state);

        // Then 3
        assertThat(state)
                .isEqualTo(6)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInIntModeMixedInput() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply(1, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(2.7d, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(1L, state));

        // Then 3
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInLongMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply(2L, null);

        // Then 1
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        state = sum.apply(1L, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        state = sum.apply(3L, state);

        // Then 3
        assertThat(state)
                .isEqualTo(6L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInLongModeMixedInput() {
        // Given
        final Sum sum = new Sum();
        final Number state = 0L;

        // When 1
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(1, state));

        // Then 1
        assertThat(state)
                .isEqualTo(0L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        final Number state2 = sum.apply(3L, state);

        // Then 2
        assertThat(state2)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(2.5d, state2));

        // Then 3
        assertThat(state2)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInFloatMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply(1.1f, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1.1f)
                .isExactlyInstanceOf(Float.class);

        // When 2
        state = sum.apply(2f, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3.1f)
                .isExactlyInstanceOf(Float.class);

        // When 3
        state = sum.apply(1.5f, state);

        // Then 3
        assertThat(state)
                .isEqualTo(4.6f)
                .isExactlyInstanceOf(Float.class);
    }

    @Test
    public void testAggregateInDoubleMode() {
        // Given
        final Sum sum = new Sum();

        // When 1
        Number state = sum.apply(1.1d, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        state = sum.apply(2.1d, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3.2d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        state = sum.apply(1.5d, state);

        // Then 3
        assertThat(state)
                .isEqualTo(4.7d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateInDoubleModeMixedInput() {
        // Given
        final Sum sum = new Sum();
        final Number state = 0d;

        // When 1
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(1, state));

        // Then 1
        assertThat(state)
                .isEqualTo(0d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(3, state));

        // Then 2
        assertThat(state)
                .isEqualTo(0d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        final Number state2 = sum.apply(2.1d, state);

        // Then 3
        assertThat(state2)
                .isEqualTo(2.1d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateInAutoModeIntInputFirst() {
        // Given
        final Sum sum = new Sum();

        // When 1
        final Number state = sum.apply(1, 0);

        // Then 1
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(3L, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(2.1d, state));

        // Then 3
        assertThat(state)
                .isEqualTo(1)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInAutoModeLongInputFirst() {
        // Given
        final Sum sum = new Sum();

        // When 1
        final Number state = sum.apply(1L, 0L);

        // Then 1
        assertThat(state)
                .isEqualTo(1L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(3, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(2.1d, state));

        // Then 3
        assertThat(state)
                .isEqualTo(1L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInAutoModeDoubleInputFirst() {
        // Given
        final Sum sum = new Sum();

        // When 1
        final Number state = sum.apply(1.1d, 0d);

        // Then 1
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(2, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> sum.apply(1L, state));

        // Then 3
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateWhenStateIsNullShouldReturnNull() {
        // Given
        final Sum sum = new Sum();

        // When
        final Number state = sum.apply(null, null);

        // Then
        assertThat(state).isNull();
    }

    @Test
    public void testAggregateInAutoModeIntInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        int firstValue = 1;
        Number state = sum.apply(firstValue, null);

        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        state = sum.apply(null, state);
        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInAutoModeLongInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        long firstValue = 1L;
        Number state = sum.apply(firstValue, null);

        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Long.class);

        // When 2
        state = sum.apply(null, state);

        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInAutoModeDoubleInputFirstNullInputSecond() {
        // Given
        final Sum sum = new Sum();

        // When 1
        double firstValue = 1.0f;
        Number state = sum.apply(firstValue, null);

        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Double.class);

        // When 2
        state = sum.apply(null, state);

        // Then
        assertThat(state)
                .isEqualTo(firstValue)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Sum aggregator = new Sum();

        // When 1
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"%n" +
                "}"), json);

        // When 2
        final Sum deserialisedAggregator = JsonSerialiser.deserialise(json, Sum.class);

        // Then 2
        assertThat(deserialisedAggregator).isNotNull();
    }

    @Override
    protected Sum getInstance() {
        return new Sum();
    }

    @Override
    protected Iterable<Sum> getDifferentInstancesOrNull() {
        return null;
    }
}
