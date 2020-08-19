/*
 * Copyright 2020 Crown Copyright
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StringAppendTest extends FunctionTest<StringAppend> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringAppend function = new StringAppend("!");

        // When
        final String result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
    public void shouldHandleNullSuffix() {
        // Given
        final StringAppend function = new StringAppend(null);

        // When
        final String result = function.apply("Hello");

        // Then
        assertEquals("Hello", result);
    }

    @Test
    public void shouldAppendStringToInput() {
        // Given
        final StringAppend function = new StringAppend("!");

        // When
        final String result = function.apply("Hello");

        // Then
        assertEquals("Hello!", result);
    }

    @Test
    public void shouldHandleUnsetSuffix() {
        // Given
        final StringAppend function = new StringAppend();

        // When
        final String result = function.apply("Hello");

        // Then
        assertEquals("Hello", result); // right now result is Hellonull
    }

    @Override
    protected StringAppend getInstance() {
        return new StringAppend();
    }

    @Override
    protected Iterable<StringAppend> getDifferentInstances() {
        return Collections.singletonList(new StringAppend("different"));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ String.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ String.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringAppend function = new StringAppend("suffix");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringAppend\",%n" +
                "  \"suffix\" : \"suffix\"%n" +
                "}"), json);

        // When 2
        final StringAppend deserialisedMethod = JsonSerialiser.deserialise(json, StringAppend.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}