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
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SizeTest extends FunctionTest
{
    @Override
    protected Function getInstance() {
        return new Size();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return Size.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Size function = new Size();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.Size\"%n" +
                "}"), json);
    }

    @Test
    public void shouldReturnSizeForGivenIterable() {
        // Given
        final Size function = new Size();
        final Iterable input = Arrays.asList(1, 2, 3, 4, 5);

        // When
        final Integer result = function.apply(input);

        // Then
        assertEquals(new Integer(5), result);
    }

    @Test
    public void shouldHandleIterableWithNullElement() {
        // Given
        final Size function = new Size();
        final Iterable<Integer> input = Arrays.asList(1, 2, null, 4, 5);

        // When
        final Integer result = function.apply(input);

        // Then
        assertEquals(new Integer(5), result);
    }

    @Test
    public void shouldHandleIterableOfAllNullElements() {
        // Given
        final Size function = new Size();
        final Iterable<Object> input = Arrays.asList(null, null, null);

        // When
        final Integer result = function.apply(input);

        // Then
        assertEquals(new Integer(3), result);
    }
    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final Size function = new Size();
        final Iterable input = null;

        // When / Then
        try {
            function.apply(input);
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Input cannot be null"));
        }
    }
}
