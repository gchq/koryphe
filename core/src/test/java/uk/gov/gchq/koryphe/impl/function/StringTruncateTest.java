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

public class StringTruncateTest extends FunctionTest<StringTruncate> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringTruncate function = new StringTruncate();

        // When
        final String result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldTruncateString() {
        // Given
        final StringTruncate function = new StringTruncate(10);
        final String input = "A long input string";

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("A long inp");
    }

    @Test
    public void shouldTruncateStringWithEllipses() {
        // Given
        final StringTruncate function = new StringTruncate(10, true);
        final String input = "A long input string";

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("A long inp...");
    }

    @Test
    public void shouldReturnFullStringIfInputShorterThanMaxLength() {
        //Given
        final StringTruncate function = new StringTruncate(10);
        final String input = "123 56";

        //When
        final String result = function.apply(input);

        //Then
        assertThat(result).isEqualTo(input);

    }

    @Override
    protected StringTruncate getInstance() {
        return new StringTruncate(10, true);
    }

    @Override
    protected Iterable<StringTruncate> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new StringTruncate(),
                new StringTruncate(5, true),
                new StringTruncate(10, false)
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
        final StringTruncate function = new StringTruncate();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringTruncate\",%n" +
                "  \"length\" : 0,%n" +
                "  \"ellipses\" : false%n" +
                "}"), json);

        // When 2
        final StringTruncate deserialisedMethod = JsonSerialiser.deserialise(json, StringTruncate.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
