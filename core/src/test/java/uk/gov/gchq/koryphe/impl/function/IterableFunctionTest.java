/*
 * Copyright 2017 Crown Copyright
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
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableFunction<Iterable<Integer>, Integer> function = new IterableFunction<>(new FirstItem<>());

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IterableFunction\",%n" +
                "   \"function\" : {%n" +
                "      \"class\" : \"uk.gov.gchq.koryphe.impl.function.FirstItem\"%n" +
                "   }%n" +
                "}"), json);

        // When 2
        final IterableFunction deserialised = JsonSerialiser.deserialise(json, IterableFunction.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldConvertIterableOfIntegers() {
        // Given
        final IterableFunction<Integer, String> function = new IterableFunction<>(Object::toString);

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
}
