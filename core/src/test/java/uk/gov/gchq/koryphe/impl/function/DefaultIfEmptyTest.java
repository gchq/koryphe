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

import com.google.common.collect.Lists;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultIfEmptyTest extends FunctionTest {
    private final static String DEFAULT_VALUE = "default";

    @Override
    protected Function getInstance() {
        return new DefaultIfEmpty();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return DefaultIfEmpty.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ Object.class };
    }

    @Test
    public void shouldHandleNullInput() {
        // Given
        final DefaultIfEmpty function = new DefaultIfEmpty();

        // When
        final Object result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final DefaultIfEmpty function = new DefaultIfEmpty(DEFAULT_VALUE);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.DefaultIfEmpty\",%n" +
                "  \"defaultValue\" : \"default\"" +
                "}"), json);

        // When 2
        final DefaultIfEmpty deserialised = JsonSerialiser.deserialise(json, DefaultIfEmpty.class);

        // Then
        assertNotNull(deserialised);
    }

    @Test
    public void shouldReturnDefaultValueIfEmpty() {
        // Given
        final DefaultIfEmpty defaultIfEmpty = new DefaultIfEmpty(DEFAULT_VALUE);

        // When
        final Object result = defaultIfEmpty.apply(Collections.emptyList());

        // Then
        assertEquals(result, DEFAULT_VALUE);
    }

    @Test
    public void shouldReturnOriginalValueIfNotEmpty() {
        // Given
        final List<String> iterable = Lists.newArrayList("first", "second");
        final DefaultIfEmpty defaultIfEmpty = new DefaultIfEmpty(DEFAULT_VALUE);

        // When
        final Object result = defaultIfEmpty.apply(iterable);

        // Then
        assertEquals(result, iterable);
    }
}