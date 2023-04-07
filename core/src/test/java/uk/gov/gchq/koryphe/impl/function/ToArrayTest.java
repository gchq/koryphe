/*
 * Copyright 2018-2020 Crown Copyright
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
import java.util.LinkedHashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class ToArrayTest extends FunctionTest<ToArray> {

    @Test
    public void shouldConvertNullToArray() {
        // Given
        final ToArray function = new ToArray();

        // When
        final Object[] result = function.apply(null);

        // Then
        assertThat(result).isEqualTo(new Object[] {null});
    }

    @Test
    public void shouldConvertStringToArray() {
        // Given
        final ToArray function = new ToArray();
        final Object value = "value1";

        // When
        final Object[] result = function.apply(value);

        // Then
        assertThat(result).isEqualTo(new Object[] {value});
    }

    @Test
    public void shouldConvertListToArray() {
        // Given
        final ToArray function = new ToArray();
        final Object value = Arrays.asList("value1", "value2");

        // When
        final Object[] result = function.apply(value);

        // Then
        assertThat(result).isEqualTo(new Object[] {"value1", "value2"});
    }

    @Test
    public void shouldConvertSetToArray() {
        // Given
        final ToArray function = new ToArray();
        final Object value = new LinkedHashSet<>(Arrays.asList("value1", "value2"));

        // When
        final Object[] result = function.apply(value);

        // Then
        assertThat(result).isEqualTo(new Object[] {"value1", "value2"});
    }

    @Test
    public void shouldReturnAGivenArray() {
        // Given
        final ToArray function = new ToArray();
        final Object value = new Object[] {"value1"};

        // When
        final Object[] result = function.apply(value);

        // Then
        assertThat(result).isSameAs(value);
    }

    @Override
    protected ToArray getInstance() {
        return new ToArray();
    }

    @Override
    protected Iterable<ToArray> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object[].class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToArray function = new ToArray();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToArray\"%n" +
                "}"), json);

        // When 2
        final ToArray deserialisedMethod = JsonSerialiser.deserialise(json, ToArray.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
