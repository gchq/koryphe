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

public class StringRegexReplaceTest extends FunctionTest<StringRegexReplace> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringRegexReplace function = new StringRegexReplace();

        // When
        final String result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldReplaceStringSimple() {
        // Given
        final StringRegexReplace function = new StringRegexReplace("input","output");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("An output string.");
    }

    @Test
    public void shouldReplaceStringRegex() {
        // Given
        final StringRegexReplace function = new StringRegexReplace("t\\s+s"," ");
        final String input = "An input string.";

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("An inpu tring.");
    }

    @Override
    protected StringRegexReplace getInstance() {
        return new StringRegexReplace("replaceMe", "withThis");
    }

    @Override
    protected Iterable<StringRegexReplace> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new StringRegexReplace("replaceMe", "withSomethingElse"),
                new StringRegexReplace("r.*Me", "withThis"),
                new StringRegexReplace()
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
        assertThat(deserialisedMethod).isNotNull();
    }
}
