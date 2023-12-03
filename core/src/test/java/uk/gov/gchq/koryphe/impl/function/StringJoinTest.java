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
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class StringJoinTest extends FunctionTest<StringJoin> {

    @Test
    public void shouldHandleNullInput() {
        // Given
        final StringJoin function = new StringJoin();

        // When
        final String result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    public void shouldHandleNullDelimiter() {
        // Given
        final StringJoin<String> function = new StringJoin<>(null);
        final Set<String> input = new LinkedHashSet<>(Lists.newArrayList("a", "b", "c"));

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("abc");
    }

    @Test
    public void shouldJoinIterableOfStrings() {
        // Given
        final StringJoin<String> function = new StringJoin<>();
        final Set<String> input = new LinkedHashSet<>(Lists.newArrayList("a", "b", "c"));

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("abc");
    }

    @Test
    public void shouldJoinIterableOfIntegers() {
        // Given
        final StringJoin<Integer> function = new StringJoin<>();
        final List<Integer> input = Lists.newArrayList(1, 2, 3, 4, 5);

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("12345");
    }

    @Test
    public void shouldJoinIterableOfStringsWithDelimiter() {
        // Given
        final StringJoin<String> function = new StringJoin<>(",");
        final Set<String> input = new LinkedHashSet<>(Lists.newArrayList("a", "b", "c"));

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("a,b,c");
    }

    @Test
    public void shouldJoinIterableOfIntegersWithDelimiter() {
        // Given
        final StringJoin<Integer> function = new StringJoin<>(" ");
        final List<Integer> input = Lists.newArrayList(1, 2, 3, 4, 5);

        // When
        final String result = function.apply(input);

        // Then
        assertThat(result).isEqualTo("1 2 3 4 5");
    }

    @Override
    protected StringJoin getInstance() {
        return new StringJoin();
    }

    @Override
    protected Iterable<StringJoin> getDifferentInstancesOrNull() {
        return Collections.singletonList(new StringJoin("\t"));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ Iterable.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ String.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringJoin function = new StringJoin(",");

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.StringJoin\",%n" +
                "  \"delimiter\" : \",\"%n" +
                "}"), json);

        // When 2
        final StringJoin deserialisedMethod = JsonSerialiser.deserialise(json, StringJoin.class);

        // Then 2
        assertThat(deserialisedMethod).isNotNull();
    }
}
