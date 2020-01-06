/*
 * Copyright 2019 Crown Copyright
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MultiplyLongByTest extends FunctionTest {
    @Test
    public void shouldMultiplyBy2() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(2L);

        // When
        long output = function.apply(4L);

        // Then
        assertEquals(8L, output);
    }

    @Test
    public void shouldMultiplyBy1IfByIsNotSet() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy();

        // When
        long output = function.apply(9L);

        // Then
        assertEquals(9L, output);
    }

    @Test
    public void shouldReturnNullIfInputIsNull() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(2L);

        // When
        Long output = function.apply(null);

        // Then
        assertNull(output);
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(4L);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.MultiplyLongBy\",%n" +
                "  \"by\" : 4%n" +
                "}"), json);

        // When 2
        final MultiplyLongBy deserialisedMultiplyLongBy = JsonSerialiser.deserialise(json, MultiplyLongBy.class);

        // Then 2
        assertNotNull(deserialisedMultiplyLongBy);
        assertEquals(4L, deserialisedMultiplyLongBy.getBy());
    }

    @Override
    protected MultiplyLongBy getInstance() {
        return new MultiplyLongBy(3L);
    }

    @Override
    protected Class<MultiplyLongBy> getFunctionClass() {
        return MultiplyLongBy.class;
    }
}