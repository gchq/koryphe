/*
 * Copyright 2022 Crown Copyright
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ToFloatTest extends FunctionTest<ToFloat> {

    @Test
    public void shouldThrowException() {
        // Given
        final ToFloat function = new ToFloat();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(true))
                .withMessageContaining("Could not convert value to Float: ");
    }

    @Test
    public void shouldConvertStringToFloat() {
        // Given
        final ToFloat function = new ToFloat();

        // When
        Object output = function.apply("5.2");

        // Then
        assertThat(output)
                .isEqualTo(5.2f)
                .isExactlyInstanceOf(Float.class);
    }

    @Test
    public void shouldConvertNumberToFloat() {
        // Given
        final ToFloat function = new ToFloat();

        // When
        Object output = function.apply(5);

        // Then
        assertThat(output)
                .isEqualTo(5.0f)
                .isExactlyInstanceOf(Float.class);
    }

    @Test
    public void shouldReturnNullWhenValueIsNull() {
        // Given
        final ToFloat function = new ToFloat();

        // When
        final Object output = function.apply(null);

        // Then
        assertThat(output).isNull();
    }

    @Override
    protected ToFloat getInstance() {
        return new ToFloat();
    }

    @Override
    protected Iterable<ToFloat> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Float.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToFloat function = new ToFloat();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToFloat\"%n" +
                "}"), json);

        // When 2
        final ToFloat deserialisedMethod = JsonSerialiser.deserialise(json, ToFloat.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
