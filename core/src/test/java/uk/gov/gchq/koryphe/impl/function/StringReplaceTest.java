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
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StringReplaceTest extends FunctionTest<StringReplace> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringReplace function = new StringReplace();

        // When
        final String result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldHandleNullSearchString() {
        // Given
        final StringReplace function = new StringReplace(null, "output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertThat(result).isEqualTo("An input string.");
    }

    @Test
    public void shouldHandleNullReplacement() {
        // Given
        final StringReplace function = new StringReplace("input", null);
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertThat(result).isEqualTo("An input string.");
    }

    @Test
    public void shouldHandleNullSearchStringAndReplacement() {
        // Given
        final StringReplace function = new StringReplace(null, null);
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertThat(result).isEqualTo("An input string.");
    }

    @Test
    public void shouldReplaceInString() {
        // Given
        final StringReplace function = new StringReplace("input", "output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        //Then
        assertThat(result).isEqualTo("An output string.");
    }

    @Override
    protected StringReplace getInstance() {
        return new StringReplace("searchForThis", "replaceWithThis");
    }

    @Override
    protected Iterable<StringReplace> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new StringReplace("searchForThis", "test"),
                new StringReplace("test", "replaceWithThis")
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ String.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ String.class };
    }

    @Test
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
        assertThat(deserialisedMethod).isNotNull();
    }
}
