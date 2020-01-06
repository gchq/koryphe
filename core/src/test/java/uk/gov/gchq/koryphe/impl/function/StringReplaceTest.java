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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StringReplaceTest extends FunctionTest {
    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringReplace function = new StringReplace();

        // When
        final String result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldHandleNullSearchString() {
        // Given
        final StringReplace function = new StringReplace(null, "output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertEquals("An input string.", result);
    }

    @Test
    public void shouldHandleNullReplacement() {
        // Given
        final StringReplace function = new StringReplace("input", null);
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertEquals("An input string.", result);
    }

    @Test
    public void shouldHandleNullSearchStringAndReplacement() {
        // Given
        final StringReplace function = new StringReplace(null, null);
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertEquals("An input string.", result);
    }

    @Test
    public void shouldReplaceInString() {
        // Given
        final StringReplace function = new StringReplace("input", "output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertEquals("An output string.", result);
    }

    @Override
    protected StringReplace getInstance() {
        return new StringReplace();
    }

    @Override
    protected Class<? extends StringReplace> getFunctionClass() {
        return StringReplace.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringReplace function = new StringReplace("find", "replace");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringReplace\",%n" +
                "  \"searchString\" : \"find\",%n" +
                "  \"replacement\" : \"replace\"%n" +
                "}"), json);

        // When 2
        final StringReplace deserialisedMethod = JsonSerialiser.deserialise(json, StringReplace.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}