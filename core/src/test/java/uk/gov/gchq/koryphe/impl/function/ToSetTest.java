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
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ToSetTest extends FunctionTest<ToSet> {

    @Test
    public void shouldConvertNullToHashSet() {
        // Given
        final ToSet function = new ToSet();

        // When
        final Object result = function.apply(null);

        // Then
        assertEquals(Sets.newHashSet((Object) null), result);
    }

    @Test
    public void shouldConvertNullToEmptyTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);

        // When
        final Object result = function.apply(null);

        // Then
        assertEquals(Sets.newTreeSet(), result);
    }

    @Test
    public void shouldConvertStringToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet(value), result);
    }

    @Test
    public void shouldConvertStringToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = "value1";
        final Set expected = new TreeSet(Sets.newHashSet(value));

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void shouldConvertStringToTreeSetUsingfullyQualifiedClassString() throws ClassNotFoundException {
        // Given
        final ToSet function = new ToSet("java.util.TreeSet");
        final Object value = "value1";
        final Set expected = new TreeSet(Sets.newHashSet(value));

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void shouldConvertStringToTreeSetUsingSimpleClassString() throws ClassNotFoundException {
        // Given
        final ToSet function = new ToSet("TreeSet");
        final Object value = "value1";
        final Set expected = new TreeSet(Sets.newHashSet(value));

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected, result);
        }

    @Test
    public void shouldConvertArrayToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = new String[] {"value1", "value2"};

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet((Object[]) value), result);
    }

    @Test
    public void shouldConvertArrayToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = new String[] {"value1", "value2"};
        final Set expected = new TreeSet(Sets.newHashSet((Object[]) value));

        // When
        Object result = function.apply(value);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void shouldConvertListToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = Lists.newArrayList("value1", "value2");

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(Sets.newHashSet((List) value), result);
    }

    @Test
    public void shouldConvertListToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = Lists.newArrayList("value1", "value2");
        final Set expected = new TreeSet(Sets.newHashSet((List) value));

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected, result);
    }

    @Test
    public void shouldReturnAGivenHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = Sets.newHashSet("value1", "value2");

        // When
        final Object result = function.apply(value);

        // Then
        assertSame(value, result);
    }

    @Test
    public void shouldReturnAGivenTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = new TreeSet(Sets.newHashSet("value1", "value2"));

        // When
        final Object result = function.apply(value);

        // Then
        assertSame(value, result);
    }

    @Test
    public void shouldConvertTreeSetToHashSet() {
        // Given
        final ToSet function = new ToSet(HashSet.class);
        final Set expected = Sets.newHashSet("value1", "value2");
        final Set value = new TreeSet(expected);

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected.getClass(), result.getClass());
        assertNotEquals(value.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    @Test
    public void shouldConvertHashSetToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Set value = Sets.newHashSet("value1", "value2");
        final Set expected = new TreeSet(value);


        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(expected.getClass(), result.getClass());
        assertNotEquals(value.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    @Test
    public void shouldHaveHashSetAsDefault() {
        // Given
        final ToSet function = new ToSet();
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertEquals(HashSet.class, function.getImplementation());
        assertEquals(HashSet.class, result.getClass());
        assertEquals(Sets.newHashSet(value), result);
    }

    @Test
    public void shouldThrowExceptionWhenUnrecognisedSetImplementationUsed() {
        // Given
        final ToSet function = new ToSet(LinkedHashSet.class);

        // When / Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> function.apply(null));
        assertEquals("Unrecognised Set implementation", e.getMessage());
    }

    @Override
    protected ToSet getInstance() {
        return new ToSet();
    }

    @Override
    protected Iterable<ToSet> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Set.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ToSet function = new ToSet(TreeSet.class);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.ToSet\",%n" +
                "  \"implementation\" : \"java.util.TreeSet\"%n" +
                "}"), json);

        // When 2
        final ToSet deserialisedMethod = JsonSerialiser.deserialise(json, ToSet.class);

        // Then 2
        assertNotNull(deserialisedMethod);
        assertEquals(TreeSet.class, deserialisedMethod.getImplementation());
    }
}
