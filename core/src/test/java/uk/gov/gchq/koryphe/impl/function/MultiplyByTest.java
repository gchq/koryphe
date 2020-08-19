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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MultiplyByTest extends FunctionTest<MultiplyBy> {

    @Test
    public void shouldMultiplyBy2() {
        // Given
        final MultiplyBy function = new MultiplyBy(2);

        // When
        int output = function.apply(4);

        // Then
        assertEquals(8, output);
    }

    @Test
    public void shouldMultiplyBy1IfByIsNotSet() {
        // Given
        final MultiplyBy function = new MultiplyBy();

        // When
        int output = function.apply(9);

        // Then
        assertEquals(9, output);
    }

    @Test
    public void shouldReturnNullIfInputIsNull() {
        // Given
        final MultiplyBy function = new MultiplyBy(2);

        // When
        Integer output = function.apply(null);

        // Then
        assertNull(output);
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MultiplyBy function = new MultiplyBy(4);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.MultiplyBy\",%n" +
                "  \"by\" : 4%n" +
                "}"), json);

        // When 2
        final MultiplyBy deserialisedMultiplyBy = JsonSerialiser.deserialise(json, MultiplyBy.class);

        // Then 2
        assertNotNull(deserialisedMultiplyBy);
        assertEquals(4, deserialisedMultiplyBy.getBy());
    }

    @Override
    protected MultiplyBy getInstance() {
        return new MultiplyBy(3);
    }

    @Override
    protected Iterable<MultiplyBy> getDifferentInstances() {
        return Arrays.asList(
                new MultiplyBy(),
                new MultiplyBy(4)
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Integer.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Integer.class };
    }
}
