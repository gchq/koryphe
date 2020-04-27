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
package uk.gov.gchq.koryphe.impl.function;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.iterable.CloseableIterable;
import uk.gov.gchq.koryphe.util.IterableUtil;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class IterableFunctionTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new IterableFunction();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return IterableFunction.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Iterable.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Iterable.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableFunction function = new IterableFunction.Builder<Iterable<Iterable<Integer>>>()
                .first(new FirstItem<>())
                .then(new NthItem<>(1))
                .build();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IterableFunction\",%n" +
                "   \"functions\" : [{%n" +
                "      \"class\" : \"uk.gov.gchq.koryphe.impl.function.FirstItem\"%n" +
                "   }, {%n" +
                "       \"class\" : \"uk.gov.gchq.koryphe.impl.function.NthItem\",%n" +
                "       \"selection\" : 1%n" +
                "   }]%n" +
                "}"), json);

        // When 2
        final IterableFunction deserialised = JsonSerialiser.deserialise(json, IterableFunction.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldConvertIterableOfIntegers() {
        // Given
        final IterableFunction<Integer, String> function = new IterableFunction<>(new ToString());

        // When
        final Iterable<String> result = function.apply(Arrays.asList(1, 2, 3, 4));

        // Then
        assertNotNull(result);
        assertEquals(Arrays.asList("1", "2", "3", "4"), Lists.newArrayList(result));
    }

    @Test
    public void shouldReturnEmptyIterableForEmptyInput() {
        // Given
        final IterableFunction<Iterable<Integer>, Integer> function = new IterableFunction<>(new FirstItem<>());

        // When
        final Iterable<Integer> result = function.apply(new ArrayList<>());

        // Then
        assertNotNull(result);
        assertTrue(Iterables.isEmpty(result));
    }

    @Test
    public void shouldPopulateFunctionFromBuilder() {
        // Given
        final Function<Integer, String> func = Object::toString;
        final Function<String, Integer> func1 = Integer::valueOf;
        final Function<Integer, String> func2 = Object::toString;
        final IterableFunction<Integer, String> function = new IterableFunction.Builder<Integer>()
                .first(func)
                .then(func1)
                .then(func2)
                .build();

        // Then
        assertEquals(3, function.getFunctions().size());
        assertEquals(Arrays.asList(func, func1, func2), function.getFunctions());
    }

    @Test
    public void shouldApplyMultipleFunctions() {
        // Given
        final IterableFunction<Integer, Integer> function = new IterableFunction.Builder<Integer>()
                .first(Object::toString)
                .then(Integer::valueOf)
                .build();

        // When
        final Iterable<Integer> result = function.apply(Arrays.asList(1, 2, 3, 4));

        // Then
        assertEquals(4, Iterables.size(result));
        assertEquals(Arrays.asList(1, 2, 3, 4), Lists.newArrayList(result));
    }

    @Test
    public void shouldReturnNullForNullInputIterable() {
        // Given
        final IterableFunction function = new IterableFunction();

        // When
        final Object result = function.apply(null);

        assertNull(result);
    }

    @Test
    public void shouldNotModifyInputForEmptyListOfFunctions() {
        // Given
        final IterableFunction<Integer, Integer> function = new IterableFunction<>(new ArrayList<>());

        // When
        final Iterable<Integer> result = function.apply(Arrays.asList(1, 2, 3));

        // Then
        assertEquals(Arrays.asList(1, 2, 3), Lists.newArrayList(result));
    }

    @Test
    public void shouldThrowErrorForNullListOfFunctions() {
        // Given
        final List<Function> functions = null;
        final IterableFunction<Integer, Integer> function = new IterableFunction<>(functions);

        // When / Then
        try {
            function.apply(Arrays.asList(1, 2, 3));
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("List of functions cannot be null"));
        }
    }

    @Test
    public void shouldThrowErrorForListOfFunctionsWithNullFunction() {
        // Given
        final List<Function> functions = new ArrayList<>();
        functions.add(new ToString());
        functions.add(null);

        final IterableFunction<Integer, Integer> function = new IterableFunction<>(functions);

        // When / Then
        try {
            function.apply(Arrays.asList(1, 2, 3));
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Functions list cannot contain a null function"));
        }
    }
}
