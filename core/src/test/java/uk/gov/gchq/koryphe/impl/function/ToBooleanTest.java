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

public class ToBooleanTest extends FunctionTest<ToBoolean> {

    @Test
    public void shouldThrowExceptionIfIncorrectTypeGiven() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(5.2))
                .withMessageContaining("Could not convert value to Boolean: ");
    }

    @Test
    public void shouldConvertNonBooleanStringToFalseBoolean() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        Object output = function.apply("test");

        // Then
        assertThat(output)
                .isEqualTo(false)
                .isExactlyInstanceOf(Boolean.class);
    }

    @Test
    public void shouldConvertStringTrueToBoolean() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        Object output = function.apply("true");

        // Then
        assertThat(output)
                .isEqualTo(true)
                .isExactlyInstanceOf(Boolean.class);
    }

    @Test
    public void shouldConvertStringFalseToBoolean() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        Object output = function.apply("false");

        // Then
        assertThat(output)
                .isEqualTo(false)
                .isExactlyInstanceOf(Boolean.class);
    }

    @Test
    public void shouldReturnAGivenBoolean() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        Object output = function.apply(Boolean.TRUE);

        // Then
        assertThat(output)
                .isEqualTo(Boolean.TRUE)
                .isExactlyInstanceOf(Boolean.class);
    }

    @Test
    public void shouldReturnNullWhenValueIsNull() {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        final Object output = function.apply(null);

        // Then
        assertThat(output).isNull();
    }

    @Override
    protected ToBoolean getInstance() {
        return new ToBoolean();
    }

    @Override
    protected Iterable<ToBoolean> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Boolean.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToBoolean function = new ToBoolean();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToBoolean\"%n" +
                "}"), json);

        // When 2
        final ToBoolean deserialisedMethod = JsonSerialiser.deserialise(json, ToBoolean.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
