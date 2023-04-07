/*
 * Copyright 2017-2023 Crown Copyright
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
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtractValuesTest extends FunctionTest<ExtractValues> {
    @Override
    protected ExtractValues getInstance() {
        return new ExtractValues();
    }

    @Override
    protected Iterable<ExtractValues> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Map.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Iterable.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ExtractValues function = new ExtractValues();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.ExtractValues\"%n" +
                "}"), json);

        // When 2
        final ExtractValues deserialised = JsonSerialiser.deserialise(json, ExtractValues.class);

        // Then
        assertThat(deserialised).isNotNull();
    }

    @Test
    public void shouldExtractValuesFromGivenMap() {
        // Given
        final ExtractValues<String, Integer> function = new ExtractValues<>();
        final Map<String, Integer> input = new HashMap<>();
        input.put("first", 1);
        input.put("second", 2);
        input.put("third", 3);

        // When
        final Iterable<Integer> results = function.apply(input);

        // Then
        assertThat(results).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    public void shouldReturnEmptySetForEmptyMap() {
        // Given
        final ExtractValues<String, Integer> function = new ExtractValues<>();

        // When
        final Iterable<Integer> results = function.apply(new HashMap<>());

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final ExtractValues<String, String> function = new ExtractValues<>();
        final Map<String, String> input = null;

        // When
        final Iterable result = function.apply(input);

        // Then
        assertThat(result).isNull();
    }
}
