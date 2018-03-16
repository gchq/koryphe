/*
 * Copyright 2017-2018 Crown Copyright
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NthItemTest extends FunctionTest {

    @Override
    protected Function getInstance() {
        return new NthItem();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return NthItem.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final NthItem function = new NthItem(2);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.NthItem\",%n" +
                "  \"selection\" : 2%n" +
                "}"), json);

        // When 2
        final NthItem deserialised = JsonSerialiser.deserialise(json, NthItem.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals(2, deserialised.getSelection());
    }

    @Test
    public void shouldReturnCorrectValueWithInteger() {
        // Given
        final NthItem<Integer> function = new NthItem<>(2);

        // When
        final Integer result = function.apply(Arrays.asList(1, 2, 3, 4, 5));

        // Then
        assertNotNull(result);
        assertEquals(new Integer(3), result);
    }

    @Test
    public void shouldReturnCorrectValueWithString() {
        // Given
        final NthItem<String> function = new NthItem<>(1);

        // When
        final String result = function.apply(Arrays.asList("these", "are", "test", "strings"));

        // Then
        assertNotNull(result);
        assertEquals("are", result);
    }

    @Test
    public void shouldReturnNullForNullElement() {
        // Given
        final NthItem<Integer> function = new NthItem<>(1);

        // When
        final Integer result = function.apply(Arrays.asList(1, null, 3));

        // Then
        assertNull(result);
    }

    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final NthItem<Integer> function = new NthItem<>();

        // When / Then
        try {
            function.apply(null);
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Input cannot be null"));
        }
    }
}
