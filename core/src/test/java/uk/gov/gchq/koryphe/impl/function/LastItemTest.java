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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LastItemTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new LastItem();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return LastItem.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final LastItem function = new LastItem();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.LastItem\"%n" +
                "}"), json);

        // When 2
        final LastItem deserialised = JsonSerialiser.deserialise(json, LastItem.class);

        // When
        assertNotNull(deserialised);
    }

    @Test
    public void shouldReturnCorrectValueWithInteger() {
        // Given
        final LastItem<Integer> function = new LastItem<>();

        // When
        final Integer result = function.apply(Arrays.asList(2, 3, 5, 7, 11));

        // Then
        assertNotNull(result);
        assertEquals(new Integer(11), result);
    }

    @Test
    public void shouldReturnCorrectValueWithString() {
        // Given
        final LastItem<String> function = new LastItem<>();

        // When
        final String result = function.apply(Arrays.asList("these", "are", "test", "strings"));

        // Then
        assertNotNull(result);
        assertEquals("strings", result);
    }
}
