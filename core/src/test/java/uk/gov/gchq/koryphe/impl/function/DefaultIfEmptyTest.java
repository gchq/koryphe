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
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultIfEmptyTest extends FunctionTest<DefaultIfEmpty> {

    private final static String DEFAULT_VALUE = "default";

    @Override
    protected DefaultIfEmpty getInstance() {
        return new DefaultIfEmpty();
    }

    @Override
    protected Iterable<DefaultIfEmpty> getDifferentInstancesOrNull() {
        return Collections.singletonList(new DefaultIfEmpty("Default"));
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
    public void shouldHandleNullInput() {
        // Given
        final DefaultIfEmpty function = new DefaultIfEmpty();

        // When
        final Object result = function.apply(null);

        // Then
        assertNull(result);
    }

    @Test
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
        assertEquals(DEFAULT_VALUE, result);
    }

    @Test
    public void shouldReturnOriginalValueIfNotEmpty() {
        // Given
        final List<String> iterable = Lists.newArrayList("first", "second");
        final DefaultIfEmpty defaultIfEmpty = new DefaultIfEmpty(DEFAULT_VALUE);

        // When
        final Object result = defaultIfEmpty.apply(iterable);

        // Then
        assertEquals(iterable, result);
    }
}