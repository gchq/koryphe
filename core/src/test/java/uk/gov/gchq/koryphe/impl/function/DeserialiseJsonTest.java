/*
 * Copyright 2019 Crown Copyright
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DeserialiseJsonTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new DeserialiseJson();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return DeserialiseJson.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final DeserialiseJson function = new DeserialiseJson().outputClass(Map.class);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.DeserialiseJson\",%n" +
                "   \"outputClass\" : \"java.util.Map\"" +
                "}"), json);
    }

    @Test
    public void shouldParseJson() {
        // Given
        final DeserialiseJson function = new DeserialiseJson();
        final String input = "{\"elements\": [{\"value\": \"value1\"}, {\"value\": \"value2\"}]}";

        // When
        Object result = function.apply(input);

        // Then
        Map<String, Object> element2aMap = new HashMap<>();
        element2aMap.put("value", "value1");
        Map<String, Object> element2bMap = new HashMap<>();
        element2bMap.put("value", "value2");
        HashMap<Object, Object> rootMap = new HashMap<>();
        rootMap.put("elements", Arrays.asList(element2aMap, element2bMap));
        assertEquals(rootMap, result);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final DeserialiseJson function = new DeserialiseJson();
        final String input = null;

        // When
        Object result = function.apply(input);

        // Then
        assertNull(result);
    }
}
