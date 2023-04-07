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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.StringConcat;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class IterableFlattenTest extends FunctionTest<IterableFlatten> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final IterableFlatten<?> function = new IterableFlatten<>();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldFlattenIterableNumbers() {
        // Given
        final IterableFlatten<Number> function = new IterableFlatten<>(new Sum());
        final List<Number> input = Arrays.asList(1, 2, 3, 4, 5);

        // When
        final Number result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(15);
    }

    @Test
    public void shouldFlattenIterableNumbersWithNull() {
        // Given
        final IterableFlatten<Number> function = new IterableFlatten<>(new Sum());
        final List<Number> input = Arrays.asList(2, 4, 6, 8, null, 10);

        // When
        final Number result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(30);
    }
    @Test
    public void shouldFlattenIterableNumbersAllNull() {
        // Given
        final IterableFlatten<Number> function = new IterableFlatten<>(new Sum());
        final List<Number> input = Arrays.asList(null, null, null, null);

        // When
        final Number result = function.apply(input);

        //then
        assertNull(result);
    }

    @Test
    public void shouldFlattenIterableStrings() {
        // Given
        final IterableFlatten<String> function = new IterableFlatten<>((a, b) -> a + b);
        final Set<String> input = new HashSet<>(Arrays.asList("a", "b", "c"));

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("abc");
    }

    @Override
    protected IterableFlatten getInstance() {
        return new IterableFlatten();
    }

    @Override
    protected Iterable<IterableFlatten> getDifferentInstancesOrNull() {
        return Collections.singletonList(new IterableFlatten(new StringConcat()));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableFlatten function = new IterableFlatten(new Sum());

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.function.IterableFlatten\"," +
                "\"operator\":{\"class\":\"uk.gov.gchq.koryphe.impl.binaryoperator.Sum\"}" +
                "}", json);

        // When 2
        final IterableFlatten deserialised = JsonSerialiser.deserialise(json, IterableFlatten.class);

        // Then 2
        assertThat(deserialised).isNotNull();
    }
}
