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

package uk.gov.gchq.koryphe.tuple.binaryoperator;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.Max;
import uk.gov.gchq.koryphe.impl.binaryoperator.Product;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.tuple.ArrayTuple;
import uk.gov.gchq.koryphe.tuple.Tuple;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TupleAdaptedBinaryOperatorCompositeTest
        extends BinaryOperatorTest<TupleAdaptedBinaryOperatorComposite<Object>> {

    @Test
    @Override
    @SuppressWarnings("unchecked")
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final TupleAdaptedBinaryOperatorComposite<Object> instance = getInstance();
        final String json = "" +
                "{" +
                    "\"operators\": [" +
                        "{" +
                            "\"selection\": [\"input\", \"anotherInput\"]," +
                            "\"binaryOperator\": {" +
                                "\"class\": \"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"" +
                            "}" +
                        "}" +
                    "]" +
                "}";

        // When
        final String serialised = JsonSerialiser.serialise(instance);
        final TupleAdaptedBinaryOperatorComposite<Object> deserialised = JsonSerialiser.deserialise(json,
                TupleAdaptedBinaryOperatorComposite.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);

    }

    @Override
    protected TupleAdaptedBinaryOperatorComposite<Object> getInstance() {
        return new TupleAdaptedBinaryOperatorComposite.Builder<>()
                .select(new String[] { "input", "anotherInput" })
                .execute(new Sum())
                .build();
    }

    @Override
    protected Iterable<TupleAdaptedBinaryOperatorComposite<Object>> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new TupleAdaptedBinaryOperatorComposite.Builder<>()
                        .select(new String[] { "differentInput", "anotherInput" })
                        .execute(new Sum())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite.Builder<>()
                        .select(new String[] { "input", "anotherInput" })
                        .execute(new Product())
                        .build(),
                new TupleAdaptedBinaryOperatorComposite<>(),
                new TupleAdaptedBinaryOperatorComposite.Builder<>()
                        .select(new String[] { "input", "anotherInput" })
                        .execute(new Sum())
                        .select(new String[] { "input", "differentInput" })
                        .execute(new Max())
                        .build());
    }

    @Test
    public void shouldErrorIfObjectsAreTheWrongType() {
        // Given
        final ArrayTuple stateTuple = new ArrayTuple(5, 10, 15);
        final ArrayTuple inputTuple = new ArrayTuple(1, "two", 3);

        // When
        final TupleAdaptedBinaryOperatorComposite<Integer> boc = new TupleAdaptedBinaryOperatorComposite.Builder<Integer>()
                .select(new Integer[] { 0 })
                .execute(new Product())
                .select(new Integer[] { 1 })
                .execute(new Max())
                .select(new Integer[] { 2 })
                .execute(new Sum())
                .build();

        // Then
        final ClassCastException actual = assertThrows(ClassCastException.class,
                () -> boc.apply(stateTuple, inputTuple));
        assertNotNull(actual.getMessage());
    }

    @Test
    public void shouldMergeTheInputTupleIntoTheStateTuple() {
        // Given
        final ArrayTuple stateTuple = new ArrayTuple(5, 10, 15);
        final ArrayTuple inputTuple = new ArrayTuple(1, 2, 3);

        // When
        final TupleAdaptedBinaryOperatorComposite<Integer> boc = new TupleAdaptedBinaryOperatorComposite.Builder<Integer>()
                .select(new Integer[] { 0 })
                .execute(new Product())
                .select(new Integer[] { 1 })
                .execute(new Max())
                .select(new Integer[] { 2 })
                .execute(new Sum())
                .build();

        final Tuple<Integer> agg = boc.apply(stateTuple, inputTuple);

        // Then
        assertEquals(5, agg.get(0));
        assertEquals(10, agg.get(1));
        assertEquals(18, agg.get(2));
    }
}