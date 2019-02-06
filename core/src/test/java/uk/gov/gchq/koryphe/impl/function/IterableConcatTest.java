/*
 * Copyright 2017-2019 Crown Copyright
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
package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IterableConcatTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new IterableConcat();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return IterableConcat.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableConcat function = new IterableConcat();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IterableConcat\"%n" +
                "}"), json);

        // When 2
        final IterableConcat deserialised = JsonSerialiser.deserialise(json, IterableConcat.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldFlattenNestedIterables() {
        // Given
        final IterableConcat<Integer> function = new IterableConcat<>();

        // When
        final Iterable<Integer> result = function.apply(Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6)));

        // Then
        assertNotNull(result);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), Lists.newArrayList(result));
    }

    @Test
    public void shouldHandleNullInputIterable() {
        // Given
        final IterableConcat<Integer> function = new IterableConcat<>();
        final Iterable<Iterable<Integer>> input = null;

        // When / Then
        try {
            function.apply(input);
            fail("Exception expected");
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("iterables are required"));
        }
    }

    @Test
    public void shouldReturnEmptyIterableForEmptyInput() {
        // Given
        final IterableConcat<Integer> function = new IterableConcat<>();
        final Iterable<Iterable<Integer>> input = new ArrayList<>();

        // When
        final Iterable<Integer> results = function.apply(input);

        // Then
        assertTrue(Iterables.isEmpty(results));
    }

    @Test
    public void shouldHandleNullElementsOfInnerIterable() {
        // Given
        final IterableConcat<Integer> function = new IterableConcat<>();
        final Iterable<Iterable<Integer>> input = Arrays.asList(
                Arrays.asList(1, 2, null, 4),
                Arrays.asList(5, null, 7));

        // When
        final Iterable<Integer> results = function.apply(input);

        // Then
        assertEquals(
                Arrays.asList(1, 2, null, 4, 5, null, 7),
                Lists.newArrayList(results));
    }
}
