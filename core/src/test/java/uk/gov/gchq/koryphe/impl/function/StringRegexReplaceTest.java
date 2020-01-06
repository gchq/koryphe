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

public class StringRegexReplaceTest extends FunctionTest {
    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringRegexReplace function = new StringRegexReplace();

        // When
        final String result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldReplaceStringSimple() {
        // Given
        final StringRegexReplace function = new StringRegexReplace("input","output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        // Then
        assertEquals("An output string.", result);
    }

    @Test
    public void shouldReplaceStringRegex() {
        // Given
        final StringRegexReplace function = new StringRegexReplace("t\\s+s"," ");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        // Then
        assertEquals("An inpu tring.", result);
    }

    @Override
    protected StringRegexReplace getInstance() {
        return new StringRegexReplace();
    }

    @Override
    protected Class<? extends StringRegexReplace> getFunctionClass() {
        return StringRegexReplace.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringRegexReplace function = new StringRegexReplace("{abc}","replace");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringRegexReplace\",%n" +
                "  \"regex\" : \"{abc}\",%n" +
                "  \"replacement\" : \"replace\"%n" +
                "}"), json);

        // When 2
        final StringRegexReplace deserialisedMethod = JsonSerialiser.deserialise(json, StringRegexReplace.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}