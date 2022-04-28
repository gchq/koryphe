/*
 * Copyright 2020 Crown Copyright
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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.binaryoperator.Sum;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class IterableFlattenTest extends FunctionTest {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final IterableFlatten<?> function = new IterableFlatten<>();

        // When
        final Object result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldFlattenIterableNumbers() {
        // Given
        final IterableFlatten<Number> function = new IterableFlatten<>(new Sum());
        final List<Number> input = Lists.newArrayList(1, 2, 3, 4, 5);

        // When
        final Number result = function.apply(input);

        // Then
        assertEquals(15, result);
    }

    @Test
    public void shouldFlattenIterableNumbersNull() {
        // Given
        final IterableFlatten<Number> function = new IterableFlatten<>(new Sum());
        final List<Number> input = Lists.newArrayList((Number) null, (Number) null, (Number) null, (Number) null);

        // When
        final Number result = function.apply(input);

        //then
        assertNull(result);
    }

    @Test
    public void shouldFlattenIterableStrings() {
        // Given
        final IterableFlatten<String> function = new IterableFlatten<>((a, b) -> a + b);
        final Set<String> input = Sets.newHashSet("a", "b", "c");

        // When
        final String result = function.apply(input);

        // Then
        assertEquals("abc", result);
    }

    @Override
    protected IterableFlatten getInstance() {
        return new IterableFlatten();
    }

    @Override
    protected Class<? extends IterableFlatten> getFunctionClass() {
        return IterableFlatten.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{Object.class};
    }

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
        assertNotNull(deserialised);
    }
}
