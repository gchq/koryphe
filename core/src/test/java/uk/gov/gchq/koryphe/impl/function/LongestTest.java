/*
 * Copyright 2020-2022 Crown Copyright
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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class LongestTest extends FunctionTest<Longest> {

    @Test
    public void shouldHandleNullInputs() {
        // Given
        final Longest function = getInstance();

        // When
        final Object result = function.apply(null, null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldThrowExceptionForIncompatibleInputType() {
        // Given
        final Longest function = getInstance();
        final Object input1 = new Concat();
        final Object input2 = new Concat();

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> function.apply(input1, input2))
                .withMessage("Could not determine the size of the provided value");
    }

    @Test
    public void shouldReturnLongestStringInput() {
        // Given
        final Longest<String> function = new Longest<>();
        final String input1 = "A short string";
        final String input2 = "A longer string";

        // When
        final String result = function.apply(input1, input2);

        // Then
        assertThat(result).isEqualTo(input2);
    }

    @Test
    public void shouldReturnLongestObjectArrayInput() {
        // Given
        final Longest<Object[]> function = new Longest<>();
        final Object[] input1 = new Object[5];
        final Object[] input2 = new Object[10];

        // When
        final Object[] result = function.apply(input1, input2);

        // Then
        assertThat(result).isEqualTo(input2);
    }

    @Test
    public void shouldReturnLongestListInput() {
        // Given
        final Longest<List<Integer>> function = new Longest<>();
        final List<Integer> input1 = Lists.newArrayList(1);
        final List<Integer> input2 = Lists.newArrayList(1, 2, 3);

        // When
        final List<Integer> result = function.apply(input1, input2);

        // Then
        assertThat(result).isEqualTo(input2);
    }

    @Test
    public void shouldReturnLongestSetInput() {
        // Given
        final Longest<Set<Integer>> function = new Longest<>();
        final Set<Integer> input1 = Sets.newHashSet(1);
        final Set<Integer> input2 = Sets.newHashSet(1, 2, 3);

        // When
        final Set<Integer> result = function.apply(input1, input2);

        // Then
        assertThat(result).isEqualTo(input2);
    }

    @Test
    public void shouldReturnLongestMapInput() {
        // Given
        final Longest<Map<String, String>> function = new Longest<>();
        final Map<String, String> input1 = new HashMap<>();
        final Map<String, String> input2 = Maps.asMap(Sets.newHashSet("1"), k -> k);

        // When
        final Map<String, String> result = function.apply(input1, input2);

        // Then
        assertThat(result).isEqualTo(input2);
    }

    @Override
    protected Longest getInstance() {
        return new Longest();
    }

    @Override
    protected Iterable<Longest> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class, Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Object.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Longest function = new Longest();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.Longest\"%n" +
                "}"), json);

        // When 2
        final Longest deserialised = JsonSerialiser.deserialise(json, Longest.class);

        // Then
        assertThat(deserialised).isNotNull();
    }
}
