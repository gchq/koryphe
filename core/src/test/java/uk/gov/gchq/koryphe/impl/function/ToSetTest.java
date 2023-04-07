/*
 * Copyright 2018-2022 Crown Copyright
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;

public class ToSetTest extends FunctionTest<ToSet> {

    @Test
    public void shouldConvertNullToHashSet() {
        // Given
        final ToSet function = new ToSet();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result)
                .isEqualTo(new HashSet<>(Arrays.asList((Object) null)))
                .asInstanceOf(COLLECTION)
                .isNotNull()
                .isNotEmpty()
                .containsExactly((Object) null);
    }

    @Test
    public void shouldConvertNullToEmptyTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result)
                .isExactlyInstanceOf(TreeSet.class)
                .isEqualTo(new TreeSet<>())
                .asInstanceOf(COLLECTION)
                .isEmpty();
    }

    @Test
    public void shouldConvertStringToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new HashSet<>(Arrays.asList(value)))
                .isExactlyInstanceOf(HashSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactly(value);
    }

    @Test
    public void shouldConvertStringToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(new HashSet<>(Arrays.asList(value))))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactly(value);
    }

    @Test
    public void shouldConvertStringToTreeSetUsingfullyQualifiedClassString() throws ClassNotFoundException {
        // Given
        final ToSet function = new ToSet("java.util.TreeSet");
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(new HashSet<>(Arrays.asList(value))))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactly(value);
    }

    @Test
    public void shouldConvertStringToTreeSetUsingSimpleClassString() throws ClassNotFoundException {
        // Given
        final ToSet function = new ToSet("TreeSet");
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(new HashSet<>(Arrays.asList(value))))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactly(value);
    }

    @Test
    public void shouldConvertArrayToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = new String[] {"value1", "value2"};

        // When
        Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new HashSet<>(Arrays.asList((Object[]) value)))
                .isExactlyInstanceOf(HashSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList((Object[]) value));
    }

    @Test
    public void shouldConvertArrayToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = new String[] {"value1", "value2"};

        // When
        Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(new HashSet<>(Arrays.asList((Object[]) value))))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(Arrays.asList((Object[]) value));
    }

    @Test
    public void shouldConvertListToHashSet() {
        // Given
        final ToSet function = new ToSet();
        final List<String> value = Arrays.asList("value1", "value2");

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new HashSet<>(value))
                .isExactlyInstanceOf(HashSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(value);
    }

    @Test
    public void shouldConvertListToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final List<String> value = Arrays.asList("value1", "value2");

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(new HashSet<>(value)))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(value);
    }

    @Test
    public void shouldReturnAGivenHashSet() {
        // Given
        final ToSet function = new ToSet();
        final Object value = new HashSet<>(Arrays.asList("value1", "value2"));

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result).isSameAs(value);
    }

    @Test
    public void shouldReturnAGivenTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Object value = new TreeSet(new HashSet<>(Arrays.asList("value1", "value2")));

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result).isSameAs(value);
    }

    @Test
    public void shouldConvertTreeSetToHashSet() {
        // Given
        final ToSet function = new ToSet(HashSet.class);
        final Set value = new TreeSet(new HashSet<>(Arrays.asList("value1", "value2")));

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new HashSet<>(value))
                .isExactlyInstanceOf(HashSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(value);
    }

    @Test
    public void shouldConvertHashSetToTreeSet() {
        // Given
        final ToSet function = new ToSet(TreeSet.class);
        final Set value = new HashSet<>(Arrays.asList("value1", "value2"));

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(result)
                .isEqualTo(new TreeSet(value))
                .isExactlyInstanceOf(TreeSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactlyInAnyOrderElementsOf(value);
    }

    @Test
    public void shouldHaveHashSetAsDefault() {
        // Given
        final ToSet function = new ToSet();
        final Object value = "value1";

        // When
        final Object result = function.apply(value);

        // Then
        assertThat(function).returns(HashSet.class, ToSet::getImplementation);
        assertThat(result)
                .isExactlyInstanceOf(HashSet.class)
                .asInstanceOf(COLLECTION)
                .containsExactly(value);
    }

    @Test
    public void shouldThrowExceptionWhenUnrecognisedSetImplementationUsed() {
        // Given
        final ToSet function = new ToSet(LinkedHashSet.class);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(null))
                .withMessage("Unrecognised Set implementation");
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
        assertThat(deserialisedMethod)
                .isNotNull()
                .returns(TreeSet.class, from(ToSet::getImplementation));
    }
}
