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

public class ProductTest extends BinaryOperatorTest<Product> {

    @Test
    public void testAggregateInShortMode() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply((short) 2, null);

        // Then 1
        assertThat(state)
                .isEqualTo((short) 2)
                .isExactlyInstanceOf(Short.class);

        // When 2
        state = product.apply((short) 3, state);

        // Then 2
        assertThat(state)
                .isEqualTo((short) 6)
                .isExactlyInstanceOf(Short.class);

        // When 3
        state = product.apply((short) 8, state);

        // Then 3
        assertThat(state)
                .isEqualTo((short) 48)
                .isExactlyInstanceOf(Short.class);

        // When 4 - check it cannot exceed MAX_VALUE
        state = product.apply(Short.MAX_VALUE, state);

        // Then 4
        assertThat(state)
                .isEqualTo(Short.MAX_VALUE)
                .isExactlyInstanceOf(Short.class);
    }

    @Test
    public void testAggregateInIntMode() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(2, null);

        // Then 1
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        state = product.apply(3, state);

        // Then 2
        assertThat(state)
                .isEqualTo(6)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        state = product.apply(8, state);

        // Then 3
        assertThat(state)
                .isEqualTo(48)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInIntModeMixedInput() {
        // Given
        final Product product = new Product();

        // When 1
        final Number state = product.apply(2, null);

        // Then 1
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(2.7d, state));

        // Then 2
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(1L, state));

        // Then 3
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInLongMode() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(2L, null);

        // Then 1
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        state = product.apply(1L, state);

        // Then 2
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        state = product.apply(3L, state);

        // Then 3
        assertThat(state)
                .isEqualTo(6L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInLongModeMixedInput() {
        // Given
        final Product product = new Product();
        final Number state1 = 1L;

        // When 1
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(1, state1));

        // Then 1
        assertThat(state1)
                .isEqualTo(1L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        final Number state2 = product.apply(3L, 1L);

        // Then 2
        assertThat(state2)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(2.5d, state2));

        // Then 3
        assertThat(state2)
                .isEqualTo(3L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInDoubleMode() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(1.2d, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1.2d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        state = product.apply(2.5d, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3.0d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        state = product.apply(1.5d, state);

        // Then 3
        assertThat(state)
                .isEqualTo(4.5d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateInFloatMode() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(1.2f, null);

        // Then 1
        assertThat(state)
                .isEqualTo(1.2f)
                .isExactlyInstanceOf(Float.class);

        // When 2
        state = product.apply(2.5f, state);

        // Then 2
        assertThat(state)
                .isEqualTo(3.0f)
                .isExactlyInstanceOf(Float.class);

        // When 3
        state = product.apply(1.5f, state);

        // Then 3
        assertThat(state)
                .isEqualTo(4.5f)
                .isExactlyInstanceOf(Float.class);
    }

    @Test
    public void testAggregateInDoubleModeMixedInput() {
        // Given
        final Product product = new Product();
        final Number state = 1d;

        // When 1
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(1, state));

        // Then 1
        assertThat(state)
                .isEqualTo(1d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(3L, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        final Number state2 = product.apply(2.1d, state);

        // Then 3
        assertThat(state2)
                .isEqualTo(2.1d)
                .isExactlyInstanceOf(Double.class);
    }

    @Test
    public void testAggregateInAutoModeIntInputFirst() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(2, 1);

        // Then 1
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(3L, state));

        // Then 2
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(2.1d, state));

        // Then 3
        assertThat(state)
                .isEqualTo(2)
                .isExactlyInstanceOf(Integer.class);
    }

    @Test
    public void testAggregateInAutoModeLongInputFirst() {
        // Given
        final Product product = new Product();

        // When 1
        Number state = product.apply(2L, 1L);

        // Then 1
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(3, state));

        // Then 2
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(2.1d, state));

        // Then 3
        assertThat(state)
                .isEqualTo(2L)
                .isExactlyInstanceOf(Long.class);
    }

    @Test
    public void testAggregateInAutoModeDoubleInputFirst() {
        // Given
        final Product product = new Product();

        // When 1
        final Number state = product.apply(1.1d, 1d);

        // Then 1
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 2
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(2, state));

        // Then 2
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);

        // When 3
        assertThatExceptionOfType(ClassCastException.class).isThrownBy(() -> product.apply(1L, state));

        // Then 3
        assertThat(state)
                .isEqualTo(1.1d)
                .isExactlyInstanceOf(Double.class);
    }


    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Product aggregator = new Product();

        // When 1
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.Product\"%n" +
                "}"), json);

        // When 2
        final Product deserialisedAggregator = JsonSerialiser.deserialise(json, Product.class);

        // Then 2
        assertThat(deserialisedAggregator).isNotNull();
    }

    @Override
    protected Product getInstance() {
        return new Product();
    }

    @Override
    protected Iterable<Product> getDifferentInstancesOrNull() {
        return null;
    }
}
