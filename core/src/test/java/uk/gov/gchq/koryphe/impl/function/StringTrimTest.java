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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StringTrimTest extends FunctionTest {
    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringTrim function = new StringTrim();

        // When
        final String result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldTrimInputString() {
        // Given
        final StringTrim function = new StringTrim();
        final String input = "   Input String   ";

        // When
        final String result = function.apply(input);

        // Then
        assertEquals("Input String", result);
    }

    @Override
    protected StringTrim getInstance() {
        return new StringTrim();
    }

    @Override
    protected Class<? extends StringTrim> getFunctionClass() {
        return StringTrim.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringTrim function = new StringTrim();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringTrim\"%n" +
                "}"), json);

        // When 2
        final StringTrim deserialisedMethod = JsonSerialiser.deserialise(json, StringTrim.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}