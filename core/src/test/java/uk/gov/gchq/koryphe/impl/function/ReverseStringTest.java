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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ReverseStringTest extends FunctionTest<ReverseString> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final ReverseString function = new ReverseString();

        // When
        final String result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldReverseString() {
        // Given
        final ReverseString function = new ReverseString();
        final String input = "12345";

        // When
        final String result = function.apply(input);

        // Then
        assertEquals("54321", result);
    }

    @Override
    protected ReverseString getInstance() {
        return new ReverseString();
    }

    @Override
    protected Iterable<ReverseString> getDifferentInstancesOrNull() {
        return null;
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        ReverseString instance = getInstance();
        String json = "" +
                "{" +
                    "\"class\": \"uk.gov.gchq.koryphe.impl.function.ReverseString\"" +
                "}";

        // When
        String serialised = JsonSerialiser.serialise(instance);
        ReverseString deserialised = JsonSerialiser.deserialise(json, ReverseString.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertEquals(instance, deserialised);

    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ String.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ String.class };
    }
}