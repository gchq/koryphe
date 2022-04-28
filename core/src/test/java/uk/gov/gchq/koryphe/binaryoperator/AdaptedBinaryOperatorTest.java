/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.StringConcat;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.impl.function.DivideBy;
import uk.gov.gchq.koryphe.impl.function.MultiplyBy;
import uk.gov.gchq.koryphe.impl.function.MultiplyLongBy;
import uk.gov.gchq.koryphe.impl.function.ToInteger;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.TupleInputAdapter;
import uk.gov.gchq.koryphe.tuple.TupleOutputAdapter;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class AdaptedBinaryOperatorTest extends BinaryOperatorTest<AdaptedBinaryOperator> {
    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        AdaptedBinaryOperator adaptedBinaryOperator = new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyBy(5));
        String json =
                "{\n" +
                        "\"binaryOperator\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Product\"\n" +
                        "},\n" +
                        "\"inputAdapter\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.impl.function.ToLong\"\n" +
                        "},\n" +
                        "\"outputAdapter\": {\n" +
                            "\"class\": \"uk.gov.gchq.koryphe.adapted.StateAgnosticOutputAdapter\",\n" +
                            "\"adapter\": {\n" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.function.MultiplyBy\",\n" +
                                "\"by\": 5\n" +
                            "}\n" +
                        "}\n" +
                    "}";
        // When
        String serialised = JsonSerialiser.serialise(adaptedBinaryOperator);
        AdaptedBinaryOperator deserialised = JsonSerialiser.deserialise(json, AdaptedBinaryOperator.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(adaptedBinaryOperator);
    }

    @Override
    protected AdaptedBinaryOperator getInstance() {
        return new AdaptedBinaryOperator(new Sum(), new ToLong(), new MultiplyBy(5));
    }

    @Override
    protected Iterable<AdaptedBinaryOperator> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyBy(5)),
                new AdaptedBinaryOperator(new Sum(), new ToInteger(), new MultiplyBy(5)),
                new AdaptedBinaryOperator(new Sum(), new ToLong(), new DivideBy(5)),
                new AdaptedBinaryOperator()
        );
    }

    @Test
    public void IfNoOutputAdapterIsSpecifiedShouldReturnNewState() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), new ToLong(), (BinaryOperator<Number>) null);

        // When
        Object aggregated = abo.apply(2, 5);

        // Then
        assertThat(aggregated).isEqualTo(10L);
    }

    @Test
    public void shouldAdaptOutputIfOutputAdapterIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), new ToLong(), new MultiplyLongBy(5L));

        // When
        Object aggregated = abo.apply(2, 5);

        // Then
        assertThat(aggregated).isEqualTo(50L);
    }

    @Test
    public void shouldConsiderStateWhenOutputAdapterIsBiFunction() {
        // Given
        ArrayTuple state = new ArrayTuple();
        state.put(0, "tuple");

        ArrayTuple input = new ArrayTuple();
        state.put(0, "test");;

        TupleInputAdapter<Integer, String> inputAdapter = new TupleInputAdapter<>();
        inputAdapter.setSelection(new Integer[] { 0 });

        TupleOutputAdapter<Integer, String> outputAdapter = new TupleOutputAdapter<>();
        outputAdapter.setProjection(new Integer[] { 0 });

        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new StringConcat(" "), inputAdapter, outputAdapter);

        // When
        Object aggregated = abo.apply(state, input);

        // Then
        ArrayTuple expected = new ArrayTuple();
        state.put(0, "tuple test");
        assertThat(aggregated).isEqualTo(expected);
    }

    @Test
    public void shouldThrowExceptionIfNoBinaryOperatorIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> abo.apply("will", "fail"))
                .withMessage("BinaryOperator cannot be null");
    }

    @Test
    public void shouldPassInputDirectlyToBinaryOperatorIfNoInputAdapterIsSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), null, new MultiplyBy(10));

        // When
        Object aggregated = abo.apply(2, 5);

        // Then
        assertThat(aggregated)
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(100);
    }

    @Test
    public void shouldJustDelegateToBinaryOperatorIfNoAdaptersAreSpecified() {
        // Given
        AdaptedBinaryOperator abo = new AdaptedBinaryOperator(new Product(), null, (BiFunction) null);

        // When
        Object aggregated = abo.apply(2, 5);

        // Then
        assertThat(aggregated)
                .isExactlyInstanceOf(Integer.class)
                .isEqualTo(10);
    }
}
