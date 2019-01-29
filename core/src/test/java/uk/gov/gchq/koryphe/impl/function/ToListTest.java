/*
 * Copyright 2018-2019 Crown Copyright
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
import com.google.common.collect.Sets;
import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ToListTest extends FunctionTest {
    @Test
    public void shouldConvertNullToList() {
        // Given
        final ToList function = new ToList();
        final Object value = null;

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Lists.newArrayList((Object) null), result);
    }

    @Test
    public void shouldConvertStringToList() {
        // Given
        final ToList function = new ToList();
        final Object value = "value1";

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Lists.newArrayList(value), result);
    }

    @Test
    public void shouldConvertArrayToList() {
        // Given
        final ToList function = new ToList();
        final Object value = new String[]{"value1", "value2"};

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Lists.newArrayList((Object[]) value), result);
    }

    @Test
    public void shouldConvertSetToList() {
        // Given
        final ToList function = new ToList();
        final Object value = Sets.newLinkedHashSet(Arrays.asList("value1", "value2"));

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Lists.newArrayList((Set) value), result);
    }

    @Test
    public void shouldReturnAGivenList() {
        // Given
        final ToList function = new ToList();
        final Object value = Lists.newArrayList("value1", "value2");

        // When
        Object result = function.apply(value);

        // Then
        assertSame(value, result);
    }

    @Override
    protected Function getInstance() {
        return new ToList();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToList.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToList function = new ToList();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToList\"%n" +
                "}"), json);

        // When 2
        final ToList deserialisedMethod = JsonSerialiser.deserialise(json, ToList.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
