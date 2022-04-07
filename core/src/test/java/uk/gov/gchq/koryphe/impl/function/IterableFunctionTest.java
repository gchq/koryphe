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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class IterableFunctionTest extends FunctionTest<IterableFunction> {
    @Override
    protected IterableFunction getInstance() {
        return new IterableFunction();
    }

    @Override
    protected Iterable<IterableFunction> getDifferentInstancesOrNull() {
        return Collections.singletonList(new IterableFunction(new ToLong()));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Iterable.class};
    }

    @Test
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
        assertThat(deserialised).isNotNull();
    }

    @Test
    public void shouldConvertIterableOfIntegers() {
        // Given
        final IterableFunction<Integer, String> function = new IterableFunction<>(new ToString());

        // When
        final Iterable<String> result = function.apply(Arrays.asList(1, 2, 3, 4));

        // Then
        assertThat(result)
                .isNotNull()
                .containsExactly("1", "2", "3", "4");
    }

    @Test
    public void shouldReturnEmptyIterableForEmptyInput() {
        // Given
        final IterableFunction<Iterable<Integer>, Integer> function = new IterableFunction<>(new FirstItem<>());

        // When
        final Iterable<Integer> result = function.apply(new ArrayList<>());

        // Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
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
        assertThat(function.getFunctions()).containsExactly(func, func1, func2);
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
        assertThat(result).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void shouldReturnNullForNullInputIterable() {
        // Given
        final IterableFunction function = new IterableFunction();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldNotModifyInputForEmptyListOfFunctions() {
        // Given
        final IterableFunction<Integer, Integer> function = new IterableFunction<>(new ArrayList<>());

        // When
        final Iterable<Integer> result = function.apply(Arrays.asList(1, 2, 3));

        // Then
        assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    public void shouldThrowErrorForNullListOfFunctions() {
        // Given
        final List<Function> functions = null;
        final IterableFunction<Integer, Integer> function = new IterableFunction<>(functions);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(Arrays.asList(1, 2, 3)))
                .withMessage("List of functions cannot be null");
    }

    @Test
    public void shouldThrowErrorForListOfFunctionsWithNullFunction() {
        // Given
        final List<Function> functions = new ArrayList<>();
        functions.add(new ToString());
        functions.add(null);

        final IterableFunction<Integer, Integer> function = new IterableFunction<>(functions);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(Arrays.asList(1, 2, 3)))
                .withMessage("Functions list cannot contain a null function");
    }
}
