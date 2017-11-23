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

import com.google.common.collect.Sets;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class IsEmptyTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new IsEmpty();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return IsEmpty.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsEmpty function = new IsEmpty();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.IsEmpty\"%n" +
                "}"), json);

        // When 2
        final IsEmpty deserialised = JsonSerialiser.deserialise(json, IsEmpty.class);

        // Then 2
        assertNotNull(deserialised);
    }

    @Test
    public void shouldReturnTrueForEmptyIterable() {
        // Given
        final IsEmpty function = new IsEmpty();
        final Iterable input = Sets.newHashSet();

        // When
        final Boolean result = function.apply(input);

        // Then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseForPopulatedIterable() {
        // Given
        final IsEmpty function = new IsEmpty();
        final Iterable input = Arrays.asList(3, 1, 4, 1, 6);

        // When
        final Boolean result = function.apply(input);

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldReturnFalseForNullElements() {
        // Given
        final IsEmpty function = new IsEmpty();
        final Iterable input = Arrays.asList(null, null);

        // When
        final Boolean result = function.apply(input);

        // Then
        assertFalse(result);
    }

    @Test
    public void shouldThrowExceptionForNullInput() {
        // Given
        final IsEmpty function = new IsEmpty();
        final Iterable input = null;

        // When / Then
        try {
            function.apply(input);
        } catch (final IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Input cannot be null"));
        }
    }
}
