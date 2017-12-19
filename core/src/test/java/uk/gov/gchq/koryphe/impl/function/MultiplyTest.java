/*
 * Copyright 2017 Crown Copyright
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

public class MultiplyTest extends FunctionTest {
    @Test
    public void shouldMultiply2() {
        // Given
        final Multiply function = new Multiply();

        // When
        int output = function.apply(4, 2);

        assertEquals(8, output);
    }

    @Test
    public void shouldMultiply1IfSecondIsNull() {
        // Given
        final Multiply function = new Multiply();

        // When
        int output = function.apply(9, null);

        assertEquals(9, output);
    }

    @Test
    public void shouldMultiply1IfFirstIsNull() {
        // Given
        final Multiply function = new Multiply();

        // When
        Integer output = function.apply(null, 9);

        assertEquals(null, output);
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Multiply function = new Multiply();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"Multiply\"" +
                "}"), json);

        // When 2
        final Multiply deserialisedMultiply = JsonSerialiser.deserialise(json, Multiply.class);

        // Then 2
        assertNotNull(deserialisedMultiply);
    }

    @Override
    protected Multiply getInstance() {
        return new Multiply();
    }

    @Override
    protected Class<Multiply> getFunctionClass() {
        return Multiply.class;
    }
}