/*
 * Copyright 2017-2022 Crown Copyright
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LastItemTest extends FunctionTest<LastItem> {
    @Override
    protected LastItem getInstance() {
        return new LastItem();
    }

    @Override
    protected Iterable<LastItem> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final LastItem function = new LastItem();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.LastItem\"%n" +
                "}"), json);

        // When 2
        final LastItem deserialised = JsonSerialiser.deserialise(json, LastItem.class);

        // When
        assertThat(deserialised).isNotNull();
    }

    @Test
    public void shouldReturnCorrectValueWithInteger() {
        // Given
        final LastItem<Integer> function = new LastItem<>();

        // When
        final Integer result = function.apply(Arrays.asList(2, 3, 5, 7, 11));

        // Then
        assertThat(result).isEqualTo(Integer.valueOf(11));
    }

    @Test
    public void shouldReturnCorrectValueWithString() {
        // Given
        final LastItem<String> function = new LastItem<>();

        // When
        final String result = function.apply(Arrays.asList("these", "are", "test", "strings"));

        // Then
        assertThat(result).isEqualTo("strings");
    }

    @Test
    public void shouldReturnNullForNullElement() {
        // Given
        final LastItem<Integer> function = new LastItem<>();

        // When
        final Integer result = function.apply(Arrays.asList(1, 2, null));

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final LastItem<Integer> function = new LastItem<>();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(null))
                .withMessage("Input cannot be null");
    }
}
