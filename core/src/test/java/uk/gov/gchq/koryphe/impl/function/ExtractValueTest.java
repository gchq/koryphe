/*
 * Copyright 2018-2022 Crown Copyright
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ExtractValueTest extends FunctionTest<ExtractValue> {
    @Override
    protected ExtractValue<String, Integer> getInstance() {
        return new ExtractValue<>("testKey");
    }

    @Override
    protected Iterable<ExtractValue> getDifferentInstancesOrNull() {
        return Collections.singletonList(new ExtractValue("differentKey"));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Map.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ExtractValue<String, Integer> function = new ExtractValue<>("test");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.ExtractValue\",%n" +
                "   \"key\" : \"test\"%n" +
                "}"), json);

        // When 2
        final ExtractValue deserialised = JsonSerialiser.deserialise(json, ExtractValue.class);

        // Then 2
        assertNotNull(deserialised);
        assertEquals("test", deserialised.getKey());
    }

    @Test
    public void shouldExtractCorrectValueFromMap() {
        // Given
        final ExtractValue<String, Integer> function = new ExtractValue<>("secondKey");
        final Map<String, Integer> input = new HashMap<>();
        input.put("firstKey", 1);
        input.put("secondKey", 3);
        input.put("thirdKey", 6);
        input.put("second", 12);

        // When
        final Integer result = function.apply(input);

        // Then
        assertEquals(3, result);
    }

    @Test
    public void shouldThrowExceptionForEmptyInput() {
        // Given
        final ExtractValue<String, Integer> function = new ExtractValue<>();

        // When
        final Integer result = function.apply(null);

        // Then
        assertNull(result);
    }
}
