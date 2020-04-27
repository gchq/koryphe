/*
 * Copyright 2018-2020 Crown Copyright
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
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ToSetTest extends FunctionTest {
    @Test
    public void shouldConvertNullToSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = null;

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet((Object) null), result);
    }

    @Test
    public void shouldConvertStringToSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = "value1";

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet(value), result);
    }

    @Test
    public void shouldConvertArrayToSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = new String[]{"value1", "value2"};

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet((Object[]) value), result);
    }

    @Test
    public void shouldConvertListToSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = Lists.newArrayList("value1", "value2");

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet((List) value), result);
    }

    @Test
    public void shouldReturnAGivenSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = Sets.newHashSet("value1", "value2");

        // When
        Object result = function.apply(value);

        // Then
        assertSame(value, result);
    }

    @Override
    protected Function getInstance() {
        return new ToSet();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ToSet.class;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Object.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Set.class };
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToSet function = new ToSet();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToSet\"%n" +
                "}"), json);

        // When 2
        final ToSet deserialisedMethod = JsonSerialiser.deserialise(json, ToSet.class);

        // Then 2
        assertNotNull(deserialisedMethod);
    }
}
