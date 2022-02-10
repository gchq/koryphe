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

public class DefaultIfNullTest extends FunctionTest<DefaultIfNull> {

    private final static String DEFAULT_VALUE = "default";

    @Override
    protected DefaultIfNull getInstance() {
        return new DefaultIfNull();
    }

    @Override
    protected Iterable<DefaultIfNull> getDifferentInstancesOrNull() {
        return Collections.singletonList(new DefaultIfNull(42L));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final DefaultIfNull function = new DefaultIfNull(DEFAULT_VALUE);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.DefaultIfNull\",%n" +
                "  \"defaultValue\" : \"default\"" +
                "}"), json);

        // When 2
        final DefaultIfNull deserialised = JsonSerialiser.deserialise(json, DefaultIfNull.class);

        // Then
        assertNotNull(deserialised);
    }

    @Test
    public void shouldReturnDefaultValueIfNull() {
        // Given
        final DefaultIfNull defaultIfNull = new DefaultIfNull(DEFAULT_VALUE);

        // When
        final Object result = defaultIfNull.apply(null);

        // Then
        assertEquals(result, DEFAULT_VALUE);
    }

    @Test
    public void shouldReturnOriginalValueIfNotNull() {
        // Given
        final DefaultIfNull defaultIfNull = new DefaultIfNull(DEFAULT_VALUE);

        // When
        final Object result = defaultIfNull.apply("input");

        // Then
        assertEquals(result, "input");
    }
}
