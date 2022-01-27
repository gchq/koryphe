/*
 * Copyright 2018-2021 Crown Copyright
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

import static org.junit.jupiter.api.Assertions.*;

public class ToDoubleTest extends FunctionTest<ToDouble> {

    @Test
    public void shouldThrowException() {
        // Given
        final ToDouble function = new ToDouble();

        // When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            function.apply(true);
        });

        String expectedMessage = "Could not convert value to Double: ";
        String actualMessage = exception.getMessage();

        // Then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void shouldConvertStringToDouble() {
        // Given
        final ToDouble function = new ToDouble();

        // When
        Object output = function.apply("5.2");

        // Then
        assertEquals(5.2, output);
        assertEquals(Double.class, output.getClass());
    }

    @Test
    public void shouldConvertNumberToDouble() {
        // Given
        final ToDouble function = new ToDouble();

        // When
        Object output = function.apply(5);

        // Then
        assertEquals(5.0, output);
        assertEquals(Double.class, output.getClass());
    }

    @Test
    public void shouldReturnNullWhenValueIsNull() {
        // Given
        final ToDouble function = new ToDouble();

        // When
        final Object output = function.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    protected ToDouble getInstance() {
        return new ToDouble();
    }

    @Override
    protected Iterable<ToDouble> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Double.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToDouble function = new ToDouble();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToDouble\"%n" +
                "}"), json);

        // When 2
        final ToDouble deserialisedMethod = JsonSerialiser.deserialise(json, ToDouble.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
