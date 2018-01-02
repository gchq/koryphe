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
import com.google.common.collect.Sets;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExtractKeysTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new ExtractKeys();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ExtractKeys.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ExtractKeys function = new ExtractKeys();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"ExtractKeys\"%n" +
                "}"), json);

        // When 2
        final ExtractKeys deserialised = JsonSerialiser.deserialise(json, ExtractKeys.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldExtractKeysFromGivenMap() {
        // Given
        final ExtractKeys<String, Integer> function = new ExtractKeys<>();
        final Map<String, Integer> input = new HashMap<>();
        input.put("first", 1);
        input.put("second", 2);
        input.put("third", 3);

        // When
        final Iterable<String> results = function.apply(input);

        // Then
        assertEquals(Sets.newHashSet("first", "second", "third"), results);
    }

    @Test
    public void shouldReturnEmptySetForEmptyMap() {
        // Given
        final ExtractKeys<String, String> function = new ExtractKeys<>();

        // When
        final Iterable<String> results = function.apply(new HashMap<>());

        // Then
        assertTrue(Iterables.isEmpty(results));
    }

    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final ExtractKeys<String, String> function = new ExtractKeys<>();
        final Map<String, String> input = new HashMap<>();

        // When / Then
        try {
            function.apply(input);
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Input cannot be null"));
        }
    }

}
