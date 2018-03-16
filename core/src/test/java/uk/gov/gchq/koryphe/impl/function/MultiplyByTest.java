/*
 * Copyright 2017-2018 Crown Copyright
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

public class MultiplyByTest extends FunctionTest {
    @Test
    public void shouldMultiplyBy2() {
        // Given
        final MultiplyBy function = new MultiplyBy(2);

        // When
        int output = function.apply(4);

        assertEquals(8, output);
    }

    @Test
    public void shouldMultiplyBy1IfByIsNotSet() {
        // Given
        final MultiplyBy function = new MultiplyBy();

        // When
        int output = function.apply(9);

        assertEquals(9, output);
    }

    @Test
    public void shouldReturnNullIfInputIsNull() {
        // Given
        final MultiplyBy function = new MultiplyBy(2);

        // When
        Integer output = function.apply(null);

        assertEquals(null, output);
    }

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
    protected Class<MultiplyBy> getFunctionClass() {
        return MultiplyBy.class;
    }
}
