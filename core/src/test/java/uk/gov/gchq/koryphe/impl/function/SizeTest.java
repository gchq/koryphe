/*
 * Copyright 2017-2020 Crown Copyright
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

public class SizeTest extends FunctionTest<Size> {
    @Override
    protected Size getInstance() {
        return new Size();
    }

    @Override
    protected Iterable<Size> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Iterable.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Integer.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Size function = new Size();

        // When
        final String json = JsonSerialiser.serialise(function);
        final Size deserialised = JsonSerialiser.deserialise(json, Size.class);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.Size\"%n" +
                "}"), json);
        assertThat(deserialised).isEqualTo(function);
    }

    @Test
    public void shouldReturnSizeForGivenIterable() {
        // Given
        final Size function = new Size();
        final Iterable input = Arrays.asList(1, 2, 3, 4, 5);

        // When
        final int result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(5);
    }

    @Test
    public void shouldHandleIterableWithNullElement() {
        // Given
        final Size function = new Size();
        final Iterable<Integer> input = Arrays.asList(1, 2, null, 4, 5);

        // When
        final int result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(5);
    }

    @Test
    public void shouldHandleIterableOfAllNullElements() {
        // Given
        final Size function = new Size();
        final Iterable<Object> input = Arrays.asList(null, null, null);

        // When
        final int result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final Size function = new Size();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(null))
                .withMessage("Input cannot be null");
    }
}
